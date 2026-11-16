package com.sistema.sla.service;

import com.sistema.sla.dao.UsuarioDao;
import com.sistema.sla.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioDao usuarioDao;

    public AutenticacaoService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Usuario validarLogin(String matricula, String senha) {
        if (matricula == null || matricula.trim().isEmpty()
                || senha == null || senha.trim().isEmpty()) {
            return null;
        }
        return usuarioDao.autenticar(matricula, senha);
    }
}
