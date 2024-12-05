package org.exemplo.curso1.dao;

import org.exemplo.curso1.model.Habilidades;
import java.sql.SQLException;

public interface IHabilidade{
     void inserirHB(Habilidades habilidades) throws SQLException;
}