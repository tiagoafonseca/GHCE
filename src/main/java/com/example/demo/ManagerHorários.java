package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManagerHorários {
    static List<Horário> horarios = new ArrayList<>();





    public static void loadAllHorários() {
        ObjectMapper objectMapper = new ObjectMapper();
        File folder = new File("./Horários/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("Horário: " + listOfFiles[i].getName());
                }
            }
        }
        for( File file : listOfFiles ) {
            try {
                Horário h = objectMapper.readValue(file, Horário.class);
                horarios.add(h);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Tamanho da minha pica: "+horarios.size()+"km");

    }
}