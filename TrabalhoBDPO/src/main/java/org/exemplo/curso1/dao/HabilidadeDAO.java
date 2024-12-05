package org.exemplo.curso1.dao;

import org.exemplo.curso1.model.Habilidades;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HabilidadeDAO implements IHabilidade {

    private static final Logger LOGGER = Logger.getLogger(HabilidadeDAO.class.getName());

    private static final String SQL_INSERT = "INSERT INTO habilidades (nome_habilidade, dano, custo_mana) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE habilidades SET nome_habilidade=?, custo_mana=?, dano=? WHERE nome_habilidade=?";
    private static final String SQL_DELETE = "DELETE FROM habilidades WHERE nome_habilidade=?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM habilidades";


    private Connection criarConexao() throws SQLException {
        return Conexao.getConexao(Conexao.stringDeConexao, Conexao.usuario, Conexao.senha);
    }

    public void inserirHB(Habilidades habilidade) {
        try (Connection conexao = criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(SQL_INSERT)) {

            configurarPreparedStatement(pstmt, habilidade);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar habilidade no banco de dados", e);
        }
    }

    public void atualizarBD(Habilidades habilidade) {
        try (Connection conexao = criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(SQL_UPDATE)) {

            configurarPreparedStatement(pstmt, habilidade);
            pstmt.setString(4, habilidade.getNome()); // Define o nome para o WHERE
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar habilidade no banco de dados", e);
        }
    }

    public void excluirBD(Habilidades habilidade) {
        try (Connection conexao = criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(SQL_DELETE)) {

            pstmt.setString(1, habilidade.getNome());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir habilidade do banco de dados", e);
        }
    }

    public List<Habilidades> buscarTodosH() {
        List<Habilidades> habilidades = new ArrayList<>();

        try (Connection conexao = criarConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {

            while (rs.next()) {
                Habilidades habilidade = new Habilidades(
                        rs.getString("nome_habilidade"),
                        rs.getInt("custo_mana"),
                        rs.getInt("dano")
                );
                habilidades.add(habilidade);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar habilidades no banco de dados", e);
        }

        return habilidades;
    }

    private void configurarPreparedStatement(PreparedStatement pstmt, Habilidades habilidade) throws SQLException {
        pstmt.setString(1, habilidade.getNome());
        pstmt.setInt(2, habilidade.getDano());
        pstmt.setInt(3, habilidade.getMana());
    }
}
