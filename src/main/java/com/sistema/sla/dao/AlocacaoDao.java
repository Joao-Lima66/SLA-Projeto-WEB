
package com.sistema.sla.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.sistema.sla.model.Alocacao;

@Repository
public class AlocacaoDao {

    private final DataSource dataSource;

    public AlocacaoDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Alocacao> buscarTodas() {
        String sql = """
                SELECT a.id, a.sala_id, a.disciplina_id, a.usuario_id,
                       a.dia_semana, a.horario, a.ativa,
                       s.numero AS sala_numero, s.bloco AS sala_bloco, s.tipo AS sala_tipo,
                       d.nome AS disciplina_nome, d.codigo AS disciplina_codigo,
                       u.nome AS usuario_nome
                FROM alocacoes a
                INNER JOIN salas s ON a.sala_id = s.id
                INNER JOIN disciplinas d ON a.disciplina_id = d.id
                INNER JOIN usuarios u ON a.usuario_id = u.id
                WHERE a.ativa = TRUE
                ORDER BY a.id DESC
                """;
        return executarConsulta(sql, null);
    }

    public List<Alocacao> buscarPorUsuarioId(int usuarioId) {
        return listarPorUsuarioId(usuarioId);
    }

    public List<Alocacao> listarPorUsuarioId(int usuarioId) {
        String sql = """
                SELECT a.id, a.sala_id, a.disciplina_id, a.usuario_id,
                       a.dia_semana, a.horario, a.ativa,
                       s.numero AS sala_numero, s.bloco AS sala_bloco, s.tipo AS sala_tipo,
                       d.nome AS disciplina_nome, d.codigo AS disciplina_codigo,
                       u.nome AS usuario_nome
                FROM alocacoes a
                INNER JOIN salas s ON a.sala_id = s.id
                INNER JOIN disciplinas d ON a.disciplina_id = d.id
                INNER JOIN usuarios u ON a.usuario_id = u.id
                WHERE a.usuario_id = ? AND a.ativa = TRUE
                ORDER BY a.id DESC
                """;
        return executarConsulta(sql, usuarioId);
    }

    public List<Alocacao> buscarPorSalaId(Integer salaId) {
        String sql = """
                SELECT a.id, a.sala_id, a.disciplina_id, a.usuario_id,
                       a.dia_semana, a.horario, a.ativa,
                       s.numero AS sala_numero, s.bloco AS sala_bloco, s.tipo AS sala_tipo,
                       d.nome AS disciplina_nome, d.codigo AS disciplina_codigo,
                       u.nome AS usuario_nome
                FROM alocacoes a
                INNER JOIN salas s ON a.sala_id = s.id
                INNER JOIN disciplinas d ON a.disciplina_id = d.id
                INNER JOIN usuarios u ON a.usuario_id = u.id
                WHERE a.sala_id = ? AND a.ativa = TRUE
                ORDER BY a.id DESC
                """;
        return executarConsulta(sql, salaId);
    }

    public void reservarSala(int salaId, int disciplinaId, int usuarioId,
                             String diaSemana, String horario) {

        String sqlInsert = """
                INSERT INTO alocacoes
                (sala_id, disciplina_id, usuario_id, dia_semana, horario, ativa)
                VALUES (?, ?, ?, ?, ?, TRUE)
                """;

        String sqlUpdateSala =
                "UPDATE salas SET status = 'OCUPADA' WHERE id = ? AND status = 'DISPONIVEL'";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psSala = conn.prepareStatement(sqlUpdateSala);
                 PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {

                psSala.setInt(1, salaId);

                int atualizadas = psSala.executeUpdate();

                if (atualizadas == 0) {
                    conn.rollback();
                    throw new IllegalStateException(
                            "Esta sala já foi ocupada por outro usuário.");
                }

                psInsert.setInt(1, salaId);
                psInsert.setInt(2, disciplinaId);
                psInsert.setInt(3, usuarioId);
                psInsert.setString(4, diaSemana);
                psInsert.setString(5, horario);

                psInsert.executeUpdate();

                conn.commit();

            } catch (Exception ex) {
                conn.rollback();
                throw ex;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao reservar sala: " + e.getMessage(), e);
        }
    }

    public void atualizar(Integer id, Integer salaId, Integer disciplinaId,
                          Integer usuarioId, String diaSemana, String horario) {

        String sqlUpdate = """
                UPDATE alocacoes
                SET sala_id = ?, disciplina_id = ?, usuario_id = ?,
                    dia_semana = ?, horario = ?
                WHERE id = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {

            ps.setInt(1, salaId);
            ps.setInt(2, disciplinaId);
            ps.setInt(3, usuarioId);
            ps.setString(4, diaSemana);
            ps.setString(5, horario);
            ps.setInt(6, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar alocação: " + e.getMessage(), e);
        }
    }

    public void liberarSala(int alocacaoId) {

        String sqlBuscarSala =
                "SELECT sala_id FROM alocacoes WHERE id = ?";

        String sqlDesativar =
                "UPDATE alocacoes SET ativa = FALSE WHERE id = ?";

        String sqlLiberarSala =
                "UPDATE salas SET status = 'DISPONIVEL' WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try {
                Integer salaId = null;

                try (PreparedStatement psBusca =
                             conn.prepareStatement(sqlBuscarSala)) {

                    psBusca.setInt(1, alocacaoId);

                    try (ResultSet rs = psBusca.executeQuery()) {
                        if (rs.next()) {
                            salaId = rs.getInt("sala_id");
                        }
                    }
                }

                try (PreparedStatement psDesativa =
                             conn.prepareStatement(sqlDesativar)) {

                    psDesativa.setInt(1, alocacaoId);
                    psDesativa.executeUpdate();
                }

                if (salaId != null) {
                    try (PreparedStatement psLibera =
                                 conn.prepareStatement(sqlLiberarSala)) {

                        psLibera.setInt(1, salaId);
                        psLibera.executeUpdate();
                    }
                }

                conn.commit();

            } catch (Exception ex) {
                conn.rollback();
                throw ex;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao liberar sala: " + e.getMessage(), e);
        }
    }

    public void deletar(Integer alocacaoId) {

        if (alocacaoId == null) {
            return;
        }

        liberarSala(alocacaoId);

        String sqlDelete =
                "DELETE FROM alocacoes WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlDelete)) {

            ps.setInt(1, alocacaoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao deletar alocação: " + e.getMessage(), e);
        }
    }

    private List<Alocacao> executarConsulta(String sql, Integer parametro) {

        List<Alocacao> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parametro != null) {
                ps.setInt(1, parametro);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Alocacao a = new Alocacao();

                    a.setId(rs.getInt("id"));
                    a.setSalaId(rs.getInt("sala_id"));
                    a.setDisciplinaId(rs.getInt("disciplina_id"));
                    a.setUsuarioId(rs.getInt("usuario_id"));
                    a.setDiaSemana(rs.getString("dia_semana"));
                    a.setHorario(rs.getString("horario"));
                    a.setAtiva(rs.getBoolean("ativa"));

                    a.setSalaNumero(rs.getString("sala_numero"));
                    a.setSalaBloco(rs.getString("sala_bloco"));
                    a.setSalaTipo(rs.getString("sala_tipo"));

                    a.setDisciplinaNome(rs.getString("disciplina_nome"));
                    a.setDisciplinaCodigo(rs.getString("disciplina_codigo"));

                    a.setUsuarioNome(rs.getString("usuario_nome"));

                    lista.add(a);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao consultar alocações: " + e.getMessage(), e);
        }

        return lista;
    }
}
