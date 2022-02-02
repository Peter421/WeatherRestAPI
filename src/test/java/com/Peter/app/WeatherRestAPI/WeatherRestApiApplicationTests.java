package com.Peter.app.WeatherRestAPI;

import com.Peter.app.WeatherRestAPI.Controllers.APIController;
import com.Peter.app.WeatherRestAPI.Models.Sensor;
import com.Peter.app.WeatherRestAPI.Repo.SensorRepo;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
 

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class) 
class WeatherRestApiApplicationTests {
     
    @Autowired
    private SensorRepo sr;
    Sensor sensor = new Sensor();
    APIController controller = new APIController();
           
	@Test
	void contextLoads() {
            sensor.setHumidity(10.8);
            sensor.setPressure(11.0);
            sensor.setCloudCover(12.0);
            sensor.setWindSpeed(9.34);
            sensor.setTemperature(8.5);
            sensor.setSensorId("galwayIreland");
             DateTimeFormatter dformat = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDate now = LocalDate.now(); 
   
        sensor.setDate(dformat.format(now));
        long l = 1;
        sr.save(sensor);
        
        
            String response = controller.postSensor(sensor);
        assertEquals("saved..",response);
	}
        
        @Test
        public void TestSensorRepo(){
            List<Sensor> sensor = sr.findAll();
            assertThat(sensor).size().isGreaterThan(0);
           
        }
        
        @Test
        public void dateConversion() throws ParseException{
            LocalDate response = controller.convertDate("2022-02-02");//whatever todays date is
            LocalDate now = LocalDate.now(); 
            assertEquals(response,now);
        }
        

}
