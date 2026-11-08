package com.sistema.sla.model;

public class Alocacao {

    private int id;
    private int usuarioId;
    private int disciplinaId;
    private int salaId;
    private String horario;

    private Disciplina disciplina;
    private Sala sala;

    public Alocacao() {
    }

    public Alocacao(int id, int usuarioId, int disciplinaId,
                    int salaId, String horario) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.disciplinaId = disciplinaId;
        this.salaId = salaId;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public int getSalaId() {
        return salaId;
    }

    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
}
