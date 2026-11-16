package com.sistema.sla.dao;

import com.sistema.sla.model.Perfil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PerfilDao {

    private final DataSource dataSource;

    public PerfilDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Perfil> listarTodos() {
        List<Perfil> perfis = new ArrayList<>();
        String sql = "SELECT id, nome FROM perfis ORDER BY id ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                perfis.add(mapearPerfil(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar perfis", e);
        }
        return perfis;
    }

    public Perfil buscarPorId(Integer id) {
        String sql = "SELECT id, nome FROM perfis WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPerfil(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar perfil por id", e);
        }
        return null;
    }

    public Perfil buscarPorNome(String nome) {
        String sql = "SELECT id, nome FROM perfis WHERE nome = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPerfil(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar perfil por nome", e);
        }
        return null;
    }

    public void inserir(Perfil perfil) {
        String sql = "INSERT INTO perfis (nome) VALUES (?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, perfil.getNome());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    perfil.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir perfil", e);
        }
    }

    public void atualizar(Perfil perfil) {
        String sql = "UPDATE perfis SET nome = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, perfil.getNome());
            ps.setInt(2, perfil.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar perfil", e);
        }
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM perfis WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar perfil", e);
        }
    }

    private Perfil mapearPerfil(ResultSet rs) throws SQLException {
        Perfil p = new Perfil();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        return p;
    }
}
