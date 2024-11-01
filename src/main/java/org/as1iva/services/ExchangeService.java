package org.as1iva.services;

import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dao.JdbcExchangeRateDAO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.dto.ExchangeRequestDTO;
import org.as1iva.dto.ExchangeResponseDTO;
import org.as1iva.models.Currency;
import org.as1iva.models.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeService {
    private final JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private final JdbcCurrencyDAO jdbcCurrencyDAO;

    public ExchangeService(JdbcExchangeRateDAO jdbcExchangeRateDAO, JdbcCurrencyDAO jdbcCurrencyDAO) {
        this.jdbcExchangeRateDAO = jdbcExchangeRateDAO;
        this.jdbcCurrencyDAO = jdbcCurrencyDAO;
    }

    public ExchangeResponseDTO exchange(ExchangeRequestDTO exchangeRequestDTO) throws SQLException {
        String baseCurrencyCode = exchangeRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRequestDTO.getTargetCurrencyCode();
        Integer amount = exchangeRequestDTO.getAmount();

        Optional<ExchangeRate> directExchangeRateOptional = jdbcExchangeRateDAO.getByCode(baseCurrencyCode, targetCurrencyCode);
        Optional<ExchangeRate> reverseExchangeRateOptional = jdbcExchangeRateDAO.getByCode(targetCurrencyCode, baseCurrencyCode);

        Optional<ExchangeRate> usdToBaseOptional = jdbcExchangeRateDAO.getByCode("USD", baseCurrencyCode);
        Optional<ExchangeRate> usdToTargetOptional = jdbcExchangeRateDAO.getByCode("USD", targetCurrencyCode);

        Optional<Currency> baseCurrencyOptional = jdbcCurrencyDAO.getByCode(baseCurrencyCode);
        Optional<Currency> targetCurrencyOptional = jdbcCurrencyDAO.getByCode(targetCurrencyCode);

        if (directExchangeRateOptional.isPresent() && baseCurrencyOptional.isPresent() && targetCurrencyOptional.isPresent()) {
            ExchangeRate exchangeRate = directExchangeRateOptional.get();
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
        } else if (reverseExchangeRateOptional.isPresent() && baseCurrencyOptional.isPresent() && targetCurrencyOptional.isPresent()) {
            ExchangeRate exchangeRate = reverseExchangeRateOptional.get();
            Currency baseCurrency = baseCurrencyOptional.get();
            Currency targetCurrency = targetCurrencyOptional.get();

            BigDecimal dividend = new BigDecimal("1");

            BigDecimal rate = dividend.divide(exchangeRate.getRate(), 6, RoundingMode.HALF_UP);

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
        } else if (usdToBaseOptional.isPresent() && usdToTargetOptional.isPresent() && baseCurrencyOptional.isPresent() && targetCurrencyOptional.isPresent()) {
            ExchangeRate usdToBase = usdToBaseOptional.get();
            ExchangeRate usdToTarget = usdToTargetOptional.get();
            Currency baseCurrency = baseCurrencyOptional.get();
            Currency targetCurrency = targetCurrencyOptional.get();

            BigDecimal rate = usdToBase.getRate().divide(usdToTarget.getRate(), 6, RoundingMode.HALF_UP);

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
