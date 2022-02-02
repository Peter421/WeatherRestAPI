/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Peter.app.WeatherRestAPI.Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 *
 * @author peter
 */
import javax.persistence.*;
import java.util.Random;

@Entity
public class Sensor implements Serializable {
    
    @Transient
    Random rand = new Random();
    
    //@Transient
    //boolean initialised = false;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = rand.nextLong() ;
     
    @Column
    private String date;  
    
     @Column
    private String sensorId;
    
    @Column
    private double temperature;
    
    @Column
    private double humidity;
    
    @Column
    private double windSpeed;
    
    @Column
    private double pressure;
           // = rand.nextDouble();
    
    @Column
    private double cloudCover;
/*
    public long getId() {
        return id;
    }*/
    
    
    public String getDate() {
        return date;
    }

    public void setDate(String date1) {
        this.date = date1;
    }
    
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
/*
    public void setId(long id) {
        this.id = id;
    }*/

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(double cloudCover) {
        this.cloudCover = cloudCover;
    }
    /*
    public boolean getStatus(){
        return initialised;
    }
    
     public void initialise(){
         initialised = true;
     }
    */
    
}
