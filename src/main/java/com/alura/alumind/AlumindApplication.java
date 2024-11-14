package com.alura.alumind;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlumindApplication {

	public static void main(String[] args) {

		// Carrega as variáveis de ambiente do arquivo .env
		Dotenv dotenv = Dotenv.load();

		SpringApplication.run(AlumindApplication.class, args);
	}

}
