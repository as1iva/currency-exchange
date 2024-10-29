package org.as1iva.services;

import org.as1iva.dao.CurrencyDAO;
import org.as1iva.dao.ExchangeRateDAO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.dto.ExchangeRateRequestDTO;
import org.as1iva.dto.ExchangeRateResponseDTO;
import org.as1iva.models.Currency;
import org.as1iva.models.ExchangeRate;

import java.sql.SQLException;
import java.util.Optional;

public class ExchangeRateService {

    private final ExchangeRateDAO exchangeRateDAO;
    private final CurrencyDAO currencyDAO;

    public ExchangeRateService(ExchangeRateDAO exchangeRateDAO, CurrencyDAO currencyDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
        this.currencyDAO = currencyDAO;
    }

    public ExchangeRateResponseDTO add(ExchangeRateRequestDTO exchangeRateRequestDTO) throws SQLException {
        Optional<Currency> baseCurrency = currencyDAO.getById(exchangeRateRequestDTO.getBaseCurrencyId());
        Optional<Currency> targetCurrency = currencyDAO.getById(exchangeRateRequestDTO.getTargetCurrencyId());

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            // TODO: обработать ошибку
            return new ExchangeRateResponseDTO(null, null, null, null);
        }

        Currency newBaseCurrency = baseCurrency.get();
        Currency newTargetCurrency = targetCurrency.get();

        ExchangeRate exchangeRate = new ExchangeRate(
                null,
                newBaseCurrency.getId(),
                newTargetCurrency.getId(),
                exchangeRateRequestDTO.getRate());

        exchangeRate = exchangeRateDAO.add(exchangeRate);

        CurrencyResponseDTO baseCurrencyResponseDTO = new CurrencyResponseDTO(
                newBaseCurrency.getId(),
                newBaseCurrency.getCode(),
                newBaseCurrency.getFullName(),
                newBaseCurrency.getSign()
        );

        CurrencyResponseDTO targetCurrencyResponseDTO = new CurrencyResponseDTO(
                newTargetCurrency.getId(),
                newTargetCurrency.getCode(),
                newTargetCurrency.getFullName(),
                newTargetCurrency.getSign()
        );

        return new ExchangeRateResponseDTO(
                exchangeRate.getId(),
                baseCurrencyResponseDTO,
                targetCurrencyResponseDTO,
                exchangeRate.getRate()
        );
    }
}
