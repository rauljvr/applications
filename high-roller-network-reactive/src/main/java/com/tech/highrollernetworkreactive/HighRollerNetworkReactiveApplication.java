package com.tech.highrollernetworkreactive;

import me.yaman.can.webflux.h2console.H2ConsoleAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value={H2ConsoleAutoConfiguration.class})
public class HighRollerNetworkReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(HighRollerNetworkReactiveApplication.class, args);
	}

}
