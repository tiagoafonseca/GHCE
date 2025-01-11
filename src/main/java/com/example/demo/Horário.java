package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Horário {
    String date;
    String name;
    List<Aula> aulas= new ArrayList<>();
    Metricas qualidade;



    Horário(){}

    Horário(String name, List<Aula> aulas){
        this.date=new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.name = name;
        this.aulas=aulas;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Aula> getAulas() {
        return aulas;
    }

    public void setAulas(List<Aula> aulas) {
        this.aulas = aulas;
    }

    public Metricas getQualidade() {
        return qualidade;
    }

    public void setQualidade(Metricas qualidade) {
        this.qualidade = qualidade;
    }

    Horário(String name, List<Aula> aulas, String data){
        this.date=data;
        this.name = name;
        this.aulas=aulas;
    }

    public void loadMetricas(){

    }






    public String writeMySelf() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(this);
        System.out.println(jsonString);
        return jsonString;
    }


}
