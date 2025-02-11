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
// 1-> Aula em Sobrelotação DONE
// 2-> Sem sala Atribuida ou Sala n existe DONE
// 3-> Informação em Falta DONE
// 4-> Horário PL fora de PL
// 5-> Aula ao Sábado  DONE
// 6-> Sala desadequada

@RestController
@RequestMapping("/api1")
public class Metricas {
    static List<Sala> salas=new ArrayList<>();
    Map<Integer,List<Integer>> mapaErros = new HashMap<>(); //Primeiro é ID, depois é lista de falhas, 55 - > [1,3,5]
    public double horarioPontuacao = -1;
    int[]arrayOfErros =new int[4];


    public int[] getArrayOfErros() {
        return arrayOfErros;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public double getHorarioPontuacao(){
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
        List<Integer> i= determineAllTheStats(a);
        for (Sala s : salas) {
            if (s.getSala().equals(a.getSalaDeAulaFull())) {
                //Correr Validaçoes e Flagar aula
                //Ligar aula a sala
                i.remove(i.size()-1);
                this.mapaErros.put(a.getId(),i);
                return;
            }
        }
        //Sala sem correspondencia
        this.mapaErros.put(a.getId(),i);
    }

    public void printMap(){
        for(int i=0; i<mapaErros.size(); i++){
            System.out.println("Id:"+i+" "+mapaErros.get(i));
        }
    }


    public List<Integer> determineAllTheStats(Aula a){
        List<Integer> tempStats = new ArrayList<>();
        if(classInOverLotacion(a)){
            System.out.println(a.alunosNaAulaInInt() +" e "+  a.lotaçãoMaximaInInt());
            tempStats.add(1);
        }
       // if(semSalaOuSalaNaoExiste(a,s))
         //   tempStats.add(2);
        if(MissingInfo(a))
            tempStats.add(3);
        //if(HorarioPLForaDePL(a,s))
          //  tempStats.add(4);
        if(aulaAoSabado(a))
            tempStats.add(5);
        //if(salaDesequada(a,s))
          //  tempStats.add(6);

        tempStats.add(2);

        return tempStats;
    }
     //A FUNCIONAR
    public boolean aulaAoSabado(Aula aula){
        return aula.getDiaDaSemana()[0].equals("S�b");
    }

   // public boolean HorarioPLForaDePL(Aula aula, Sala sala){ //Lotacion ahahahahahha
     //   return sala.getCapacidadeSala() > aula.lotaçãoInInt();
    //}


    public void semSalaOuSalaNaoExiste(Aula aula, Sala sala){ //Lotacion ahahahahahha
        //this.mapaErros.put(aula.idAula);

    }

    public boolean MissingInfo(Aula aula){
        try {
            return aula.getDia()[0].isEmpty() || aula.getLotação()[0].isEmpty() || aula.getSalaDaAula()[0].isEmpty() ||
                    aula.getFim()[0].isEmpty() || aula.getDiaDaSemana()[0].isEmpty() || aula.getTurma()[0].isEmpty() ||
                    aula.getInicio()[0].isEmpty() || aula.getCaracteristicasReais()[0].isEmpty() ||
                    aula.getCaracteristicasDaSalaPedidaParaAula()[0].isEmpty() || aula.getUnidadeDeExecucao()[0].isEmpty() ||
                    aula.getTurno()[0].isEmpty() || aula.getInscritosNoTurno()[0].isEmpty() || aula.getCurso()[0].isEmpty();
        }catch (NullPointerException  | ArrayIndexOutOfBoundsException e){
            return true;
        }
    }


    //A FUNCIONAR
    public boolean classInOverLotacion(Aula aula){
        return aula.lotaçãoMaximaInInt() < aula.alunosNaAulaInInt();
    }



    public void calcularPontuacao() {
        System.out.println("Tamanho:"+mapaErros.keySet().size());
        for (Integer chave : mapaErros.keySet()) {
                System.out.println("Chave:"+chave);
                List<Integer> listaDeErros = mapaErros.get(chave);
                System.out.println("Chave 2:"+mapaErros.get(chave));
                if (listaDeErros.contains(1) && arrayOfErros[0] != 500) {
                    arrayOfErros[0]++;

                } else if (listaDeErros.contains(2) && arrayOfErros[1] != 500) {
                    arrayOfErros[1]++;

                } else if (listaDeErros.contains(3) && arrayOfErros[2] != 500) {
                    arrayOfErros[2]++;

                } else if (listaDeErros.contains(5) && arrayOfErros[3] != 500) {
                    arrayOfErros[3]++;

                }
                System.out.println(arrayOfErros[0] + " "+arrayOfErros[1] +" "+arrayOfErros[2] +" "+arrayOfErros[3]);
                double pontuacao=0.25*(100- (double) arrayOfErros[0] /5)+0.25*(100- (double) arrayOfErros[1] /5)
                        +0.25*(100- (double) arrayOfErros[2] /5)+0.25*(100- (double) arrayOfErros[3] /5);

                horarioPontuacao=pontuacao;
                System.out.println("Pontuação:"+horarioPontuacao+pontuacao);
                //return (int)pontuacao;
        }
    }
}
