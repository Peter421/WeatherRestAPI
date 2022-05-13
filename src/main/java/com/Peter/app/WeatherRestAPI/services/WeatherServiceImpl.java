/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Peter.app.WeatherRestAPI.services;

import com.Peter.app.WeatherRestAPI.Exceptions.IllegalInputException;
import com.Peter.app.WeatherRestAPI.Models.Sensor;
import com.Peter.app.WeatherRestAPI.Repo.SensorRepo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;


/**
 *
 * @author peter
 */
@Service
public class WeatherServiceImpl implements WeatherService{
    
    private SensorRepo sr; 
    
    public WeatherServiceImpl(SensorRepo sr){
        super();
        this.sr = sr;
    }
    
    @Override
    public Map<String,Object> jsonToMap(String json){
        Map<String,Object> map = new Gson().fromJson(json, new TypeToken<HashMap<String,Object>>() {}.getType());
  
        return map;
    }
    
    @Override
    public String postSensor(Sensor sensor){
      
     sr.save(sensor);
    return "saved..";
        }
    
    
    @Override
    public String updateSensor(String sensor) throws MalformedURLException, IOException, IllegalInputException{
       String API_KEY ="75ba3eb0de6b73d78ab55758595452e5";
       String lon="";
       String lat="";
         
        switch (sensor) {
            case "galway":
                lon = "-9.062691";
                lat = "53.270962";
                break;
            case "madrid":
                lon = "-3.703790";
                lat = "40.416775";
                break;
            case "munich":
                lon = "11.576124";
                lat = "48.137154";
                break;
            case "rome":
                lon = "12.496366";
                lat = "41.902782";
                break;
            case "texas":
                lon = "-100.000000";
                lat = "31.000000";
                break;
            default:
                throw new IllegalInputException("sensor does not exist");
        }
         
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+API_KEY+"&units=metric";
        
        
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
        String nxtLine;
        while((nxtLine = rd.readLine()) != null){
            result.append(nxtLine);
        }
        rd.close();
     
        Map<String, Object> resMap = jsonToMap(result.toString());
        Map<String, Object> mainMap = jsonToMap(resMap.get("main").toString());
        Map<String, Object> windMap = jsonToMap(resMap.get("wind").toString());
        Map<String, Object> cloudMap = jsonToMap(resMap.get("clouds").toString());
       
        
        Sensor weatherSensor = new Sensor();
        weatherSensor.setTemperature(Double.parseDouble(mainMap.get("temp").toString()));
        weatherSensor.setSensorId("galwayIreland");
        weatherSensor.setWindSpeed(Double.parseDouble(windMap.get("speed").toString()));
        weatherSensor.setHumidity(Double.parseDouble(mainMap.get("humidity").toString()));
        weatherSensor.setCloudCover(Double.parseDouble(cloudMap.get("all").toString()));
        weatherSensor.setPressure(Double.parseDouble(mainMap.get("pressure").toString()));
        
        
        DateTimeFormatter dformat = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDate now = LocalDate.now(); 
   
        weatherSensor.setDate(dformat.format(now));
        
        postSensor(weatherSensor);
         
        return sensor+" info has been updated...";
    }
    
    @Override
    public LocalDate convertDate(String date) throws ParseException{
        
        try{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate d = LocalDate.parse(date, formatter);
        return d;
        }catch(DateTimeParseException e){
            //System.out.println("error here ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate d = LocalDate.parse(date, formatter);
        return d;
        }
       
    }
    
