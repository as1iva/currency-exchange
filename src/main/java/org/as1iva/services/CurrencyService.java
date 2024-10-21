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

    public void add(CurrencyDTO currencyDTO) throws SQLException {
        Currency currency = new Currency(null, currencyDTO.getCode(), currencyDTO.getFullName(), currencyDTO.getSign());
        currencyDAO.add(currency);
    }
}
