package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;


@RestController
@RequestMapping("/api1")
public class Metricas {
    String nomeFile;
    List<Sala> salas;
    public int horarioPontuacao = -1;
    public int aulasEmSobreLotação;
    public int aulasSemSala;


    public String getNomeFile() {
        return nomeFile;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public int getHorarioPontuacao(){
        return horarioPontuacao;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public void setHorarioPontuacao(int horarioPontuacao) {
        this.horarioPontuacao = horarioPontuacao;
    }

    public int getAulasEmSobreLotação() {
        return aulasEmSobreLotação;
    }

    public void setAulasEmSobreLotação(int aulasEmSobreLotação) {
        this.aulasEmSobreLotação = aulasEmSobreLotação;
    }

    public int getAulasSemSala() {
        return aulasSemSala;
    }

    public void setAulasSemSala(int aulasSemSala) {
        this.aulasSemSala = aulasSemSala;
    }

    Metricas(){
    }

    void setNomeFile(String s){
        nomeFile = "./Horários/"+s+".json";
    }


    //Refazer
    @GetMapping("/processCaracterizacao")
    public ResponseEntity<String> processCaracterizacaoSalas() throws IOException {
        File caracterizacaoFile = new File("./caracterizacao.json");
        if(caracterizacaoFile.exists()){
            salas=Sala.lerFicheiro("./caracterizacao.json");
            System.out.println("Foram carregadas: "+salas.size()+" salas");
        }


        String filePath = "./salas.csv"; // Caminho do arquivo local
        List<Map<String, String>> caracterizacaoList = new ArrayList<>();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();


        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             FileWriter writer = new FileWriter(caracterizacaoFile)) {

            String headerLine = br.readLine(); // Lê o cabeçalho
            if (headerLine == null) {
                return ResponseEntity.badRequest().body("O arquivo está vazio.");
            }
            String[] headers = headerLine.split(";");

            String line;
            int lineNumber = 1; // Para rastrear em qual linha estamos
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";", -1); // "-1" mantém colunas vazias no final
                Map<String, String> caracterizacao = new HashMap<>();

                // Mapeia os valores para os cabeçalhos
                for (int i = 0; i < headers.length; i++) {
                    String value = (i < values.length) ? values[i].trim() : null; // Preenche com null se faltar valor
                    caracterizacao.put(headers[i], value.isEmpty() ? null : value); // Tratamento para strings vazias
                }

                caracterizacaoList.add(caracterizacao);
                lineNumber++;
            }

            // Escreve os dados no arquivo JSON
            writer.write(ow.writeValueAsString(caracterizacaoList));
            return ResponseEntity.ok("Caracterização processada e salva em JSON.");

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404).body("Arquivo de caracterização não encontrado.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao processar o arquivo de caracterização: " + e.getMessage());
        }
    }

    private int countAulasSemSala(List<Map<String, Object>> horarioList, List<Map<String, Object>> caracterizacaoList) {
        Set<String> nomesDasSalas = new HashSet<>();

        // Coletar os nomes de salas existentes
        for (Map<String, Object> sala : caracterizacaoList) {
            String nomeSala = (String) sala.get("Nome sala");
            if (nomeSala != null) {
                nomesDasSalas.add(nomeSala.trim());
            }
        }

        // Contar aulas sem sala atribuída
        int aulasSemSala = 0;
        for (Map<String, Object> aula : horarioList) {
            List<String> salaDaAula = (List<String>) aula.get("salaDaAula");
            if (salaDaAula == null || salaDaAula.isEmpty() || !nomesDasSalas.contains(salaDaAula.get(0).trim())) {
                aulasSemSala++;
            }
        }

        System.out.println("Aulas sem sala atribuída: " + aulasSemSala);
        return aulasSemSala;
    }


    private int calcularPontuacao(int aulasSobrelotacao, int aulasSemSala) {
        int pontuacao = 100;

        // Perde 1 ponto a cada 185 aulas em sobrelotação
        pontuacao -= aulasSobrelotacao / 185;

        // Perde 1 ponto a cada 185 aulas sem sala atribuída
        pontuacao -= aulasSemSala / 185;

        // Garantir que a pontuaçã o não seja negativa
        return pontuacao;
    }




    @GetMapping("/evaluateOvercrowding")
    public ResponseEntity<Integer> evaluateOvercrowding(
            @RequestParam(defaultValue = "true") boolean overcrowding,
            @RequestParam(defaultValue = "true") boolean noRoom) {

        File horarioFile = new File(nomeFile);
        File caracterizacaoFile = new File("./caracterizacao.json");

        if (!horarioFile.exists() || !caracterizacaoFile.exists()) {
            return ResponseEntity.status(404).body(-1);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> horarioList = objectMapper.readValue(horarioFile, List.class);
            List<Map<String, Object>> caracterizacaoList = objectMapper.readValue(caracterizacaoFile, List.class);

            int aulasSobrelotacao = 0;
            int aulasSemSala = 0;

            // Calcular aulas em sobrelotação
            if (overcrowding) {
                Map<String, Integer> capacidadeSalas = new HashMap<>();
                for (Map<String, Object> sala : caracterizacaoList) {
                    String nomeSala = (String) sala.get("Nome sala");
                    String capacidadeStr = (String) sala.get("Capacidade Normal");
                    if (nomeSala != null && capacidadeStr != null) {
                        try {
                            capacidadeSalas.put(nomeSala, Integer.parseInt(capacidadeStr));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                for (Map<String, Object> aula : horarioList) {
                    List<String> salaDaAula = (List<String>) aula.get("salaDaAula");
                    List<String> lotacaoStr = (List<String>) aula.get("lotação");

                    if (salaDaAula != null && !salaDaAula.isEmpty() && lotacaoStr != null && !lotacaoStr.isEmpty()) {
                        String sala = salaDaAula.get(0);
                        try {
                            int lotacao = Integer.parseInt(lotacaoStr.get(0));
                            if (capacidadeSalas.containsKey(sala) && lotacao > capacidadeSalas.get(sala)) {
                                aulasSobrelotacao++;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            // Calcular aulas sem sala atribuída
            if (noRoom) {
                aulasSemSala = countAulasSemSala(horarioList, caracterizacaoList);
            }

            // Calcular pontuação
            horarioPontuacao = calcularPontuacao(aulasSobrelotacao, aulasSemSala);

            return ResponseEntity.ok(horarioPontuacao);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(-1);
        }
    }

}
