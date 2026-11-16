package com.sistema.sla.dao;

import com.sistema.sla.model.Perfil;
import com.sistema.sla.model.Usuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDao {

    private final DataSource dataSource;

    public UsuarioDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Usuario autenticar(String matricula, String senha) {
        String sql = "SELECT u.id, u.nome, u.matricula, u.email, u.senha, " +
                     "p.id AS perfil_id, p.nome AS perfil_nome " +
                     "FROM usuarios u " +
                     "INNER JOIN perfis p ON u.perfil_id = p.id " +
                     "WHERE u.matricula = ? AND u.senha = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuario", e);
        }
        return null;
    }

    public boolean matriculaExiste(String matricula) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE matricula = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar matricula duplicada", e);
        }
        return false;
    }

    public void inserirUsuario(String nome, String matricula, String email, String senha, Integer perfilId) {
        String sql = "INSERT INTO usuarios (nome, matricula, email, senha, perfil_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, matricula);
            ps.setString(3, email);
            ps.setString(4, senha);
            ps.setInt(5, perfilId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuario", e);
        }
    }

    public List<Usuario> listarTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, u.matricula, u.email, u.senha, " +
                     "p.id AS perfil_id, p.nome AS perfil_nome " +
                     "FROM usuarios u " +
                     "INNER JOIN perfis p ON u.perfil_id = p.id " +
                     "ORDER BY u.id ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuarios", e);
        }
        return usuarios;
    }

    public Usuario buscarPorId(Integer id) {
        String sql = "SELECT u.id, u.nome, u.matricula, u.email, u.senha, " +
                     "p.id AS perfil_id, p.nome AS perfil_nome " +
                     "FROM usuarios u " +
                     "INNER JOIN perfis p ON u.perfil_id = p.id " +
                     "WHERE u.id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuario por id", e);
        }
        return null;
    }

    public void atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, matricula = ?, email = ?, senha = ?, perfil_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getMatricula());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getSenha());
            ps.setInt(5, usuario.getPerfil().getId());
            ps.setInt(6, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuario", e);
        }
    }

    public void deletarUsuario(Integer id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuario", e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setMatricula(rs.getString("matricula"));
        u.setEmail(rs.getString("email"));
        u.setSenha(rs.getString("senha"));
        Perfil p = new Perfil();
        p.setId(rs.getInt("perfil_id"));
        p.setNome(rs.getString("perfil_nome"));
        u.setPerfil(p);
        u.setPerfilNome(rs.getString("perfil_nome"));
        return u;
    }
}
