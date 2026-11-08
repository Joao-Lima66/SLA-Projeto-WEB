package com.sistema.sla.dao;

import com.sistema.sla.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Usuario buscarPorEmailESenha(String email, String senha) {
        // Consulta SQL utilizando JOIN para buscar o nome do perfil (ALUNO ou PROFESSOR)
        String sql = "SELECT u.id, u.nome, u.email, u.senha, p.nome AS perfil_nome " +
                     "FROM usuario u " +
                     "JOIN perfil p ON u.perfil_id = p.id " +
                     "WHERE u.email = ? AND u.senha = ?";

        try {
            // Mapeamento manual do ResultSet (Padrão DAO puro)
            return jdbcTemplate.queryForObject(sql, new Object[]{email, senha}, (rs, rowNum) -> {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil_nome")); 
                return usuario;
            });
        } catch (EmptyResultDataAccessException e) {
            // Tratamento de exceção SQL caso o login esteja incorreto e não retorne resultados
            return null; 
        }
    }
}