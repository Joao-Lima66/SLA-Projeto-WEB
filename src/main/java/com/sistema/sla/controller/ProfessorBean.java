package com.sistema.sla.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.sistema.sla.dao.AlocacaoDao;
import com.sistema.sla.dao.SalaDao;
import com.sistema.sla.model.Alocacao;
import com.sistema.sla.model.Sala;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("professorBean")
@ViewScoped
public class ProfessorBean implements Serializable {

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SalaDao salaDao;

    @Inject
    private AlocacaoDao alocacaoDao;

    private List<Sala> todasSalas = new ArrayList<>();
    private List<Alocacao> minhasAlocacoes = new ArrayList<>();
    private List<String> blocosDisponiveis = new ArrayList<>();

    // Filtros de consulta
    private String filtroBusca = "";
    private String filtroBloco = "";
    private String filtroTipo = "";
    private String filtroStatus = "";

    // Modal de detalhes da ocupação
    private Sala salaDetalhes;

    @PostConstruct
    public void init() {
        carregarDados();
    }

    public void carregarDados() {
        todasSalas = salaDao.listarTodasComOcupacao();

        if (sessionBean != null && sessionBean.getUsuarioLogado() != null) {
            minhasAlocacoes = alocacaoDao.listarPorUsuarioId(sessionBean.getUsuarioLogado().getId());
        } else {
            minhasAlocacoes = new ArrayList<>();
        }

        Set<String> blocos = new TreeSet<>();
        for (Sala s : todasSalas) {
            if (s.getBloco() != null && !s.getBloco().isBlank()) {
                blocos.add(s.getBloco().trim());
            }
        }
        blocosDisponiveis = new ArrayList<>(blocos);
    }

    public List<Sala> getSalasFiltradas() {
        return todasSalas.stream()
                .filter(s -> {
                    if (filtroBusca != null && !filtroBusca.isBlank()) {
                        String termo = filtroBusca.toLowerCase().trim();
                        boolean bateNumero = s.getNumero() != null && s.getNumero().toLowerCase().contains(termo);
                        boolean bateBloco = s.getBloco() != null && s.getBloco().toLowerCase().contains(termo);
                        boolean bateDisciplina = s.getDisciplinaNome() != null && s.getDisciplinaNome().toLowerCase().contains(termo);
                        boolean bateUsuario = s.getUsuarioNome() != null && s.getUsuarioNome().toLowerCase().contains(termo);
                        if (!bateNumero && !bateBloco && !bateDisciplina && !bateUsuario) {
                            return false;
                        }
                    }
                    if (filtroBloco != null && !filtroBloco.isBlank()) {
                        if (s.getBloco() == null || !s.getBloco().equalsIgnoreCase(filtroBloco)) {
                            return false;
                        }
                    }
                    if (filtroTipo != null && !filtroTipo.isBlank()) {
                        if (s.getTipo() == null || !s.getTipo().equalsIgnoreCase(filtroTipo)) {
                            return false;
                        }
                    }
                    if (filtroStatus != null && !filtroStatus.isBlank()) {
                        if (s.getStatus() == null || !s.getStatus().equalsIgnoreCase(filtroStatus)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public void limparFiltros() {
        filtroBusca = "";
        filtroBloco = "";
        filtroTipo = "";
        filtroStatus = "";
    }

    public void verDetalhes(Sala sala) {
        Sala atualizada = salaDao.buscarPorIdComOcupacao(sala.getId());
        this.salaDetalhes = (atualizada != null) ? atualizada : sala;
    }

    // KPIs
    public int getTotalSalas() {
        return todasSalas.size();
    }

    public long getTotalDisponiveis() {
        return todasSalas.stream().filter(s -> "DISPONIVEL".equalsIgnoreCase(s.getStatus())).count();
    }

    public long getTotalOcupadas() {
        return todasSalas.stream().filter(s -> "OCUPADA".equalsIgnoreCase(s.getStatus())).count();
    }

    public int getTotalMinhasAlocacoes() {
        return minhasAlocacoes.size();
    }

    // Getters e Setters
    public List<Sala> getTodasSalas() {
        return todasSalas;
    }

    public List<Alocacao> getMinhasAlocacoes() {
        return minhasAlocacoes;
    }

    public List<String> getBlocosDisponiveis() {
        return blocosDisponiveis;
    }

    public String getFiltroBusca() {
        return filtroBusca;
    }

    public void setFiltroBusca(String filtroBusca) {
        this.filtroBusca = filtroBusca;
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

    public String getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(String filtroStatus) {
        this.filtroStatus = filtroStatus;
    }

    public Sala getSalaDetalhes() {
        return salaDetalhes;
    }

    public void setSalaDetalhes(Sala salaDetalhes) {
        this.salaDetalhes = salaDetalhes;
    }
}