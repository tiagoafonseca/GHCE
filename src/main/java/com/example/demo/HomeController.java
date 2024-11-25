package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToInterfaceUpload() {
        return "redirect:/InterfaceUpload.html"; // Redireciona para o arquivo HTML na pasta static
    }
}
