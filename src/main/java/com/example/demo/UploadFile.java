package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UploadFile {

    @PostMapping("/upload")
    public ResponseEntity<String> readFileAndCreateJsons(@RequestParam("file") MultipartFile file, @RequestParam("nome") String name) throws IOException {
        System.out.println("NOME:"+name);
        List<Aula> linhasObj = new ArrayList<>();
        List<String> linhas = new ArrayList<>();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        File myObj = new File("./aulas.json");
        FileWriter myWriter = new FileWriter("aulas.json");

        myWriter.append("[\n");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        }

        for (int i = 0; i < linhas.size(); i++) {
            String json = ow.writeValueAsString(buildLinhaObj(linhas.get(i)));
            myWriter.append(json + ",\n");
            myWriter.flush();
        }

        String json = ow.writeValueAsString(buildLinhaObj(linhas.get(linhas.size() - 1)));
        myWriter.append(json + "\n]");
        myWriter.flush();
        myWriter.close();

        Metricas metricas=new Metricas();

        ResponseEntity<Integer> overcrowdingResponse = metricas.evaluateOvercrowding(true, true);

        if (!overcrowdingResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(overcrowdingResponse.getStatusCode())
                    .body("Erro ao avaliar sobrelotação.");
        }
        // Atualizar pontuação do horário

        int pontuacao = overcrowdingResponse.getBody();
        System.out.println("Pontuação calculada: " + pontuacao);



        return ResponseEntity.ok(String.valueOf(pontuacao));
    }


    @GetMapping("/json")
    public ResponseEntity<Resource> getJsonFile() {
        File aulas=new File("aulas.json");

        if (!aulas.exists()) {
            return ResponseEntity.notFound().build(); // Return 404 if file doesn't exist
        }
        Resource resource = new FileSystemResource(aulas);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=aulas.json")
                .body(resource);
    }


    @GetMapping("/qualidade")
    public ResponseEntity<Resource> getQualidade(String nomeHorário) {
        return null;
    }





        //refazer
        Aula buildLinhaObj(String linha) {
        String[] valoresRaw = linha.split(";");
        String[] valores= new String[valoresRaw.length];
        Aula linhaNew = new Aula();
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

        return linhaNew;

    }


}


