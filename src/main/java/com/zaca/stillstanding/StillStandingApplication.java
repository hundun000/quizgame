package com.zaca.stillstanding;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


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
