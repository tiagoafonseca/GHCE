package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api2")
public class ManagerHorários {
    static List<Horário> horarios = new ArrayList<>();


    @GetMapping("/recieveListOfHorários")
    public static ResponseEntity<Map<String, Object>> loadAllHorários() {
        ObjectMapper objectMapper = new ObjectMapper();
        String[] qualidades;
        String[] nomes;
        String[] datas;
        File folder = new File("./Horários/");
        File[] listOfFiles = folder.listFiles();
        Map<String, Object> response = new HashMap<>();

        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("Horário: " + listOfFiles[i].getName());
                }
            }

            nomes = new String[listOfFiles.length];
            qualidades = new String[listOfFiles.length];
            datas = new String[listOfFiles.length];


        } else {
            return null;
        }

        int j = 0;
        for (File file : listOfFiles) {
            try {
                Horário h = objectMapper.readValue(file, Horário.class);
                nomes[j] = h.getName();
                qualidades[j] = String.valueOf(h.getQualidade().getHorarioPontuacao());
                datas[j] = h.getDate();
                horarios.add(h);
                j++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.put("nomes", nomes);
            response.put("qualidades", qualidades);
            response.put("datas", datas);
        }

        System.out.println("Tamanho da minha pica: " + horarios.size() + "km");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/getSelectedHorario")
    public static ResponseEntity<Horário> getSelectedHorario(@RequestParam("nomeHorario") String nomeHorario) {
        for (Horário horario : horarios) {
            if (horario.name.equals(nomeHorario)) {
                return ResponseEntity.ok(horario);

            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/deleteHorario")
    public static ResponseEntity<String> deleteHorario(@RequestParam("nomeHorario") String nomeHorario) {
        // Remover o horário da lista
        boolean removedFromList = horarios.removeIf(horario -> horario.getName().equals(nomeHorario));

        // Se não foi encontrado na lista, retorna erro
        if (!removedFromList) {
            return ResponseEntity.status(404).body("Horário não encontrado na lista.");
        }

        // Remover o arquivo do sistema de arquivos
        File folder = new File("./Horários/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().equals(nomeHorario + ".json")) {
                    if (file.delete()) {
                        return ResponseEntity.ok("Horário removido com sucesso!");
                    } else {
                        return ResponseEntity.status(500).body("Erro ao remover o arquivo do sistema de arquivos.");
                    }
                }
            }
        }

        return ResponseEntity.status(404).body("Arquivo do horário não encontrado no sistema de arquivos.");
    }






}