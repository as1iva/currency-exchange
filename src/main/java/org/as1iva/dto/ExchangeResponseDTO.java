package org.as1iva.dto;

import java.math.BigDecimal;

public final class ExchangeResponseDTO {
    private final CurrencyResponseDTO baseCurrency;
    private final CurrencyResponseDTO targetCurrency;
    private final BigDecimal rate;
    private final Integer amount;
    private final BigDecimal convertedAmount;

    public ExchangeResponseDTO(CurrencyResponseDTO baseCurrency, CurrencyResponseDTO targetCurrency, BigDecimal rate, Integer amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
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

    public Integer getAmount() {
        return amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }
}
