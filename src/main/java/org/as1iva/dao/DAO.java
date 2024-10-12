package org.as1iva.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    void add(T t) throws SQLException;
    Optional<T> getById(int id) throws SQLException;
    List<T> getAll() throws SQLException;
    void update(T t) throws SQLException;
}
