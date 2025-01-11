package com.example.demo;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Arrays;

public class Aula {
    int id;
    String[] curso;
    String[] unidadeDeExecucao;
    String[] turno;
    String[] turma;
    String[] inscritosNoTurno;
    String[] diaDaSemana;
    String[] inicio;
    String[] fim;
    String[] dia;
    String[] caracteristicasDaSalaPedidaParaAula;
    String[] salaDaAula;
    String[] lotação;
    String[] caracteristicasReais;

    Aula(String[] curso, String[] unidadeDeExecucao, String[] turno, String[] turma, String[] inscritosNoTurno, String[] diaDaSemana,
          String[] inicio, String[] fim, String[] dia, String[] caracteristicasDaSalaPedidaParaAula, String[] salaDaAula, String[] lotação, String[] caracteristicasReais) {

        this.curso = curso;
        this.unidadeDeExecucao = unidadeDeExecucao;
        this.turno = turno;
        this.turma = turma;
        this.inscritosNoTurno = inscritosNoTurno;
        this.diaDaSemana = diaDaSemana;
        this.inicio = inicio;
        this.fim = fim;
        this.dia = dia;
        this.caracteristicasReais = caracteristicasReais;
        this.caracteristicasDaSalaPedidaParaAula = caracteristicasDaSalaPedidaParaAula;
        this.salaDaAula = salaDaAula;
        this.lotação = lotação;
    }



    @JsonSetter("curso")
    void setCurso(String[] curso) {
        this.curso = curso;
    }

    @JsonSetter("unidadeDeExecucao")
    public void setUnidadeDeExecucao(String[] unidadeDeExecucao) {
        this.unidadeDeExecucao = unidadeDeExecucao;
    }

    @JsonSetter("turno")
    public void setTurno(String[] turno) {
        this.turno = turno;
    }

    @JsonSetter("turma")
    public void setTurma(String[] turma) {
        this.turma = turma;
    }

    @JsonSetter("inscritosNoTurno")
    public void setInscritosNoTurno(String[] inscritosNoTurno) {
        this.inscritosNoTurno = inscritosNoTurno;
    }

