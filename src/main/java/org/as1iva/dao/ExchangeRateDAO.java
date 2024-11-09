package org.as1iva.dao;

import org.as1iva.model.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDAO extends CrudDAO<ExchangeRate> {
    Optional<ExchangeRate> getByCode(String baseCurrencyCode, String targetCurrencyCode);
}
