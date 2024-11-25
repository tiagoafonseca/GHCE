package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload")
    public List<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        List<Map<String, String>> horarios = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine(); // Lê a primeira linha (cabeçalho)
            if (headerLine == null) {
                throw new RuntimeException("Arquivo vazio");
            }
            String[] headers = headerLine.split(","); // Divide os cabeçalhos por vírgulas

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] valores = linha.split(",");
                Map<String, String> horario = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    horario.put(headers[i], valores[i]);
                }
                horarios.add(horario);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + e.getMessage());
        }

        return horarios; // Retorna os dados como JSON
    }
}
