package org.as1iva.services;

import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dao.JdbcExchangeRateDAO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.dto.ExchangeRateRequestDTO;
import org.as1iva.dto.ExchangeRateResponseDTO;
import org.as1iva.exceptions.DataExistsException;
import org.as1iva.exceptions.DataNotFoundException;
import org.as1iva.models.Currency;
import org.as1iva.models.ExchangeRate;

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
        Optional<Currency> baseCurrency = jdbcCurrencyDAO.getByCode(exchangeRateRequestDTO.getBaseCurrencyCode());
        Optional<Currency> targetCurrency = jdbcCurrencyDAO.getByCode(exchangeRateRequestDTO.getTargetCurrencyCode());

        if (baseCurrency.isEmpty() && targetCurrency.isEmpty()) {
            throw new DataNotFoundException("No currency was found");
        } else if (baseCurrency.isEmpty()) {
            throw new DataNotFoundException("Base currency not found");
        } else if (targetCurrency.isEmpty()) {
            throw new DataNotFoundException("Target currency not found");
        }

        ExchangeRate exchangeRate = new ExchangeRate(
                null,
                baseCurrency.get().getCode(),
                targetCurrency.get().getCode(),
                exchangeRateRequestDTO.getRate());

        Optional<ExchangeRate> exchangeRateOptional = jdbcExchangeRateDAO.getByCode(
                baseCurrency.get().getCode(),
                targetCurrency.get().getCode()
        );

        if (exchangeRateOptional.isPresent()) {
            throw new DataExistsException("Exchange rate already exists");
        }

        exchangeRate = jdbcExchangeRateDAO.add(exchangeRate);

        return new ExchangeRateResponseDTO(
                exchangeRate.getId(),
                new CurrencyResponseDTO(
                        baseCurrency.get().getId(),
                        baseCurrency.get().getCode(),
                        baseCurrency.get().getFullName(),
                        baseCurrency.get().getSign()
                ),
                new CurrencyResponseDTO(
                        targetCurrency.get().getId(),
                        targetCurrency.get().getCode(),
                        targetCurrency.get().getFullName(),
                        targetCurrency.get().getSign()
                ),
                exchangeRate.getRate()
        );
    }

    public ExchangeRateResponseDTO getByCode(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();

        Optional<ExchangeRate> exchangeRate = jdbcExchangeRateDAO.getByCode(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate.isPresent()) {
            ExchangeRate exchangeRate1 = exchangeRate.get();
            return new ExchangeRateResponseDTO(
                    exchangeRate1.getId(),
                    new CurrencyResponseDTO(
                            exchangeRate1.getBaseCurrency().getId(),
                            exchangeRate1.getBaseCurrency().getCode(),
                            exchangeRate1.getBaseCurrency().getFullName(),
                            exchangeRate1.getBaseCurrency().getSign()
                    ),
                    new CurrencyResponseDTO(
                            exchangeRate1.getTargetCurrency().getId(),
                            exchangeRate1.getTargetCurrency().getCode(),
                            exchangeRate1.getTargetCurrency().getFullName(),
                            exchangeRate1.getTargetCurrency().getSign()
                    ),
                    exchangeRate1.getRate()
            );
        } else {
            throw new DataNotFoundException("Exchange rate not found");
        }
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

        Optional<ExchangeRate> exchangeRateOptional = jdbcExchangeRateDAO.getByCode(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRateOptional.isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found");
        }

        exchangeRate = exchangeRateOptional.get();
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
