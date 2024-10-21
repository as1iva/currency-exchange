package org.as1iva.services;

import org.as1iva.dao.CurrencyDAO;
import org.as1iva.dto.CurrencyDTO;
import org.as1iva.models.Currency;

import java.sql.SQLException;

public class CurrencyService {

    private final CurrencyDAO currencyDAO;

    public CurrencyService(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }
}
