package org.as1iva.dao;

import java.sql.SQLException;
import java.util.Optional;

public interface CurrencyDAO<T> extends CrudDAO<T> {
    Optional<T> getByCode(String code) throws SQLException;
}
