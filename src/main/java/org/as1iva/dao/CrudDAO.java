package org.as1iva.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {
    T add(T t) throws SQLException;
    List<T> getAll() throws SQLException;
    void update(T t) throws SQLException;

}