    @JsonSetter("diaDaSemana")
    public void setDiaDaSemana(String[] diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    @JsonSetter("inicio")
    public void setInicio(String[] inicio) {
        this.inicio = inicio;
    }

    @JsonSetter("fim")
    public void setFim(String[] fim) {
        this.fim = fim;
    }

    @JsonSetter("dia")
    public void setDia(String[] dia) {
        this.dia = dia;
    }

    @JsonSetter("caracteristicasDaSalaPedidaParaAula")
    public void setCaracteristicasDaSalaPedidaParaAula(String[] caracteristicasDaSalaPedidaParaAula) {
        this.caracteristicasDaSalaPedidaParaAula = caracteristicasDaSalaPedidaParaAula;
    }

    @JsonSetter("salaDaAula")
    public void setSalaDaAula(String[] salaDaAula) {
        this.salaDaAula = salaDaAula;
    }

    @JsonSetter("lotação")
    public void setLotação(String[] lotação) {
        this.lotação = lotação;
    }

    @JsonSetter("caracteristicasReais")
    public void setCaracteristicasReais(String[] caracteristicasReais) {
        this.caracteristicasReais = caracteristicasReais;
    }

    public Aula() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    String[] filtroArrayOrItem(String valorRaw){
        String dataFormatada[]=valorRaw.split("\\s+");
        return dataFormatada;
    }


    public String[] getCurso() {
        return curso;
    }

    public String[] getUnidadeDeExecucao() {
        return unidadeDeExecucao;
    }

    public String[] getTurno() {
        return turno;
    }

    public String[] getTurma() {
        return turma;
    }

    public String[] getInscritosNoTurno() {
        return inscritosNoTurno;
    }

    public String[] getDiaDaSemana() {
        return diaDaSemana;
    }

    public String[] getInicio() {
        return inicio;
    }

    public String[] getFim() {
        return fim;
    }

    public String[] getDia() {
        return dia;
    }

    public String[] getCaracteristicasDaSalaPedidaParaAula() {
        return caracteristicasDaSalaPedidaParaAula;
    }

    public String[] getSalaDaAula() {
        return salaDaAula;
    }

    public String[] getLotação() {
        return lotação;
    }

    public String[] getCaracteristicasReais() {
        return caracteristicasReais;
    }

    public void setCurso(String curso) {
        if(curso!=null)
        this.curso = filtroArrayOrItem(curso);
        else{
            this.curso = new String[]{};
        }


    }

    public void setUnidadeDeExecucao(String unidadeDeExecucao) {
        if(unidadeDeExecucao!=null)
            this.unidadeDeExecucao = filtroArrayOrItem(unidadeDeExecucao);
        else{
            this.unidadeDeExecucao = new String[]{};
        }
    }

    public void setTurno(String turno) {
        if(turno!=null)
            this.turno = filtroArrayOrItem(turno);
        else{
            this.turno = new String[]{};
        }
    }

    public void setTurma(String turma) {
        if(turma!=null)
            this.turma = filtroArrayOrItem(turma);
        else{
            this.turma = new String[]{};
        }
    }

    public void setInscritosNoTurno(String inscritosNoTurno) {
        if(inscritosNoTurno!=null)
            this.inscritosNoTurno = filtroArrayOrItem(inscritosNoTurno);
        else{
            this.inscritosNoTurno = new String[]{};
        }
    }

    public void setDiaDaSemana(String diaDaSemana) {
        if(diaDaSemana!=null)
            this.diaDaSemana = filtroArrayOrItem(diaDaSemana);
        else{
            this.diaDaSemana = new String[]{};
        }
    }

    public void setInicio(String inicio) {
        if(inicio!=null)
            this.inicio = filtroArrayOrItem(inicio);
        else{
            this.inicio = new String[]{};
        }
    }

    public void setFim(String fim) {
        if(fim!=null)
            this.fim = filtroArrayOrItem(fim);
        else{
            this.fim = new String[]{};
        }
    }

    public void setDia(String dia) {
        if(dia!=null)
            this.dia = filtroArrayOrItem(dia);
        else{
            this.dia = new String[]{};
        }
    }

    public void setCaracteristicasDaSalaPedidaParaAula(String caracteristicasDaSalaPedidaParaAula) {
        if(caracteristicasDaSalaPedidaParaAula!=null)
            this.caracteristicasDaSalaPedidaParaAula = filtroArrayOrItem(caracteristicasDaSalaPedidaParaAula);
        else{
            this.caracteristicasDaSalaPedidaParaAula = new String[]{};
        }}

    public void setSalaDaAula(String salaDaAula) {
        if(Character.isDigit(salaDaAula.charAt(0))){
            this.salaDaAula = filtroArrayOrItem("--SalaNãoAtribuída--");
            return;
        }
        if(salaDaAula!=null)
            this.salaDaAula = filtroArrayOrItem(salaDaAula);
        else{
            this.salaDaAula = new String[]{};
        }}

    public void setLotação(String lotação) {
        if(lotação!=null)
            this.lotação = filtroArrayOrItem(lotação);
        else{
            this.lotação = new String[]{};
        }}


    public String replaceVirgula(String s){
        return s.replace(","," ");
    }


    public void setCaracteristicasReais(String caracteristicasReais) {
        caracteristicasReais=caracteristicasReais.replace(",,,,,,,,,,,,,,,,,,,","");
        caracteristicasReais=caracteristicasReais.replace(",,,,,,,,,","");
        caracteristicasReais=caracteristicasReais.replace(",,","");
        caracteristicasReais=caracteristicasReais.replace(",,,,","");
        caracteristicasReais=caracteristicasReais.replace(","," ");
        caracteristicasReais=replaceVirgula(caracteristicasReais);
        if(caracteristicasReais!=null)
            this.caracteristicasReais = filtroArrayOrItem(caracteristicasReais);
        else{
            this.caracteristicasReais = new String[]{};
        }}

    @Override
    public String toString(){
        return "Linha criada: \n " +
                Arrays.toString(getCurso())+"\n"+
                Arrays.toString(getUnidadeDeExecucao())+"\n"+
                Arrays.toString(getTurno())+"\n"+
                Arrays.toString(getTurma())+"\n"+
                Arrays.toString(getInscritosNoTurno())+"\n"+
                Arrays.toString(getDiaDaSemana())+"\n"+
                Arrays.toString(getInicio())+"\n"+
                Arrays.toString(getFim())+"\n"+
                Arrays.toString(getDia())+"\n"+
                Arrays.toString(getCaracteristicasDaSalaPedidaParaAula())+"\n"+
                Arrays.toString(getSalaDaAula())+"\n"+
                Arrays.toString(getLotação())+"\n"+
                Arrays.toString(getCaracteristicasReais())+"\n";
    }

}