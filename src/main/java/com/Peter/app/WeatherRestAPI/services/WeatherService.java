/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Peter.app.WeatherRestAPI.services;

import com.Peter.app.WeatherRestAPI.Exceptions.IllegalInputException;
import com.Peter.app.WeatherRestAPI.Models.Sensor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author peter
 */
public interface WeatherService {
    public Map<String,Object> jsonToMap(String json);
    public String updateSensor(String sensor) throws MalformedURLException, IOException, IllegalInputException;
    public String postSensor(Sensor sensor);
    public Object[] getSensor(String sensorId,String date1,String date2) throws ParseException, IllegalInputException;
    List<Sensor> getBetweenDates(String date1, String date2) throws ParseException;
    public LocalDate convertDate(String date) throws ParseException;
    public HashMap<String,Double> getAvgMetric(String metric, String date1, String date2) throws Exception;
    public String postSensors(List<Sensor> sensor);
}
