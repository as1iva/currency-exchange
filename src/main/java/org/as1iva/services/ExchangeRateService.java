package org.as1iva.services;

import org.as1iva.dao.ExchangeRateDAO;
import org.as1iva.dto.ExchangeRateRequestDTO;
import org.as1iva.dto.ExchangeRateResponseDTO;
import org.as1iva.models.ExchangeRate;

import java.sql.SQLException;

public class ExchangeRateService {

    private final ExchangeRateDAO exchangeRateDAO;

    public ExchangeRateService(ExchangeRateDAO exchangeRateDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
    }
}
