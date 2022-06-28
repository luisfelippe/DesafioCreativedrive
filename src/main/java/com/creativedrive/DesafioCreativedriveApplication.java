package com.creativedrive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.creativedrive.security.property.JwtConfiguration;

@SpringBootApplication
@EntityScan({"com.creativedrive"})
@ComponentScan("com.creativedrive")
@EnableConfigurationProperties(value = JwtConfiguration.class)
public class DesafioCreativedriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioCreativedriveApplication.class, args);
	}

}