    @Override
    public List<Sensor> getBetweenDates(String date1, String date2) throws ParseException{
        List<Sensor> repo = sr.findAll();
        List<Sensor> result = new ArrayList();
        if(date1 == null){
           // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
           LocalDate now = LocalDate.now(); 
        date1 = now.toString();
        
        LocalDate date = convertDate(date1);
        date1 =date.toString();
        }
        if(date2 ==null){
            date2 = date1;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate d1 = convertDate(date1); //LocalDate.parse(date1, formatter);
        LocalDate d2 = convertDate(date2);//LocalDate.parse(date2, formatter);
        
        
          long daysBetween = Duration.between(d1.atStartOfDay(), d2.atStartOfDay()).toDays();
          
          if(daysBetween >30){
              d1 = LocalDate.now();
              d1.format(formatter);
              d2 = d1;
              d2.format(formatter);
              
              d2 = d2.plusDays(30);   
          }

           DayOfWeek dayOfWeek1 = DayOfWeek.from(d1);
          DayOfWeek dayOfWeek2 = DayOfWeek.from(d2);
        
        for(int i=0;i<repo.size();i++){
            LocalDate d = convertDate(repo.get(i).getDate());
            DayOfWeek dow = DayOfWeek.from(d);
            
            if(dow.getValue() >= dayOfWeek1.getValue()  && d.getMonth().getValue()>= d1.getMonth().getValue() && 
                    dow.getValue() <= dayOfWeek2.getValue()  && d.getMonth().getValue()<= d2.getMonth().getValue()){
               result.add(repo.get(i));
            }
        }
        return  result;
        
    }
    
    @Override
    public Object[] getSensor(String sensorId,String date1,String date2) throws ParseException, IllegalInputException{
       
        
        List<String> query = new ArrayList();
        List<Sensor> all = getBetweenDates(date1,date2);
        List<Sensor> sensor = new ArrayList();
        
        if(date1 == null){
            date1 = " the months start";
            date2 =" the months end";
        }
        
        
       if(sensorId != null){
        String s1 = "galwayIreland";
        String s2 = "munichGermany";
        String s3 = "texasUS";
        String s4 = "romeItaly";
        String s5 = "madridSpain";
        
        if(sensorId.equals("all") ){
           
            sensor = all;
            for(int i=0;i<all.size();i++){
                //all.get(i).initialise();
            }
            query.add("Get all Weather sensors between "+date1+" and "+date2);
            
        }else if(sensorId.equals("galway&munich&texas&rome")){
            for(int i = 0; i< all.size();i++){
            if(all.get(i).getSensorId().equals(s1) || all.get(i).getSensorId().equals(s2) || all.get(i).getSensorId().equals(s3) || 
                    all.get(i).getSensorId().equals(s4) ){
                //all.get(i).initialise();
                sensor.add(all.get(i));
            }
        }
              query.add("Get Galway, Munich, Texas and Rome Weather sensors between "+date1+" and "+date2);
            
        }else if(sensorId.equals("galway&munich&texas")){
            for(int i = 0; i< all.size();i++){
            if(all.get(i).getSensorId().equals(s1) || all.get(i).getSensorId().equals(s2) || all.get(i).getSensorId().equals(s3)){
               // all.get(i).initialise();
                sensor.add(all.get(i));
            }
        }
            query.add("Get Galway, Munich and Texas Weather sensors between "+date1+" and "+date2);
           
        }else if(sensorId.equals("galway&munich")){
            for(int i = 0; i< all.size();i++){
            if(all.get(i).getSensorId().equals(s1) || all.get(i).getSensorId().equals(s2)){
               // all.get(i).initialise();
                sensor.add(all.get(i));
            }
        }
            query.add("Get Galway and Munich Weather sensors between "+date1+" and "+date2);
            
        } else if(sensorId.equals(s1) || sensorId.equals(s2) || sensorId.equals(s3) ||
                sensorId.equals(s4) || sensorId.equals(s5)){
        for(int i = 0; i< all.size();i++){
            if(all.get(i).getSensorId().equals(sensorId)){
                sensor.add(all.get(i));
            }
        }
        query.add("Get sensor " + sensorId + ":");
        
        }else throw new IllegalInputException("sensor does not exist");
        
        
        return  new Object[]{query, sensor}; 
        }else 
           sensor = all;
            query.add("Get all Weather sensors between "+date1+" and "+date2);
           return new Object[]{query, sensor};
    
    }
    
    @Override
     public HashMap<String,Double> getAvgMetric(String metric, String date1, String date2) throws Exception{
         ArrayList<String> mlist = new ArrayList<>();
         HashMap<String, Double> map = new HashMap<String, Double>();
      
        if(metric == null){
            metric = "temp";
            mlist.add("temp");
        }else{
             String[] metrics = metric.split("&");
             
             
            Collections.addAll(mlist, metrics);
  
        }
        
        List<Sensor> repo = getBetweenDates(date1,date2);
        double sum=0,sum2=0,sum3=0,sum4=0;
        
         if(date1 == null){
            date1 = " the months start";
            date2 =" the months end";
        }
     
        if(metric.equals("temp") || mlist.contains("temp")){
            for(int i = 0;i < repo.size();i++){
               // if(repo.get(i).getStatus() == true)
                sum += repo.get(i).getTemperature();
            }  
            
            map.put("Give me the average the temperature between "+date1+" and "+date2,sum/repo.size());
        } 
        if(metric.equals("windspeed") || mlist.contains("windspeed")){
           
             for(int i = 0;i < repo.size();i++){
                 //if(repo.get(i).getStatus() == true)
                sum2 += repo.get(i).getWindSpeed();
            }   
             map.put("Give me the average the wind speed  between "+date1+" and "+date2 , sum2/repo.size());
        }
        
        if(metric.equals("humidity")|| mlist.contains("humidity")){
           
             for(int i = 0;i < repo.size();i++){
               //  if(repo.get(i).getStatus() == true)
                sum3 += repo.get(i).getHumidity();
            }  
             map.put("Give me the average the humidity  between "+date1+" and "+date2 , sum3/repo.size());
            
        }
        
        if(metric.equals("cloudcover")|| mlist.contains("cloudcover")){
            
             for(int i = 0;i < repo.size();i++){
                 //if(repo.get(i).getStatus() == true){
                sum4 += repo.get(i).getCloudCover();
                 //}
            } 
              
 
             map.put("Give me the average the cloud cover  between "+date1+" and "+date2 , sum4/repo.size());
            
        }
      return map;     
    }
     
   @Override
   public String postSensors(List<Sensor> sensor){
      
     sr.saveAll(sensor);
    return "saved..";
    }  
}
