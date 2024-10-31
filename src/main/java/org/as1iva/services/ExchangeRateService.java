package org.as1iva.services;

import org.as1iva.dao.CurrencyDAO;
import org.as1iva.dao.ExchangeRateDAO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.dto.ExchangeRateRequestDTO;
import org.as1iva.dto.ExchangeRateResponseDTO;
import org.as1iva.models.Currency;
import org.as1iva.models.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public ExchangeRateResponseDTO getByCode(ExchangeRateRequestDTO exchangeRateRequestDTO) throws SQLException {
        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();

        Optional<ExchangeRate> exchangeRate = exchangeRateDAO.getByCode(baseCurrencyCode, targetCurrencyCode);

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
            return new ExchangeRateResponseDTO(null, null, null, null);
        }
    }

    public List<ExchangeRateResponseDTO> getAll() throws SQLException {
        List<ExchangeRateResponseDTO> exchangeRateResponseDTOS = new ArrayList<>();

        List<ExchangeRate> exchangeRates = exchangeRateDAO.getAll();

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

    public ExchangeRateResponseDTO update(ExchangeRateRequestDTO exchangeRateRequestDTO) throws SQLException {
        Integer id = exchangeRateRequestDTO.getId();
        BigDecimal rate = exchangeRateRequestDTO.getRate();

        ExchangeRate exchangeRate = new ExchangeRate(
                id,
                null,
                null,
                rate);

        exchangeRateDAO.update(exchangeRate);

        Optional<ExchangeRate> exchangeRateOptional = exchangeRateDAO.getById(id);

        if (exchangeRateOptional.isPresent()) {
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
        } else {
            return new ExchangeRateResponseDTO(null, null, null, null);
        }
    }
}
