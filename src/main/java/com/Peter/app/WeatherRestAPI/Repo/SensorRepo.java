/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Peter.app.WeatherRestAPI.Repo;

/**
 *
 * @author peter
 */
import com.Peter.app.WeatherRestAPI.Models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author peter
 */
public interface SensorRepo extends JpaRepository<Sensor, String> {
    
}
