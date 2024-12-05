package org.exemplo.curso1.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.exemplo.curso1.model.Jogador;

public class JogadorDAO implements IJogador, IConst {
    private static final Logger LOGGER = Logger.getLogger(JogadorDAO.class.getName()); // Log
    private static final String SQL_INSERT = "INSERT INTO personagem (nome, id_personagem, classe, nivel, pontos_de_vida, pontos_de_mana) VALUES (?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE personagem SET nome=?, classe=?, nivel=?, pontos_de_vida=?, pontos_de_mana=? WHERE id_personagem=?";
    private static final String SQL_DELETE = "DELETE FROM personagem WHERE id_personagem = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM personagem";

    public void inserirBD(Jogador jogador) {
        try (Connection conexao = criarConexao();
             PreparedStatement pstmt = prepararInsercao(conexao, jogador)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            registrarErro("Erro ao salvar jogador no banco de dados", e);
        }
    }

    private PreparedStatement prepararInsercao(Connection conexao, Jogador jogador) throws SQLException {
        PreparedStatement pstmt = conexao.prepareStatement(SQL_INSERT);
        pstmt.setString(1, jogador.getNome());
        pstmt.setInt(2, jogador.getId_personagem());
        pstmt.setString(3, jogador.getClasse());
        pstmt.setInt(4, jogador.getNivel());
        pstmt.setInt(5, jogador.getP_vida());
        pstmt.setInt(6, jogador.getP_mana());
        return pstmt;
    }

    public void atualizarBD(Jogador jogador) throws SQLException {
        try (Connection conexao = criarConexao();
             PreparedStatement pstmt = prepararAtualizacao(conexao, jogador)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            registrarErro("Erro ao atualizar jogador no banco de dados", e);
        }
    }

    private PreparedStatement prepararAtualizacao(Connection conexao, Jogador jogador) throws SQLException {
        PreparedStatement pstmt = conexao.prepareStatement(SQL_UPDATE);
        pstmt.setString(1, jogador.getNome());
        pstmt.setString(2, jogador.getClasse());
        pstmt.setInt(3, jogador.getNivel());
        pstmt.setInt(4, jogador.getP_vida());
        pstmt.setInt(5, jogador.getP_mana());
        pstmt.setInt(6, jogador.getId_personagem());
        return pstmt;
    }

    public void excluirBD(Jogador jogador) throws SQLException{
        try (Connection conexao = criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(SQL_DELETE)) {
            pstmt.setInt(1, jogador.getId_personagem());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            registrarErro("Erro ao excluir jogador do banco de dados", e);
        }
    }

    public List<Jogador> buscarTodos() {
        List<Jogador> jogadores = new ArrayList<>();
        try (Connection conexao = criarConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {

            while (rs.next()) {
                Jogador jogador = new Jogador(
                        rs.getString("nome"),
                        rs.getInt("id_personagem"),
                        rs.getString("classe"),
                        rs.getInt("nivel"),
                        rs.getInt("pontos_de_vida"),
                        rs.getInt("pontos_de_mana")
                );
                jogadores.add(jogador);
            }
        } catch (SQLException e) {
            registrarErro("Erro ao buscar jogadores no banco de dados", e);
        }
        return jogadores;
    }

    private Connection criarConexao() throws SQLException {
        return Conexao.getConexao(Conexao.stringDeConexao, Conexao.usuario, Conexao.senha);
    }

    private void registrarErro(String mensagem, Exception e) {
        LOGGER.log(Level.SEVERE, mensagem, e);
    }
}
