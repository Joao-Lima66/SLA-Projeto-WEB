
package com.sistema.sla.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.sistema.sla.model.Sala;

@Repository
public class SalaDao {

    private final DataSource dataSource;

    public SalaDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Sala> listarTodos() {
        return listarTodasComOcupacao();
    }

    public List<Sala> listarTodasComOcupacao() {
        String sql = """
                SELECT s.id, s.numero, s.bloco, s.tipo, s.capacidade, s.status,
                       a.id AS alocacao_id, a.dia_semana, a.horario,
                       d.nome AS disciplina_nome,
                       u.nome AS usuario_nome, u.matricula AS usuario_matricula
                FROM salas s
                LEFT JOIN alocacoes a ON s.id = a.sala_id AND a.ativa = TRUE
                LEFT JOIN disciplinas d ON a.disciplina_id = d.id
                LEFT JOIN usuarios u ON a.usuario_id = u.id
                ORDER BY s.bloco ASC, s.numero ASC
                """;

        return executarConsultaLista(sql, null);
    }

    public List<Sala> listarSalasOcupadasComAlocacao() {
        String sql = """
                SELECT s.id, s.numero, s.bloco, s.tipo, s.capacidade, s.status,
                       a.id AS alocacao_id, a.dia_semana, a.horario,
                       d.nome AS disciplina_nome,
                       u.nome AS usuario_nome, u.matricula AS usuario_matricula
                FROM salas s
                INNER JOIN alocacoes a ON s.id = a.sala_id AND a.ativa = TRUE
                INNER JOIN disciplinas d ON a.disciplina_id = d.id
                INNER JOIN usuarios u ON a.usuario_id = u.id
                ORDER BY s.bloco ASC, s.numero ASC
                """;

        return executarConsultaLista(sql, null);
    }

    public List<Sala> listarPorStatus(String status) {
        String sql = """
                SELECT s.id, s.numero, s.bloco, s.tipo, s.capacidade, s.status,
                       a.id AS alocacao_id, a.dia_semana, a.horario,
                       d.nome AS disciplina_nome,
                       u.nome AS usuario_nome, u.matricula AS usuario_matricula
                FROM salas s
                LEFT JOIN alocacoes a ON s.id = a.sala_id AND a.ativa = TRUE
                LEFT JOIN disciplinas d ON a.disciplina_id = d.id
                LEFT JOIN usuarios u ON a.usuario_id = u.id
                WHERE s.status = ?
                ORDER BY s.bloco ASC, s.numero ASC
                """;

        return executarConsultaLista(sql, status);
    }

    public List<Sala> listarPorBloco(String bloco) {
        String sql = """
                SELECT s.id, s.numero, s.bloco, s.tipo, s.capacidade, s.status,
                       a.id AS alocacao_id, a.dia_semana, a.horario,
                       d.nome AS disciplina_nome,
                       u.nome AS usuario_nome, u.matricula AS usuario_matricula
                FROM salas s
                LEFT JOIN alocacoes a ON s.id = a.sala_id AND a.ativa = TRUE
                LEFT JOIN disciplinas d ON a.disciplina_id = d.id
                LEFT JOIN usuarios u ON a.usuario_id = u.id
                WHERE s.bloco = ?
                ORDER BY s.numero ASC
                """;

        return executarConsultaLista(sql, bloco);
    }

    public List<Sala> listarPorTipo(String tipo) {
        String sql = """
                SELECT s.id, s.numero, s.bloco, s.tipo, s.capacidade, s.status,
                       a.id AS alocacao_id, a.dia_semana, a.horario,
                       d.nome AS disciplina_nome,
                       u.nome AS usuario_nome, u.matricula AS usuario_matricula
                FROM salas s
                LEFT JOIN alocacoes a ON s.id = a.sala_id AND a.ativa = TRUE
                LEFT JOIN disciplinas d ON a.disciplina_id = d.id
                LEFT JOIN usuarios u ON a.usuario_id = u.id
                WHERE s.tipo = ?
                ORDER BY s.bloco ASC, s.numero ASC
                """;

        return executarConsultaLista(sql, tipo);
    }

