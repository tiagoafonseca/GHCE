package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@SpringBootApplication
@Controller
public class DemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/")
	public String redirectToInterfaceUpload() throws IOException {

		ManagerHorários.loadAllHorários();
		return "redirect:/html/InterfaceUpload.html"; // Redireciona para o arquivo HTML na pasta static
	}

}
