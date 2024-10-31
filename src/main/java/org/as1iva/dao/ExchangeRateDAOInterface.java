package org.as1iva.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateDAOInterface<T> {
    T add(T t) throws SQLException;
    Optional<T> getByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException;
    List<T> getAll() throws SQLException;
    void update(T t) throws SQLException;
}
