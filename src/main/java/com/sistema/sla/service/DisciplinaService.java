package com.sistema.sla.service;

import com.sistema.sla.dao.DisciplinaDao;
import com.sistema.sla.model.Disciplina;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {

    private final DisciplinaDao disciplinaDao;

    public DisciplinaService(DisciplinaDao disciplinaDao) {
        this.disciplinaDao = disciplinaDao;
    }

    public List<Disciplina> listarTodos() {
        return disciplinaDao.listarTodos();
    }

    public Disciplina buscarPorId(Integer id) {
        return disciplinaDao.buscarPorId(id);
    }

    public List<Disciplina> listarPorProfessor(Integer professorId) {
        return disciplinaDao.listarPorProfessor(professorId);
    }

    public void inserir(Disciplina disciplina) {
        disciplinaDao.inserir(disciplina);
    }

    public void atualizar(Disciplina disciplina) {
        disciplinaDao.atualizar(disciplina);
    }

    public void deletar(Integer id) {
        disciplinaDao.deletar(id);
    }
}
