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

//Mapa de Metricas
// 1-> Aula em Sobrelotação
// 2-> Sem sala Atribuida ou Sala n existe
// 3-> Informação em Falta
// 4-> Horário PL fora de PL
// 5-> Aula ao Sábado
// 6-> Sala desadequada

@RestController
@RequestMapping("/api1")
public class Metricas {
    static List<Sala> salas=new ArrayList<>();
    static List<Aula> aulas;
    Map<Integer,List<Integer>> mapaErros = new HashMap<>(); //Primeiro é ID, depois é lista de falhas, 55 - > [1,3,5]
    public int horarioPontuacao = -1;


    public List<Sala> getSalas() {
        return salas;
    }

    public int getHorarioPontuacao(){
        return horarioPontuacao;
    }

    public static void setSalas(List<Sala> salas) {
        Metricas.salas = salas;
    }

    public void setHorarioPontuacao(int horarioPontuacao) {
        this.horarioPontuacao = horarioPontuacao;
    }

    public void setMapaErros(Map<Integer,List<Integer>> mapaErros) {
        this.mapaErros = mapaErros;
    }

    public Map<Integer,List<Integer>> getMapaErros() {
        return mapaErros;
    }

    Metricas() throws IOException {
        Metricas.setSalas(Sala.lerFicheiro("./caracterizacao.json"));
    }


    public void tinderMatch(Aula a) {
        if(a.getSalaDaAula()==null){
            return;
        }
        for (Sala s : salas) {
            if (s.getSala().equals(a.getSalaDeAulaFull())) {
                determineAllTheStats(a,s);

                //Correr Validaçoes e Flagar aula
                //Ligar aula a sala
                System.out.println("It's a Match!");
                return;
            }
        }
        System.out.println("It's not a Match :((( " +  a.getSalaDeAulaFull());

    }



    public void determineAllTheStats(Aula a,Sala s){
        List<Integer> tempStats = new ArrayList<>();
        int idAula= a.getId();
        if(classInOverLotacion(a,s))
            tempStats.add(1);
       // if(semSalaOuSalaNaoExiste(a,s))
         //   tempStats.add(2);
        //if(MissingInfo(a,s))
          //  tempStats.add(3);
        //if(HorarioPLForaDePL(a,s))
          //  tempStats.add(4);
        //if(aulaAoSabado(a,s))
          //  tempStats.add(5);
        //if(salaDesequada(a,s))
          //  tempStats.add(6);
        this.mapaErros.put(idAula,tempStats);

    }

    public boolean aulaAoSabado(Aula aula, Sala sala){
        return sala.getCapacidadeSala() > aula.LotaçãoInInt();
    }

    public boolean HorarioPLForaDePL(Aula aula, Sala sala){ //Lotacion ahahahahahha
        return sala.getCapacidadeSala() > aula.LotaçãoInInt();
    }

    public boolean semSalaOuSalaNaoExiste(Aula aula, Sala sala){ //Lotacion ahahahahahha
        return sala.getCapacidadeSala() > aula.LotaçãoInInt();
    }

    public boolean MissingInfo(Aula aula, Sala sala){
        return sala.getCapacidadeSala() > aula.LotaçãoInInt();
    }

    public boolean classInOverLotacion(Aula aula, Sala sala){
        return sala.getCapacidadeSala() > aula.LotaçãoInInt();
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




}
