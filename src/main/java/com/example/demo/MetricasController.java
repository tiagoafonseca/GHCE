package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api1")
public class MetricasController {

    static Horário horarioCarregado = null;
    // Endpoint para obter o mapa de erros
    @GetMapping("/getMapaErros")
    public ResponseEntity<Map<Integer, List<Integer>>> getMapaErros() {
        if (horarioCarregado == null) {
            return ResponseEntity.status(404).body(null); // Horário não está carregado
        }

        // Acesse o mapaErros a partir das métricas do horário carregado
        Metricas metricas = horarioCarregado.getQualidade(); // Certifique-se de que existe
        if (metricas != null) {
            return ResponseEntity.ok(metricas.getMapaErros());
        }

        return ResponseEntity.status(500).body(null); // Caso algo dê errado
    }
}

