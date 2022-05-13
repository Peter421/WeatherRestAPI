/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Peter.app.WeatherRestAPI.Controllers;

/**
 *
 * @author peter
 */
import com.Peter.app.WeatherRestAPI.Exceptions.IllegalInputException;
import com.Peter.app.WeatherRestAPI.Models.Sensor;
import com.Peter.app.WeatherRestAPI.services.WeatherService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class APIController {
       
    @Autowired
    private WeatherService ws;
    
    public APIController(WeatherService ws){
        this.ws = ws;
    }
    
    @GetMapping(value = "/update/{sensor}")
    public ResponseEntity<String> updateSensor(@PathVariable String sensor) throws IOException, MalformedURLException, IllegalInputException{
        return new ResponseEntity<>(ws.updateSensor(sensor),HttpStatus.FOUND);
    }
    
   @GetMapping(value = {"/get/sensor/", "/get/sensor/{sensorId}","/get/sensor/{sensorId}/{date1}/{date2}"})
   @ResponseBody
    public ResponseEntity<Object[]> getSensor(@PathVariable(required = false) String sensorId,
            @PathVariable(required=false) String date1, @PathVariable(required=false) String date2) throws ParseException, IllegalInputException{
               return new ResponseEntity<>(ws.getSensor(sensorId,date1,date2),HttpStatus.FOUND);
    }
        
    @GetMapping(value = {"/get/avg/{metric}","/get/avg/{metric}/{date1}","/get/avg/{metric}/{date1}/{date2}"})
    public ResponseEntity<HashMap<String,Double>>getAvgMetric(@PathVariable(required =false) String metric, 
            @PathVariable(required =false) String date1, @PathVariable(required =false) String date2) throws Exception{
              return new ResponseEntity<>(ws.getAvgMetric(metric, date1, date2),HttpStatus.FOUND);
    }
    
    @GetMapping(value = {"/get/weather/{date1}/{date2}","/get/weather/{date1}"})
    public ResponseEntity<List<Sensor>> getBetweenDates(@PathVariable String date1,@PathVariable(required = false) String date2) throws ParseException{
        return new ResponseEntity<>(ws.getBetweenDates(date1, date2),HttpStatus.FOUND);
    }
     
    @PostMapping(value = "/saves")
    public ResponseEntity<String> postSensors(@RequestBody List<Sensor> sensors){
        return new ResponseEntity<>(ws.postSensors(sensors),HttpStatus.ACCEPTED);
    }
   
}

