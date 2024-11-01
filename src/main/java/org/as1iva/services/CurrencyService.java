package org.as1iva.services;

import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dto.CurrencyRequestDTO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.models.Currency;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private final JdbcCurrencyDAO jdbcCurrencyDAO;

    public CurrencyService(JdbcCurrencyDAO jdbcCurrencyDAO) {
        this.jdbcCurrencyDAO = jdbcCurrencyDAO;
    }

    public CurrencyResponseDTO add(CurrencyRequestDTO currencyRequestDTO) throws SQLException {
        Currency currency = new Currency(null, currencyRequestDTO.getCode(), currencyRequestDTO.getFullName(), currencyRequestDTO.getSign());
        currency = jdbcCurrencyDAO.add(currency);

        return new CurrencyResponseDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }

    public CurrencyResponseDTO getByCode(CurrencyRequestDTO currencyRequestDTO) throws SQLException {
        String code = currencyRequestDTO.getCode();

        Optional<Currency> currency = jdbcCurrencyDAO.getByCode(code);

        if (currency.isPresent()) {
            Currency currency1 = currency.get();
            return new CurrencyResponseDTO(
                    currency1.getId(),
                    currency1.getCode(),
                    currency1.getFullName(),
                    currency1.getSign());
        } else {
            return new CurrencyResponseDTO(null, null, null, null);
        }
    }

    public List<CurrencyResponseDTO> getAll() throws SQLException {
        List<CurrencyResponseDTO> currencyResponseDTOS = new ArrayList<>();

        List<Currency> currencies = jdbcCurrencyDAO.getAll();

        for (Currency currency : currencies) {
            currencyResponseDTOS.add(new CurrencyResponseDTO(
                    currency.getId(),
                    currency.getCode(),
                    currency.getFullName(),
                    currency.getSign()
            ));
        }

        return currencyResponseDTOS;
    }
}
