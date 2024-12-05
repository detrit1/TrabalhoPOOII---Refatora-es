package org.exemplo.curso1.dao;

import org.exemplo.curso1.model.Jogador;
import java.sql.SQLException;

public interface IJogador{                                                      // Interface que acessa o metodo para inserir informações
     void inserirBD(Jogador jogador) throws SQLException;                       // no banco de dados
}