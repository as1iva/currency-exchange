package org.as1iva.service;

import org.as1iva.dao.JdbcExchangeRateDAO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.dto.ExchangeRequestDTO;
import org.as1iva.dto.ExchangeResponseDTO;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.math.MathContext.DECIMAL64;

public class ExchangeService {
    private final JdbcExchangeRateDAO jdbcExchangeRateDAO;

    public ExchangeService(JdbcExchangeRateDAO jdbcExchangeRateDAO) {
        this.jdbcExchangeRateDAO = jdbcExchangeRateDAO;
    }

    public ExchangeResponseDTO exchange(ExchangeRequestDTO exchangeRequestDTO) {
        ExchangeRate exchangeRate = findExchangeRate(exchangeRequestDTO)
                .orElseThrow(() -> new DataNotFoundException("No exchange rates was found"));

        Integer amount = exchangeRequestDTO.getAmount();

        BigDecimal convertedAmount = BigDecimal.valueOf(amount)
                .multiply(exchangeRate.getRate())
                .setScale(2, RoundingMode.HALF_EVEN);

        return new ExchangeResponseDTO(
                new CurrencyResponseDTO(
                        exchangeRate.getBaseCurrency().getId(),
                        exchangeRate.getBaseCurrency().getCode(),
                        exchangeRate.getBaseCurrency().getFullName(),
                        exchangeRate.getBaseCurrency().getSign()
                ),
                new CurrencyResponseDTO(
                        exchangeRate.getTargetCurrency().getId(),
                        exchangeRate.getTargetCurrency().getCode(),
                        exchangeRate.getTargetCurrency().getFullName(),
                        exchangeRate.getTargetCurrency().getSign()
                ),
                exchangeRate.getRate(),
                amount,
                convertedAmount
        );
    }

    private Optional<ExchangeRate> findExchangeRate(ExchangeRequestDTO exchangeRequestDTO) {
        Optional<ExchangeRate> exchangeRate = findByDirectRate(exchangeRequestDTO);

        if (exchangeRate.isEmpty()) {
            exchangeRate = findByIndirectRate(exchangeRequestDTO);
        }

        if (exchangeRate.isEmpty()) {
            exchangeRate = findByCrossRate(exchangeRequestDTO);
        }

        return exchangeRate;
    }

    private Optional<ExchangeRate> findByDirectRate(ExchangeRequestDTO exchangeRequestDTO) {
        return jdbcExchangeRateDAO.getByCode(exchangeRequestDTO.getBaseCurrencyCode(), exchangeRequestDTO.getTargetCurrencyCode());
    }

    private Optional<ExchangeRate> findByIndirectRate(ExchangeRequestDTO exchangeRequestDTO) {
        Optional<ExchangeRate> exchangeRateOptional = jdbcExchangeRateDAO.getByCode(exchangeRequestDTO.getTargetCurrencyCode(), exchangeRequestDTO.getBaseCurrencyCode());

        if (exchangeRateOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRate exchangeRate = exchangeRateOptional.get();

        BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.getRate(), DECIMAL64)
                .setScale(6, RoundingMode.HALF_EVEN);

        return Optional.of(new ExchangeRate(
                exchangeRate.getTargetCurrency(),
                exchangeRate.getBaseCurrency(),
                rate
        ));
    }

    private Optional<ExchangeRate> findByCrossRate(ExchangeRequestDTO exchangeRequestDTO) {
        Optional<ExchangeRate> usdToBaseOptional = jdbcExchangeRateDAO.getByCode("USD", exchangeRequestDTO.getBaseCurrencyCode());
        Optional<ExchangeRate> usdToTargetOptional = jdbcExchangeRateDAO.getByCode("USD", exchangeRequestDTO.getTargetCurrencyCode());

        if (usdToBaseOptional.isEmpty() || usdToTargetOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRate usdToBase = usdToBaseOptional.get();
        ExchangeRate usdToTarget = usdToTargetOptional.get();

        BigDecimal rate = usdToBase.getRate().divide(usdToTarget.getRate(), DECIMAL64)
                .setScale(6, RoundingMode.HALF_EVEN);

        return Optional.of(new ExchangeRate(
                usdToBase.getTargetCurrency(),
                usdToTarget.getBaseCurrency(),
                rate
        ));
    }
}
