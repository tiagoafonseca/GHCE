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
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UploadNewHorário {

    Horário h;
    File horárioFile;

    @PostMapping("/upload")
    public ResponseEntity<String> readFileAndCreateJsons(@RequestParam("file") MultipartFile file, @RequestParam("nome") String name) throws IOException {
        h=new Horário();
        List<Aula> linhasObj = new ArrayList<>();
        List<String> linhas = new LinkedList<>();
        Aula aulaObj;
        Metricas metricas = new Metricas();


        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String linha;
            int tamanho=0;

            while ((linha = br.readLine()) != null) {
                tamanho++;
                if(tamanho!=1)
                linhas.add(linha);

            }

            for (int i = 0; i < tamanho-2; i++) {
                aulaObj=buildAulaObj(linhas.get(i));
                aulaObj.setId(i);

                linhasObj.add(aulaObj);
            }


            aulaObj=buildAulaObj(linhas.get(tamanho-2));
            aulaObj.setId(tamanho-1);
            h=new Horário(name,linhasObj);

        }
        return ResponseEntity.ok("fixe");
    }



    @GetMapping("/json")
    public ResponseEntity<Resource> getJsonFile() throws IOException {
        File horario=new File("./Horários/"+h.getName()+".json");
        FileWriter myWriter = new FileWriter(horario);
        Metricas metricas = new Metricas();
        for(Aula a : h.getAulas()){
            metricas.tinderMatch(a);
        }
        metricas.printMap();
        metricas.setHorarioPontuacao(20);
        h.setQualidade(metricas);
        myWriter.write(h.writeMySelf());
        myWriter.flush();
        myWriter.close();
        ManagerHorários.getHorarios().add(h);
        ManagerHorários.selectOne=h;
        if (!horario.exists()) {
            return ResponseEntity.notFound().build(); // Return 404 if file doesn't exist
        }
        Resource resource = new FileSystemResource(horario);

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
        Aula buildAulaObj(String linha) {
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
                linhaNew.setCaracteristicasReais("");
                linhaNew.setInicio(valoresRaw[6]);
                linhaNew.setFim(valoresRaw[7]);
                linhaNew.setDiaDaSemana(valoresRaw[5]);
                linhaNew.setLotação("");
                linhaNew.setSalaDaAula("");
                linhaNew.setUnidadeDeExecucao(valoresRaw[1]);
                linhaNew.setTurno(valoresRaw[2]);
                linhaNew.setTurma(valoresRaw[3]);
                linhaNew.setInscritosNoTurno(valoresRaw[4]);
                linhaNew.setDia(valoresRaw[8]);
                linhaNew.setCaracteristicasDaSalaPedidaParaAula(valoresRaw[9]);
            } else {
                linhaNew.setCurso(valoresRaw[0]);
                linhaNew.setCaracteristicasReais("");
                linhaNew.setInicio(valoresRaw[6]);
                linhaNew.setFim(valoresRaw[7]);
                linhaNew.setDiaDaSemana(valoresRaw[5]);
                linhaNew.setLotação("");
                linhaNew.setSalaDaAula("");
                linhaNew.setUnidadeDeExecucao(valoresRaw[1]);
                linhaNew.setTurno(valoresRaw[2]);
                linhaNew.setTurma(valoresRaw[3]);
                linhaNew.setInscritosNoTurno(valoresRaw[4]);
                linhaNew.setDia("");
                linhaNew.setCaracteristicasDaSalaPedidaParaAula("");
            }
        }catch (Exception e){
            return linhaNew;
        }

        return linhaNew;

    }


}


