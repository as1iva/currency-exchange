package org.as1iva.service;

import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dao.JdbcExchangeRateDAO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.dto.ExchangeRateRequestDTO;
import org.as1iva.dto.ExchangeRateResponseDTO;
import org.as1iva.exception.DataExistsException;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.model.Currency;
import org.as1iva.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {

    private final JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private final JdbcCurrencyDAO jdbcCurrencyDAO;

    public ExchangeRateService(JdbcExchangeRateDAO jdbcExchangeRateDAO, JdbcCurrencyDAO jdbcCurrencyDAO) {
        this.jdbcExchangeRateDAO = jdbcExchangeRateDAO;
        this.jdbcCurrencyDAO = jdbcCurrencyDAO;
    }

    public ExchangeRateResponseDTO add(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        Currency baseCurrency = jdbcCurrencyDAO.getByCode(exchangeRateRequestDTO.getBaseCurrencyCode())
                .orElseThrow(() -> new DataNotFoundException("Base currency not found"));

        Currency targetCurrency = jdbcCurrencyDAO.getByCode(exchangeRateRequestDTO.getTargetCurrencyCode())
                .orElseThrow(() -> new DataNotFoundException("Target currency not found"));

        ExchangeRate exchangeRate = new ExchangeRate(
                null,
                baseCurrency.getCode(),
                targetCurrency.getCode(),
                exchangeRateRequestDTO.getRate());

        jdbcExchangeRateDAO.getByCode(baseCurrency.getCode(), targetCurrency.getCode())
                .ifPresent(existingExchangeRate -> {
                    throw new DataExistsException("Exchange rate already exists");
                });

        exchangeRate = jdbcExchangeRateDAO.add(exchangeRate);

        return new ExchangeRateResponseDTO(
                exchangeRate.getId(),
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
                exchangeRate.getRate()
        );
    }

    public ExchangeRateResponseDTO getByCode(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();

        ExchangeRate exchangeRate = jdbcExchangeRateDAO.getByCode(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("Exchange rate not found"));

        return new ExchangeRateResponseDTO(
                exchangeRate.getId(),
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
                exchangeRate.getRate()
        );
    }

    public List<ExchangeRateResponseDTO> getAll() {
        List<ExchangeRateResponseDTO> exchangeRateResponseDTOS = new ArrayList<>();

        List<ExchangeRate> exchangeRates = jdbcExchangeRateDAO.getAll();

        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRateResponseDTOS.add(new ExchangeRateResponseDTO(
                    exchangeRate.getId(),
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
                    exchangeRate.getRate()
            ));
        }
        return exchangeRateResponseDTOS;
    }

    public ExchangeRateResponseDTO update(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDTO.getRate();

        ExchangeRate exchangeRate = new ExchangeRate(
                null,
                baseCurrencyCode,
                targetCurrencyCode,
                rate);

        jdbcExchangeRateDAO.update(exchangeRate);

        exchangeRate = jdbcExchangeRateDAO.getByCode(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("Exchange rate not found"));

        return new ExchangeRateResponseDTO(
                exchangeRate.getId(),
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
                exchangeRate.getRate()
        );
    }
}
