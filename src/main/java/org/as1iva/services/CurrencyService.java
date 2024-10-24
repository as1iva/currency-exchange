package org.as1iva.services;

import org.as1iva.dao.CurrencyDAO;
import org.as1iva.dto.CurrencyRequestDTO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.models.Currency;

import java.sql.SQLException;

public class CurrencyService {

    private final CurrencyDAO currencyDAO;

    public CurrencyService(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public CurrencyResponseDTO add(CurrencyRequestDTO currencyRequestDTO) throws SQLException {
        Currency currency = new Currency(null, currencyRequestDTO.getCode(), currencyRequestDTO.getFullName(), currencyRequestDTO.getSign());
        currency = currencyDAO.add(currency);

        return new CurrencyResponseDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }
    }
}
