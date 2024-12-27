package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Horário {
    String date;
    String name;
    File listaHorarios;
    Metricas qualidade;

    Horário(String name){
        this.date=new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.name = name;
        listaHorarios=new File("jsonHorarios/"+this.name+".json");
    }

    Horário(String location, String name,String date){
        this.date=date;
        this.name = name;
        listaHorarios=new File("ListaHorarios.json");
    }

    public void loadMetricas(){



    }






    public void writeMySelf() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(listaHorarios, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String jsonString = mapper.writeValueAsString(this);
        System.out.println(jsonString);
    }


}
