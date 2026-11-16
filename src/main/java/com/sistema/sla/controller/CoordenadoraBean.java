package com.sistema.sla.controller;

import com.sistema.sla.dao.AlocacaoDao;
import com.sistema.sla.dao.DisciplinaDao;
import com.sistema.sla.dao.PerfilDao;
import com.sistema.sla.dao.SalaDao;
import com.sistema.sla.dao.UsuarioDao;
import com.sistema.sla.model.Alocacao;
import com.sistema.sla.model.Disciplina;
import com.sistema.sla.model.Perfil;
import com.sistema.sla.model.Sala;
import com.sistema.sla.model.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class CoordenadoraBean implements Serializable {

    private Usuario coordenadoraLogada;
    private List<Alocacao> alocacoes;
    private List<Usuario> usuariosCadastrados;
    private List<Sala> salas;
    private List<Sala> salasDisponiveis;

    private String novoNome;
    private String novoEmail;
    private String novaSenha;
    private String novaMatricula;
    private Integer novoPerfilId;

    private Integer idAlocacaoSelecionada;
    private Integer usuarioId;
    private Integer disciplinaId;
    private Integer salaId;
    private String diaSemana;
    private String horario;

    private String filtroBusca;
    private String filtroBloco;
    private String filtroTipo;
    private String filtroStatus;

    private Integer salaIdReserva;

    private String novaSalaNumero;
    private String novaSalaBloco;
    private String novaSalaTipo;
    private Integer novaSalaCapacidade;

    @Autowired
    private AlocacaoDao alocacaoDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private SalaDao salaDao;

    @Autowired
    private DisciplinaDao disciplinaDao;

    @Autowired
    private PerfilDao perfilDao;

    @PostConstruct
    public void init() {
        coordenadoraLogada = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                           .getSessionMap().get("usuarioLogado");
        carregarDados();
    }

    public void carregarDados() {
        alocacoes = alocacaoDao.buscarTodas();
        usuariosCadastrados = usuarioDao.listarTodosUsuarios();
        salas = salaDao.listarTodos();
        salasDisponiveis = salaDao.listarPorStatus("DISPONIVEL");
    }

    public List<Sala> getSalasFiltradas() {
        List<Sala> resultado = new ArrayList<>(salas);

        if (filtroBusca != null && !filtroBusca.trim().isEmpty()) {
            String termo = filtroBusca.trim().toLowerCase();
            resultado = resultado.stream()
                .filter(s -> (s.getNumero() != null && s.getNumero().toLowerCase().contains(termo))
                    || (s.getBloco() != null && s.getBloco().toLowerCase().contains(termo))
                    || (s.getTipo() != null && s.getTipo().toLowerCase().contains(termo)))
                .collect(Collectors.toList());
        }

        if (filtroBloco != null && !filtroBloco.isEmpty()) {
            resultado = resultado.stream()
                .filter(s -> filtroBloco.equals(s.getBloco()))
                .collect(Collectors.toList());
        }

        if (filtroTipo != null && !filtroTipo.isEmpty()) {
            resultado = resultado.stream()
                .filter(s -> filtroTipo.equals(s.getTipo()))
                .collect(Collectors.toList());
        }

        if (filtroStatus != null && !filtroStatus.isEmpty()) {
            resultado = resultado.stream()
                .filter(s -> filtroStatus.equals(s.getStatus()))
                .collect(Collectors.toList());
        }

        return resultado;
    }

    public void limparFiltros() {
        this.filtroBusca = null;
        this.filtroBloco = null;
        this.filtroTipo = null;
        this.filtroStatus = null;
    }

    public void salvarUsuario() {
        try {
            if (usuarioDao.matriculaExiste(novaMatricula)) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Matrícula já cadastrada!"));
                return;
            }
            usuarioDao.inserirUsuario(novoNome, novaMatricula, novoEmail, novaSenha, novoPerfilId);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário cadastrado com sucesso!"));
            limparFormUsuario();
            carregarDados();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao cadastrar usuário."));
        }
    }

    public void excluirUsuario(Integer id) {
        try {
            usuarioDao.deletarUsuario(id);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário removido com sucesso!"));
            carregarDados();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não é possível excluir usuário com aulas ativas."));
        }
    }

    public void salvarAlocacao() {
        try {
            if (idAlocacaoSelecionada == null) {
               alocacaoDao.reservarSala(salaIdReserva, disciplinaId, usuarioId, diaSemana, horario);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sala reservada com sucesso!"));
            } else {
                alocacaoDao.atualizar(idAlocacaoSelecionada, usuarioId, disciplinaId, salaId, diaSemana, horario);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Alocação atualizada!"));
            }
            limparFormAlocacao();
            carregarDados();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao salvar alocação."));
        }
    }

    public void liberarSala(Integer alocacaoId) {
        try {
            alocacaoDao.liberarSala(alocacaoId);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sala liberada!"));
            carregarDados();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao liberar sala."));
        }
    }

    public void cadastrarSala() {
        try {
            Sala sala = new Sala();
            sala.setNumero(novaSalaNumero);
            sala.setBloco(novaSalaBloco);
            sala.setTipo(novaSalaTipo);
            sala.setCapacidade(novaSalaCapacidade);
            sala.setStatus("DISPONIVEL");
            salaDao.inserir(sala);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sala cadastrada com sucesso!"));
            limparFormNovaSala();
            carregarDados();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao cadastrar sala."));
        }
    }