    public Sala buscarPorId(Integer salaId) {
        if (salaId == null) {
            return null;
        }

        return buscarPorIdComOcupacao(salaId);
    }

    public Sala buscarPorIdComOcupacao(int salaId) {
        String sql = """
                SELECT s.id, s.numero, s.bloco, s.tipo, s.capacidade, s.status,
                       a.id AS alocacao_id, a.dia_semana, a.horario,
                       d.nome AS disciplina_nome,
                       u.nome AS usuario_nome, u.matricula AS usuario_matricula
                FROM salas s
                LEFT JOIN alocacoes a ON s.id = a.sala_id AND a.ativa = TRUE
                LEFT JOIN disciplinas d ON a.disciplina_id = d.id
                LEFT JOIN usuarios u ON a.usuario_id = u.id
                WHERE s.id = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, salaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearSalaComOcupacao(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar detalhes da sala: " + e.getMessage(), e);
        }

        return null;
    }

    public void inserir(Sala sala) {
        String sql = """
                INSERT INTO salas (numero, bloco, tipo, capacidade, status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sala.getNumero());
            ps.setString(2, sala.getBloco());
            ps.setString(3, sala.getTipo());
            ps.setInt(4, sala.getCapacidade());
            ps.setString(5,
                    sala.getStatus() != null ? sala.getStatus() : "DISPONIVEL");

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao cadastrar sala: " + e.getMessage(), e);
        }
    }

    public void atualizar(Sala sala) {
        String sql = """
                UPDATE salas
                SET numero = ?, bloco = ?, tipo = ?, capacidade = ?, status = ?
                WHERE id = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sala.getNumero());
            ps.setString(2, sala.getBloco());
            ps.setString(3, sala.getTipo());
            ps.setInt(4, sala.getCapacidade());
            ps.setString(5, sala.getStatus());
            ps.setInt(6, sala.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar sala: " + e.getMessage(), e);
        }
    }

    public void atualizarStatus(Integer salaId, String status) {
        String sql = "UPDATE salas SET status = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, salaId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar status da sala: " + e.getMessage(), e);
        }
    }

    public void deletar(Integer salaId) {
        String sqlLimparHistorico =
                "DELETE FROM alocacoes WHERE sala_id = ? AND ativa = FALSE";

        String sqlDeleteSala = "DELETE FROM salas WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {

            try (PreparedStatement psHist =
                         conn.prepareStatement(sqlLimparHistorico)) {

                psHist.setInt(1, salaId);
                psHist.executeUpdate();
            }

            try (PreparedStatement psSala =
                         conn.prepareStatement(sqlDeleteSala)) {

                psSala.setInt(1, salaId);
                psSala.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir sala: " + e.getMessage(), e);
        }
    }

    private List<Sala> executarConsultaLista(String sql, String parametro) {
        List<Sala> salas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parametro != null) {
                ps.setString(1, parametro);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    salas.add(mapearSalaComOcupacao(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao consultar salas: " + e.getMessage(), e);
        }

        return salas;
    }

    private Sala mapearSalaComOcupacao(ResultSet rs) throws SQLException {
        Sala sala = new Sala();

        sala.setId(rs.getInt("id"));
        sala.setNumero(rs.getString("numero"));
        sala.setBloco(rs.getString("bloco"));
        sala.setTipo(rs.getString("tipo"));
        sala.setCapacidade(rs.getInt("capacidade"));
        sala.setStatus(rs.getString("status"));

        int alocId = rs.getInt("alocacao_id");

        if (!rs.wasNull()) {
            sala.setAlocacaoId(alocId);
            sala.setDiaSemana(rs.getString("dia_semana"));
            sala.setHorario(rs.getString("horario"));
            sala.setDisciplinaNome(rs.getString("disciplina_nome"));
            sala.setUsuarioNome(rs.getString("usuario_nome"));
            sala.setUsuarioMatricula(rs.getString("usuario_matricula"));
        }

        return sala;
    }
}
