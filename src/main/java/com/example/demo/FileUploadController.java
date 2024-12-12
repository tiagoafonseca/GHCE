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
import java.util.*;

@RestController
@RequestMapping("/api")
public class FileUploadController {

   static File ficheiro;


    @PostMapping("/upload")
    public ResponseEntity readFileAndCreateJsons(@RequestParam("file") MultipartFile file) throws IOException {
        List<Linha> linhasObj=new ArrayList<>();
        List<String> linhas = new ArrayList<>();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        File myObj = new File("./json.txt");
        FileWriter myWriter = new FileWriter("json.txt");

        myWriter.append("[\n");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        }
        for(int i = 0; i<linhas.size(); i++) {
            System.out.println("Linha: "+i);
            String json = ow.writeValueAsString(buildLinhaObj(linhas.get(i)));
            myWriter.append(json+",\n");
            myWriter.flush();
        }
        System.out.println("LinhaFINAL: "+ (linhas.size()-1));
        String json = ow.writeValueAsString(buildLinhaObj(linhas.get(linhas.size()-1)));
        this.ficheiro=myObj;
        myWriter.append(json+"\n]");
        myWriter.flush();
        myWriter.close();

        return ResponseEntity.ok().build();
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