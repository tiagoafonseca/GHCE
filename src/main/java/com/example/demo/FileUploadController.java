package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FileUploadController {

   static File ficheiro;
   ArrayList<File> files=new ArrayList<>();
    private int horarioPontuacao = -1; // Variável para armazenar a pontuação do horário

    @PostConstruct
    public void runProcessCaracterizacaoSalas() {
        processCaracterizacaoSalas();
    }




    @PostMapping("/upload")
    public ResponseEntity<String> readFileAndCreateJsons(@RequestParam("file") MultipartFile file) throws IOException {
        List<Linha> linhasObj = new ArrayList<>();
        List<String> linhas = new ArrayList<>();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        File myObj = new File("./json.txt");
        FileWriter myWriter = new FileWriter("json.txt");

        myWriter.append("[\n");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        }
        for (int i = 0; i < linhas.size(); i++) {
            System.out.println("Linha: " + i);
            String json = ow.writeValueAsString(buildLinhaObj(linhas.get(i)));
            myWriter.append(json + ",\n");
            myWriter.flush();
        }
        System.out.println("LinhaFINAL: " + (linhas.size() - 1));
        String json = ow.writeValueAsString(buildLinhaObj(linhas.get(linhas.size() - 1)));
        this.ficheiro = myObj;
        myWriter.append(json + "\n]");
        myWriter.flush();
        myWriter.close();

        // 1. Converter JSON TXT para JSON
        ResponseEntity<String> conversionResponse = convertJsonTxtToJson();
        if (!conversionResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(conversionResponse.getStatusCode())
                    .body("Erro ao converter json.txt para .json: " + conversionResponse.getBody());
        }

        // 2. Avaliar sobrelotação
        ResponseEntity<String> overcrowdingResponse = evaluateOvercrowding();
        if (!overcrowdingResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(overcrowdingResponse.getStatusCode())
                    .body("Erro ao avaliar sobrelotação: " + overcrowdingResponse.getBody());
        }

        // 3. Informar que a operação foi concluída com sucesso
        return ResponseEntity.ok("Upload processado com sucesso. Conversão e avaliação realizadas.");
    }





    @GetMapping("/processCaracterizacao")
    public ResponseEntity<String> processCaracterizacaoSalas() {
        String filePath = "./salas.csv"; // Caminho do arquivo local
        List<Map<String, String>> caracterizacaoList = new ArrayList<>();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        File caracterizacaoFile = new File("./caracterizacao.json");

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



    @GetMapping("/evaluateOvercrowding")
    public ResponseEntity<String> evaluateOvercrowding() {
        File horarioFile = new File("./horario.json");
        File caracterizacaoFile = new File("./caracterizacao.json");

        if (!horarioFile.exists() || !caracterizacaoFile.exists()) {
            return ResponseEntity.status(404).body("Um ou ambos os arquivos JSON não foram encontrados.");
        }

        try {
            // Ler os arquivos JSON
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> horarioList = objectMapper.readValue(horarioFile, List.class);
            List<Map<String, Object>> caracterizacaoList = objectMapper.readValue(caracterizacaoFile, List.class);

            // Mapeamento de capacidade máxima por sala
            Map<String, Integer> capacidadeSalas = new HashMap<>();
            for (Map<String, Object> sala : caracterizacaoList) {
                String nomeSala = (String) sala.get("Nome sala");
                String capacidadeStr = (String) sala.get("Capacidade Normal");
                if (nomeSala != null && capacidadeStr != null) {
                    try {
                        capacidadeSalas.put(nomeSala, Integer.parseInt(capacidadeStr));
                    } catch (NumberFormatException e) {
                        System.err.println("Capacidade inválida para a sala: " + nomeSala);
                    }
                }
            }

            // Identificar aulas em sobrelotação
            int aulasSobrelotacao = 0;
            for (Map<String, Object> aula : horarioList) {
                List<String> salaDaAula = (List<String>) aula.get("salaDaAula");
                List<String> lotacaoStr = (List<String>) aula.get("lotação");

                if (salaDaAula != null && !salaDaAula.isEmpty() && lotacaoStr != null && !lotacaoStr.isEmpty()) {
                    String sala = salaDaAula.get(0);
                    try {
                        int lotacao = Integer.parseInt(lotacaoStr.get(0));
                        Integer capacidadeMaxima = capacidadeSalas.get(sala);

                        if (capacidadeMaxima != null && lotacao > capacidadeMaxima) {
                            aulasSobrelotacao++;
                            System.out.println("Sobrelotação detectada: Sala " + sala + " - Lotação " + lotacao + "/" + capacidadeMaxima);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Lotação inválida para a aula na sala: " + sala);
                    }
                }
            }

            // Cálculo da pontuação: 1 ponto perdido a cada 5 aulas em sobrelotação
            int pontuacao = 100 - (aulasSobrelotacao / 5);
            if (pontuacao < 0) pontuacao = 0; // Garantir que a pontuação não seja negativa

            // Atualizar a variável de classe
            horarioPontuacao = pontuacao;

            // Resultado
            String resultado = "Aulas em sobrelotação: " + aulasSobrelotacao + "\nPontuação do horário: " + pontuacao;
            System.out.println(resultado);
            return ResponseEntity.ok(resultado);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao processar os arquivos JSON: " + e.getMessage());
        }
    }


    @GetMapping("/horarioPontuacao")
    public ResponseEntity<Integer> getHorarioPontuacao() {
        if (horarioPontuacao == -1) {
            return ResponseEntity.status(404).body(-1); // Retorna -1 se a pontuação ainda não foi calculada
        }
        return ResponseEntity.ok(horarioPontuacao);
    }






    @GetMapping("/convertJsonTxtToJson")
    public ResponseEntity<String> convertJsonTxtToJson() {
        File sourceFile = new File("./json.txt");
        File targetFile = new File("./horario.json");

        if (!sourceFile.exists()) {
            return ResponseEntity.status(404).body("O arquivo json.txt não foi encontrado.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
             FileWriter writer = new FileWriter(targetFile)) {

            // Ler o conteúdo do arquivo .txt
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // Escrever o conteúdo no arquivo .json
            writer.write(content.toString());
            return ResponseEntity.ok("Arquivo convertido para horario.json com sucesso.");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao converter json.txt para .json: " + e.getMessage());
        }
    }






    @GetMapping("/json")
    public ResponseEntity<Resource> getJsonFile() {

        System.out.println("TOU AQUI CRLH"+"  "+ficheiro.exists());
        if (!ficheiro.exists()) {
            return ResponseEntity.notFound().build(); // Return 404 if file doesn't exist
        }

        Resource resource = new FileSystemResource(ficheiro);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=json.txt")
                .body(resource);
    }


    Linha buildLinhaObj(String linha) {
        String[] valoresRaw = linha.split(";");
        System.out.println(valoresRaw.length + "   "+ Arrays.toString(valoresRaw));
        Linha linhaNew = new Linha();
        try {
            if (valoresRaw.length == 13) {
                linhaNew.setCurso(valoresRaw[0]);
                linhaNew.setCaracteristicasReais(valoresRaw[12]);
                linhaNew.setInicio(valoresRaw[6]);
                linhaNew.setFim(valoresRaw[7]);
                linhaNew.setDiaDaSemana(valoresRaw[5]);
                linhaNew.setLotação(valoresRaw[11]);
                linhaNew.setSalaDaAula(valoresRaw[10]);
                linhaNew.setUnidadeDeExecucao(valoresRaw[1]);
                linhaNew.setTurno(valoresRaw[2]);
                linhaNew.setTurma(valoresRaw[3]);
                linhaNew.setInscritosNoTurno(valoresRaw[4]);
                linhaNew.setDia(valoresRaw[8]);
                linhaNew.setCaracteristicasDaSalaPedidaParaAula(valoresRaw[9]);
            } else if (valoresRaw.length == 10) {
                linhaNew.setCurso(valoresRaw[0]);
                linhaNew.setCaracteristicasReais(null);
                linhaNew.setInicio(valoresRaw[6]);
                linhaNew.setFim(valoresRaw[7]);
                linhaNew.setDiaDaSemana(valoresRaw[5]);
                linhaNew.setLotação(null);
                linhaNew.setSalaDaAula(null);
                linhaNew.setUnidadeDeExecucao(valoresRaw[1]);
                linhaNew.setTurno(valoresRaw[2]);
                linhaNew.setTurma(valoresRaw[3]);
                linhaNew.setInscritosNoTurno(valoresRaw[4]);
                linhaNew.setDia(valoresRaw[8]);
                linhaNew.setCaracteristicasDaSalaPedidaParaAula(valoresRaw[9]);
            } else {
                linhaNew.setCurso(valoresRaw[0]);
                linhaNew.setCaracteristicasReais(null);
                linhaNew.setInicio(valoresRaw[6]);
                linhaNew.setFim(valoresRaw[7]);
                linhaNew.setDiaDaSemana(valoresRaw[5]);
                linhaNew.setLotação(null);
                linhaNew.setSalaDaAula(null);
                linhaNew.setUnidadeDeExecucao(valoresRaw[1]);
                linhaNew.setTurno(valoresRaw[2]);
                linhaNew.setTurma(valoresRaw[3]);
                linhaNew.setInscritosNoTurno(valoresRaw[4]);
                linhaNew.setDia(null);
                linhaNew.setCaracteristicasDaSalaPedidaParaAula(null);
            }
        }catch (Exception e){
            return linhaNew;
        }
        System.out.println(linhaNew.toString());
        return linhaNew;

    }


}