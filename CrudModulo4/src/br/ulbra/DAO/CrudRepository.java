package br.ulbra.DAO;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
    boolean salvar(T t) throws SQLException;
    T buscarPorId(int id) throws SQLException;
    List<T> listar() throws SQLException;
    boolean atualizar(T t) throws SQLException;
    boolean remover(int id) throws SQLException;
}
