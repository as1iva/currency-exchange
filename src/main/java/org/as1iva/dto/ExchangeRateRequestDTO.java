package org.as1iva.dto;

import java.math.BigDecimal;

public final class ExchangeRateRequestDTO {
    private final String baseCurrencyCode;
    private final String targetCurrencyCode;
    private final BigDecimal rate;

    public ExchangeRateRequestDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
