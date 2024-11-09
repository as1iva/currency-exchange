package org.as1iva.dao;

import org.as1iva.model.Currency;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<Currency> {
    Optional<Currency> getByCode(String code);
}
