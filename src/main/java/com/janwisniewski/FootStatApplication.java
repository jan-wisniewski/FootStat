package com.janwisniewski;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class FootStatApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootStatApplication.class, args);
	}

}
