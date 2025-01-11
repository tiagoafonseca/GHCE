package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Sala {
    int capacidadeSala;
    int capacidadeExame;
    String Sala;
    String Edificio;
    String[] info;
    Map<Date,Aula[]> mapaAulas=new HashMap<Date,Aula[]>();


    static List<Sala> lerFicheiro(String filePath) throws IOException {
        File salas = new File(filePath);
        return new JsonParser(salas).parseFileIntoList();
    }

    public int getCapacidadeSala() {
        return capacidadeSala;
    }

    public void setCapacidadeSala(int capacidadeSala) {
        this.capacidadeSala = capacidadeSala;
    }

    public int getCapacidadeExame() {
        return capacidadeExame;
    }

    public void setCapacidadeExame(int capacidadeExame) {
        this.capacidadeExame = capacidadeExame;
    }

    public String getSala() {
        return Sala;
    }

    public void setSala(String sala) {
        Sala = sala;
    }

    public String getEdificio() {
        return Edificio;
    }

    public void setEdificio(String edificio) {
        Edificio = edificio;
    }

    public String[] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    Sala(){

    }

    @Override
    public String toString() {
        return "SALA: \n"+
                "Capacidade Sala:" +getCapacidadeSala() +"\n" +
                "Capacidade Exame:" +getCapacidadeExame() +"\n" +
                "Nome Sala:" +getSala() +"\n" +
                "Nome Edifício:" +getEdificio() +"\n"+
                "Info Adicional Sala: " + Arrays.toString(getInfo())+"\n";
    }



    private static class JsonParser {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();


        JsonParser(File file) {
            this.file = file;
        }

        List<Sala> parseFileIntoList() throws IOException {
            List<Sala> salas = new ArrayList<Sala>();
            int i;
            ArrayList<String> info = new ArrayList<String>(){};
            Sala sala;

            List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {
            });
            for (Map<String, Object> obj : data) {
                i=0;
                info = new ArrayList<String>(){};
                sala = new Sala();
                for (Map.Entry<String, Object> entry : obj.entrySet()){
                    if(i==7)
                        sala.setEdificio(entry.getValue().toString());
                    else if(i==6)
                        sala.setCapacidadeSala(Integer.parseInt(entry.getValue().toString()));
                    else if(i==22)
                        sala.setSala(entry.getValue().toString());
                    else if(i==34)
                        sala.setCapacidadeExame(Integer.parseInt(entry.getValue().toString()));

                    else{
                        if(!(entry.getValue()==null) && i!=0){
                               info.add(entry.getKey().toString());
                        }
                    }
                    i++;
                }
                sala.setInfo(info.toArray(new String[0]));
                salas.add(sala);
            }
        return salas;
        }
    }
}
/*
        "Nº características" : "4", 0
         "Sala NEE" : null,
         "Anfiteatro aulas" : null, 2
         "Laboratório de Bases de Engenharia" : null,
         "Laboratório de Jornalismo" : null, 4
         "Sala Aulas Mestrado Plus" : "X",
         "Capacidade Normal" : "80", 6
         "Edifício" : "Ala Autónoma (ISCTE-IUL)",
         "Arq 9" : null, 8
         "Focus Group" : null,
         "Laboratório de Arquitectura de Computadores II" : null, 10
         "Laboratório de Redes de Computadores I" : null,
         "Horário sala visível portal público" : "X", 12
         "Laboratório de Arquitectura de Computadores I" : null,
         "Arq 1" : null, 14
         "Apoio técnico eventos" : null,
         "Arq 3" : null, 16
         "Laboratório de Telecomunicações" : null,
         "Sala Aulas Mestrado" : "X",
         "Arq 2" : null, 18
         "Arq 5" : null,
         "Arq 4" : null, 20
         "Nome sala" : "Auditório Afonso de Barros",
         "Arq 6" : null, 22
         "Sala Reunião" : null,
         "Laboratório de Informática" : null, 24
         "Sala Provas" : null,
         "Sala de Arquitectura" : null, 26
         "BYOD (Bring Your Own Device)" : null,
         "Átrio" : null, 28
         "videoconferencia" : null,
         "Laboratório de Electrónica" : null, 30
         "Laboratório de Redes de Computadores II" : null,
         "Sala de Aulas normal" : "X",
         "Capacidade Exame" : "39" 32 */