package com.example.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("com.example.elasticsearch")
public class HigApplication {

	public static void main(String[] args) {
		SpringApplication.run(HigApplication.class, args);
	}

}
