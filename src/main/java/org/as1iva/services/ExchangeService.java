package org.as1iva.services;

import org.as1iva.dao.CurrencyDAO;
import org.as1iva.dao.ExchangeRateDAO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.dto.ExchangeRequestDTO;
import org.as1iva.dto.ExchangeResponseDTO;
import org.as1iva.models.Currency;
import org.as1iva.models.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeService {
    private final ExchangeRateDAO exchangeRateDAO;
    private final CurrencyDAO currencyDAO;

    public ExchangeService(ExchangeRateDAO exchangeRateDAO, CurrencyDAO currencyDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
        this.currencyDAO = currencyDAO;
    }

    public ExchangeResponseDTO exchange(ExchangeRequestDTO exchangeRequestDTO) throws SQLException {
        String baseCurrencyCode = exchangeRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRequestDTO.getTargetCurrencyCode();
        Integer amount = exchangeRequestDTO.getAmount();

        Optional<ExchangeRate> exchangeRateOptional = exchangeRateDAO.getByCode(baseCurrencyCode, targetCurrencyCode);
        Optional<Currency> baseCurrencyOptional = currencyDAO.getByCode(baseCurrencyCode);
        Optional<Currency> targetCurrencyOptional = currencyDAO.getByCode(targetCurrencyCode);

        if (exchangeRateOptional.isPresent() && baseCurrencyOptional.isPresent() && targetCurrencyOptional.isPresent()) {
            ExchangeRate exchangeRate = exchangeRateOptional.get();
            Currency baseCurrency = baseCurrencyOptional.get();
            Currency targetCurrency = targetCurrencyOptional.get();

            BigDecimal rate = exchangeRate.getRate();

            BigDecimal convertedAmount = rate.multiply(BigDecimal.valueOf(amount));

            return new ExchangeResponseDTO(
                    new CurrencyResponseDTO(
                            baseCurrency.getId(),
                            baseCurrency.getCode(),
                            baseCurrency.getFullName(),
                            baseCurrency.getSign()
                    ),
                    new CurrencyResponseDTO(
                            targetCurrency.getId(),
                            targetCurrency.getCode(),
                            targetCurrency.getFullName(),
                            targetCurrency.getSign()
                    ),
                    rate,
                    amount,
                    convertedAmount
            );
        } else {
            return new ExchangeResponseDTO(null, null, null, null, null);
        }
    }
}