public void excluirSala(Integer salaId) {
    try {
        salaDao.deletar(salaId);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sala / Laboratório excluído com sucesso!"));
        carregarDados();
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível excluir: esta sala possui aulas ativas."));
    }
}
    public void limparFormUsuario() {
        this.novoNome = null; this.novoEmail = null; this.novaSenha = null;
        this.novaMatricula = null; this.novoPerfilId = null;
    }

    public void limparFormAlocacao() {
        this.idAlocacaoSelecionada = null; this.usuarioId = null; this.disciplinaId = null;
        this.salaId = null; this.diaSemana = null; this.horario = null;
    }

    public void limparFormNovaSala() {
        this.novaSalaNumero = null; this.novaSalaBloco = null;
        this.novaSalaTipo = null; this.novaSalaCapacidade = null;
    }

    public List<Perfil> getPerfis() {
        return perfilDao.listarTodos();
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinaDao.listarTodos();
    }

   public List<Usuario> getUsuariosParaAlocacao() {
    if (usuariosCadastrados == null) {
        return new ArrayList<>();
    }
    return usuariosCadastrados.stream()
            .filter(u -> u.getPerfil() != null 
                    && "PROFESSOR".equalsIgnoreCase(u.getPerfil().getNome()))
            .collect(Collectors.toList());
}

    public Usuario getCoordenadoraLogada() { return coordenadoraLogada; }
    public List<Alocacao> getAlocacoes() { return alocacoes; }
    public List<Usuario> getUsuariosCadastrados() { return usuariosCadastrados; }
    public List<Sala> getSalas() { return salas; }
    public List<Sala> getSalasDisponiveis() { return salasDisponiveis; }
    public String getNovoNome() { return novoNome; }
    public void setNovoNome(String novoNome) { this.novoNome = novoNome; }
    public String getNovoEmail() { return novoEmail; }
    public void setNovoEmail(String novoEmail) { this.novoEmail = novoEmail; }
    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
    public String getNovaMatricula() { return novaMatricula; }
    public void setNovaMatricula(String novaMatricula) { this.novaMatricula = novaMatricula; }
    public Integer getNovoPerfilId() { return novoPerfilId; }
    public void setNovoPerfilId(Integer novoPerfilId) { this.novoPerfilId = novoPerfilId; }
    public Integer getIdAlocacaoSelecionada() { return idAlocacaoSelecionada; }
    public void setIdAlocacaoSelecionada(Integer idAlocacaoSelecionada) { this.idAlocacaoSelecionada = idAlocacaoSelecionada; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public Integer getDisciplinaId() { return disciplinaId; }
    public void setDisciplinaId(Integer disciplinaId) { this.disciplinaId = disciplinaId; }
    public Integer getSalaId() { return salaId; }
    public void setSalaId(Integer salaId) { this.salaId = salaId; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public String getFiltroBusca() { return filtroBusca; }
    public void setFiltroBusca(String filtroBusca) { this.filtroBusca = filtroBusca; }
    public String getFiltroBloco() { return filtroBloco; }
    public void setFiltroBloco(String filtroBloco) { this.filtroBloco = filtroBloco; }
    public String getFiltroTipo() { return filtroTipo; }
    public void setFiltroTipo(String filtroTipo) { this.filtroTipo = filtroTipo; }
    public String getFiltroStatus() { return filtroStatus; }
    public void setFiltroStatus(String filtroStatus) { this.filtroStatus = filtroStatus; }
    public Integer getSalaIdReserva() { return salaIdReserva; }
    public void setSalaIdReserva(Integer salaIdReserva) { this.salaIdReserva = salaIdReserva; }
    public String getNovaSalaNumero() { return novaSalaNumero; }
    public void setNovaSalaNumero(String novaSalaNumero) { this.novaSalaNumero = novaSalaNumero; }
    public String getNovaSalaBloco() { return novaSalaBloco; }
    public void setNovaSalaBloco(String novaSalaBloco) { this.novaSalaBloco = novaSalaBloco; }
    public String getNovaSalaTipo() { return novaSalaTipo; }
    public void setNovaSalaTipo(String novaSalaTipo) { this.novaSalaTipo = novaSalaTipo; }
    public Integer getNovaSalaCapacidade() { return novaSalaCapacidade; }
    public void setNovaSalaCapacidade(Integer novaSalaCapacidade) { this.novaSalaCapacidade = novaSalaCapacidade; }
}
