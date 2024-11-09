package org.as1iva.service;

import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dto.CurrencyRequestDTO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.exception.DataExistsException;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.model.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private final JdbcCurrencyDAO jdbcCurrencyDAO;

    public CurrencyService(JdbcCurrencyDAO jdbcCurrencyDAO) {
        this.jdbcCurrencyDAO = jdbcCurrencyDAO;
    }

    public CurrencyResponseDTO add(CurrencyRequestDTO currencyRequestDTO)  {
        Currency currency = new Currency(null, currencyRequestDTO.getCode(), currencyRequestDTO.getFullName(), currencyRequestDTO.getSign());

        Optional<Currency> currencyOptional = jdbcCurrencyDAO.getByCode(currencyRequestDTO.getCode());

        if (currencyOptional.isPresent()) {
            throw new DataExistsException("Currency is already exists");
        }

        currency = jdbcCurrencyDAO.add(currency);

        return new CurrencyResponseDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }

    public CurrencyResponseDTO getByCode(CurrencyRequestDTO currencyRequestDTO) {
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
            throw new DataNotFoundException("Currency not found");
        }
    }

    public List<CurrencyResponseDTO> getAll() {
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
