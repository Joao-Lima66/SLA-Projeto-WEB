package com.sistema.sla.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.sistema.sla.model.Disciplina;

@Repository
public class DisciplinaDao {

    private final DataSource dataSource;

    public DisciplinaDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Disciplina> listarTodos() {
        return listarTodas();
    }

    public List<Disciplina> listarTodas() {
        String sql = """
                SELECT d.id, d.nome, d.codigo, d.professor_id
                FROM disciplinas d
                LEFT JOIN usuarios u ON d.professor_id = u.id
                ORDER BY d.nome ASC
                """;
        return executarConsulta(sql, null);
    }

    public List<Disciplina> listarPorProfessor(Integer professorId) {
        String sql = """
                SELECT d.id, d.nome, d.codigo, d.professor_id
                FROM disciplinas d
                INNER JOIN usuarios u ON d.professor_id = u.id
                WHERE d.professor_id = ?
                ORDER BY d.nome ASC
                """;
        return executarConsulta(sql, professorId);
    }

    public Disciplina buscarPorId(Integer id) {
        if (id == null) {
            return null;
        }
        String sql = "SELECT id, nome, codigo, professor_id FROM disciplinas WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDisciplina(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar disciplina: " + e.getMessage(), e);
        }
        return null;
    }

    public void inserir(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas (nome, codigo, professor_id) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, disciplina.getNome());
            ps.setString(2, disciplina.getCodigo());
            ps.setObject(3, disciplina.getProfessorId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir disciplina: " + e.getMessage(), e);
        }
    }

    public void atualizar(Disciplina disciplina) {
        String sql = "UPDATE disciplinas SET nome = ?, codigo = ?, professor_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, disciplina.getNome());
            ps.setString(2, disciplina.getCodigo());
            ps.setObject(3, disciplina.getProfessorId());
            ps.setObject(4, disciplina.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar disciplina: " + e.getMessage(), e);
        }
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM disciplinas WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar disciplina: " + e.getMessage(), e);
        }
    }

    private List<Disciplina> executarConsulta(String sql, Integer parametro) {
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parametro != null) {
                ps.setInt(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    disciplinas.add(mapearDisciplina(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar disciplinas: " + e.getMessage(), e);
        }
        return disciplinas;
    }

    private Disciplina mapearDisciplina(ResultSet rs) throws SQLException {
        Disciplina d = new Disciplina();
        d.setId(rs.getInt("id"));
        d.setNome(rs.getString("nome"));
        d.setCodigo(rs.getString("codigo"));
        d.setProfessorId(rs.getInt("professor_id"));
        return d;
    }
}