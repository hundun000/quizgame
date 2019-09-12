package com.zaca.stillstanding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.zaca.stillstanding.gui.MyFrame;

@SpringBootApplication
public class StillStandingApplication {
 
	public static void main(String[] args) {
		//SpringApplication.run(StillStandingApplication.class, args);
		
		SpringApplicationBuilder builder = new SpringApplicationBuilder(StillStandingApplication.class);
	    builder.headless(false);
	    builder.run(args);
	    System.out.println("StillStandingApplication.main run.");
	}
	
	

}
