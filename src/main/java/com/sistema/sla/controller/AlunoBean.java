package com.sistema.sla.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.sistema.sla.dao.AlocacaoDao;
import com.sistema.sla.model.Alocacao;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("alunoBean")
@ViewScoped
public class AlunoBean implements Serializable {

    @Inject
    private AlocacaoDao alocacaoDao;

    private List<Alocacao> aulasAtivas = new ArrayList<>();
    private List<String> blocosComAula = new ArrayList<>();

    // Filtros focados nas aulas
    private String filtroBusca = "";
    private String filtroDiaSemana = "";
    private String filtroBloco = "";
    private String filtroTipo = "";

    // Modal de detalhes da aula
    private Alocacao aulaDetalhes;

    @PostConstruct
    public void init() {
        aulasAtivas = alocacaoDao.buscarTodas();

        Set<String> blocos = new TreeSet<>();
        for (Alocacao a : aulasAtivas) {
            if (a.getSalaBloco() != null && !a.getSalaBloco().isBlank()) {
                blocos.add(a.getSalaBloco().trim());
            }
        }
        blocosComAula = new ArrayList<>(blocos);
    }

    public List<Alocacao> getAulasFiltradas() {
        return aulasAtivas.stream()
                .filter(a -> {
                    if (filtroBusca != null && !filtroBusca.isBlank()) {
                        String termo = filtroBusca.toLowerCase().trim();
                        boolean bateDisciplina = a.getDisciplinaNome() != null && a.getDisciplinaNome().toLowerCase().contains(termo);
                        boolean bateCodigo = a.getDisciplinaCodigo() != null && a.getDisciplinaCodigo().toLowerCase().contains(termo);
                        boolean bateProfessor = a.getUsuarioNome() != null && a.getUsuarioNome().toLowerCase().contains(termo);
                        boolean bateSala = a.getSalaNumero() != null && a.getSalaNumero().toLowerCase().contains(termo);
                        
                        if (!bateDisciplina && !bateCodigo && !bateProfessor && !bateSala) {
                            return false;
                        }
                    }
                    if (filtroDiaSemana != null && !filtroDiaSemana.isBlank()) {
                        if (a.getDiaSemana() == null || !a.getDiaSemana().equalsIgnoreCase(filtroDiaSemana)) {
                            return false;
                        }
                    }
                    if (filtroBloco != null && !filtroBloco.isBlank()) {
                        if (a.getSalaBloco() == null || !a.getSalaBloco().equalsIgnoreCase(filtroBloco)) {
                            return false;
                        }
                    }
                    if (filtroTipo != null && !filtroTipo.isBlank()) {
                        if (a.getSalaTipo() == null || !a.getSalaTipo().equalsIgnoreCase(filtroTipo)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public void limparFiltros() {
        filtroBusca = "";
        filtroDiaSemana = "";
        filtroBloco = "";
        filtroTipo = "";
    }

    public void verDetalhesAula(Alocacao alocacao) {
        this.aulaDetalhes = alocacao;
    }

    // KPIs focados em aulas
    public int getTotalAulasAtivas() {
        return aulasAtivas.size();
    }

    public long getTotalAulasTeoricas() {
        return aulasAtivas.stream()
                .filter(a -> "TEORICA".equalsIgnoreCase(a.getSalaTipo()))
                .count();
    }

    public long getTotalAulasLaboratorio() {
        return aulasAtivas.stream()
                .filter(a -> "LABORATORIO".equalsIgnoreCase(a.getSalaTipo()))
                .count();
    }

    // Getters e Setters
    public List<Alocacao> getAulasAtivas() {
        return aulasAtivas;
    }

    public List<String> getBlocosComAula() {
        return blocosComAula;
    }

    public String getFiltroBusca() {
        return filtroBusca;
    }

    public void setFiltroBusca(String filtroBusca) {
        this.filtroBusca = filtroBusca;
    }

    public String getFiltroDiaSemana() {
        return filtroDiaSemana;
    }

    public void setFiltroDiaSemana(String filtroDiaSemana) {
        this.filtroDiaSemana = filtroDiaSemana;
    }

    public String getFiltroBloco() {
        return filtroBloco;
    }

    public void setFiltroBloco(String filtroBloco) {
        this.filtroBloco = filtroBloco;
    }

    public String getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(String filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public Alocacao getAulaDetalhes() {
        return aulaDetalhes;
    }

    public void setAulaDetalhes(Alocacao aulaDetalhes) {
        this.aulaDetalhes = aulaDetalhes;
    }
}