package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api1")
public class MetricasController {

    List<Horário> horariosCarregados = ManagerHorários.getHorariosCarregados();

    @GetMapping("/getMetricas")
    public ResponseEntity<List<int[]>> getMetricas() {
        List<int[]> listaDeArraysMetricas = new ArrayList<>();
        System.out.println("Numero de ghoarioaud: " + horariosCarregados.size());
        for (Horário horario : horariosCarregados) {
            if (horario == null) {
                return ResponseEntity.status(404).body(null); // Horário não está carregado
            }

            Metricas metricas = horario.getQualidade();
            if (metricas == null) {
                return ResponseEntity.status(404).body(null);
            }

            Map<Integer, List<Integer>> mapaErros = metricas.getMapaErros();
            if (mapaErros != null) {
                int[] arrayOfMetrics = new int[4]; // Cria um novo array para cada horário carregado
                for (Integer chave : mapaErros.keySet()) {
                    List<Integer> listaDeErros = mapaErros.get(chave);

                    if (listaDeErros.contains(1) && arrayOfMetrics[0] != 1000) {
                        arrayOfMetrics[0]++;
                    } else if (listaDeErros.contains(2) && arrayOfMetrics[1] != 1000) {
                        arrayOfMetrics[1]++;
                    } else if (listaDeErros.contains(3) && arrayOfMetrics[2] != 1000) {
                        arrayOfMetrics[2]++;
                    } else if (listaDeErros.contains(5) && arrayOfMetrics[3] != 1000) {
                        System.out.println("Entrei");
                        arrayOfMetrics[3]++;
                    }
                }

                // Adiciona o array de métricas do horário atual à lista
                listaDeArraysMetricas.add(arrayOfMetrics);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        }

        System.out.println("Número de Horários Carregados: " + listaDeArraysMetricas.size());
        return ResponseEntity.ok(listaDeArraysMetricas);
    }
}
