package com.sistema.sla.service;

import com.sistema.sla.dao.AlocacaoDao;
import com.sistema.sla.model.Alocacao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlocacaoService {

    private final AlocacaoDao alocacaoDao;

    public AlocacaoService(AlocacaoDao alocacaoDao) {
        this.alocacaoDao = alocacaoDao;
    }

    public List<Alocacao> buscarPorUsuarioId(Integer usuarioId) {
        return alocacaoDao.buscarPorUsuarioId(usuarioId);
    }

    public List<Alocacao> buscarTodas() {
        return alocacaoDao.buscarTodas();
    }

    public List<Alocacao> buscarPorSalaId(Integer salaId) {
        return alocacaoDao.buscarPorSalaId(salaId);
    }

    public void reservarSala(Integer usuarioId, Integer disciplinaId, Integer salaId,
                             String diaSemana, String horario) {
        alocacaoDao.reservarSala(usuarioId, disciplinaId, salaId, diaSemana, horario);
    }

    public void liberarSala(Integer alocacaoId) {
        alocacaoDao.liberarSala(alocacaoId);
    }

    public void atualizar(Integer id, Integer usuarioId, Integer disciplinaId,
                          Integer salaId, String diaSemana, String horario) {
        alocacaoDao.atualizar(id, usuarioId, disciplinaId, salaId, diaSemana, horario);
    }

    public void deletar(Integer id) {
        alocacaoDao.deletar(id);
    }
}
