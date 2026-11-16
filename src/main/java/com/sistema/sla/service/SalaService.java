package com.sistema.sla.service;

import com.sistema.sla.dao.SalaDao;
import com.sistema.sla.model.Sala;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaService {

    private final SalaDao salaDao;

    public SalaService(SalaDao salaDao) {
        this.salaDao = salaDao;
    }

    public List<Sala> listarTodos() {
        return salaDao.listarTodos();
    }

    public Sala buscarPorId(Integer id) {
        return salaDao.buscarPorId(id);
    }

    public List<Sala> listarPorBloco(String bloco) {
        return salaDao.listarPorBloco(bloco);
    }

    public List<Sala> listarPorTipo(String tipo) {
        return salaDao.listarPorTipo(tipo);
    }

    public List<Sala> listarPorStatus(String status) {
        return salaDao.listarPorStatus(status);
    }

    public List<Sala> listarSalasOcupadasComAlocacao() {
        return salaDao.listarSalasOcupadasComAlocacao();
    }

    public void inserir(Sala sala) {
        salaDao.inserir(sala);
    }

    public void atualizar(Sala sala) {
        salaDao.atualizar(sala);
    }

    public void atualizarStatus(Integer id, String status) {
        salaDao.atualizarStatus(id, status);
    }

    public void deletar(Integer id) {
        salaDao.deletar(id);
    }
}
