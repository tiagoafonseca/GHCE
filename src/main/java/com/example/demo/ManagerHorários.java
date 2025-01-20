package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api2")
public class ManagerHorários {
    static List<Horário> horarios = new ArrayList<>();
    static Horário selectOne;

    public static List<Horário> getHorarios() {
        return horarios;
    }

    public static void setHorarios(List<Horário> horarios) {
        ManagerHorários.horarios = horarios;
    }

    @GetMapping("/recieveListOfHorários")
    public static ResponseEntity<Map<String, Object>> loadAllHorários() {
        horarios.clear();
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
                selectOne=horario;
                return ResponseEntity.ok(horario);

            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/editLine")
    public ResponseEntity<String> editLineAPI(@RequestParam("idAula") int idAula, @RequestParam("idParametro") int idParametroAlterar, @RequestParam("newInfo") String newInfo) throws IOException {
        System.out.println(1);
        Aula a;
        idAula=idAula+1;
        switch (idParametroAlterar) {
            case 1:
                a=getAulaWithID(idAula,selectOne);
                a.setCurso(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+1);
            case 2:
                a=getAulaWithID(idAula,selectOne);
                a.setUnidadeDeExecucao(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+2);
            case 3:
                a=getAulaWithID(idAula,selectOne);
                a.setTurno(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+3);

            case 4:
                a=getAulaWithID(idAula,selectOne);
                a.setTurma(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+4);

            case 5:
                a=getAulaWithID(idAula,selectOne);
                a.setInscritosNoTurno(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+5);
            case 6:
                a=getAulaWithID(idAula,selectOne);
                a.setDiaDaSemana(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+6);
            case 7:
                a=getAulaWithID(idAula,selectOne);
                a.setInicio(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+7);
            case 8:
                a=getAulaWithID(idAula,selectOne);
                a.setFim(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+8);
            case 9:
                a=getAulaWithID(idAula,selectOne);
                a.setDia(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+9);
            case 10:
                a=getAulaWithID(idAula,selectOne);
                a.setSalaDaAula(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+10);
            case 11:
                a=getAulaWithID(idAula,selectOne);
                a.setCaracteristicasDaSalaPedidaParaAula(newInfo);
                updateHorário(selectOne);
                return ResponseEntity.ok().body("Alterado com sucesso "+11);

        }
        return ResponseEntity.notFound().build();
    }

    public Aula getAulaWithID(int idAula, Horário h){
        for(Aula a : h.aulas){
            if(a.getId()==idAula){
                return a;
            }
        }
        System.out.println("ID INVALIDO ");
        return null;
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


    public void updateHorário(Horário h) throws IOException {
        File folder = new File("./Horários/");
        FileWriter myWriter;
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().equals(h.getName() + ".json")) {
                    if (file.delete()) {
                        File horario=new File("./Horários/"+h.getName()+".json");
                        myWriter= new FileWriter(horario);
                        myWriter.write(h.writeMySelf());
                        myWriter.flush();
                        myWriter.close();
                        System.out.println("Sucesso Horário atualizado");
                    } else {
                        System.out.println("Error a apagar o horário");
                    }
                }
            }
        }
    }

    public static List<Horário> getHorariosCarregados() {
        return horarios;
    }


}