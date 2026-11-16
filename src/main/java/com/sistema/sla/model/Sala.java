package com.sistema.sla.model;

import java.io.Serializable;

public class Sala implements Serializable {

    private int id;
    private String numero;
    private String bloco;
    private String tipo;
    private int capacidade;
    private String status;

    // Campos preenchidos via LEFT JOIN quando a sala está ocupada
    private Integer alocacaoId;
    private String disciplinaNome;
    private String usuarioNome;
    private String usuarioMatricula;
    private String diaSemana;
    private String horario;

    public Sala() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAlocacaoId() {
        return alocacaoId;
    }

    public void setAlocacaoId(Integer alocacaoId) {
        this.alocacaoId = alocacaoId;
    }

    public String getDisciplinaNome() {
        return disciplinaNome;
    }

    public void setDisciplinaNome(String disciplinaNome) {
        this.disciplinaNome = disciplinaNome;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioMatricula() {
        return usuarioMatricula;
    }

    public void setUsuarioMatricula(String usuarioMatricula) {
        this.usuarioMatricula = usuarioMatricula;
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

    public String getTipoFormatado() {
        if ("LABORATORIO".equalsIgnoreCase(tipo)) {
            return "Laboratório";
        }
        return "Sala Teórica";
    }
}