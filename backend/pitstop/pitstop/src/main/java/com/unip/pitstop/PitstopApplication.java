package com.unip.pitstop;

import com.unip.pitstop.model.Carro;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PitstopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PitstopApplication.class, args);

	}

}
