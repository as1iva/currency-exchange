package org.as1iva.dto;

import java.math.BigDecimal;

public final class ExchangeRateResponseDTO {
    private final Integer id;
    private final CurrencyResponseDTO baseCurrency;
    private final CurrencyResponseDTO targetCurrency;
    private final BigDecimal rate;

    public ExchangeRateResponseDTO(Integer id, CurrencyResponseDTO baseCurrency, CurrencyResponseDTO targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public CurrencyResponseDTO getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyResponseDTO getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
