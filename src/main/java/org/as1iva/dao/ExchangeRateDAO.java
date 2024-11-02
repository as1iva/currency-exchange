package org.as1iva.dao;

import java.sql.SQLException;
import java.util.Optional;

public interface ExchangeRateDAO<T> extends CrudDAO<T> {
    Optional<T> getByCode(String baseCurrencyCode, String targetCurrencyCode);
}
