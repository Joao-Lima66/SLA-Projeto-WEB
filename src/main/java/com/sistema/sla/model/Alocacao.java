package com.sistema.sla.model;

import java.io.Serializable;

public class Alocacao implements Serializable {

    private int id;
    private int salaId;
    private int disciplinaId;
    private int usuarioId;
    private String diaSemana;
    private String horario;
    private boolean ativa;

    // Campos obtidos via JOIN para exibição
    private String salaNumero;
    private String salaBloco;
    private String salaTipo;
    private String disciplinaNome;
    private String disciplinaCodigo;
    private String usuarioNome;

    public Alocacao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalaId() {
        return salaId;
    }

    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public String getSalaNumero() {
        return salaNumero;
    }

    public void setSalaNumero(String salaNumero) {
        this.salaNumero = salaNumero;
    }

    public String getSalaBloco() {
        return salaBloco;
    }

    public void setSalaBloco(String salaBloco) {
        this.salaBloco = salaBloco;
    }

    public String getSalaTipo() {
        return salaTipo;
    }

    public void setSalaTipo(String salaTipo) {
        this.salaTipo = salaTipo;
    }

    public String getDisciplinaNome() {
        return disciplinaNome;
    }

    public void setDisciplinaNome(String disciplinaNome) {
        this.disciplinaNome = disciplinaNome;
    }

    public String getDisciplinaCodigo() {
        return disciplinaCodigo;
    }

    public void setDisciplinaCodigo(String disciplinaCodigo) {
        this.disciplinaCodigo = disciplinaCodigo;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getSalaTipoFormatado() {
        if ("LABORATORIO".equalsIgnoreCase(salaTipo)) {
            return "Laboratório";
        }
        return "Sala Teórica";
    }
}