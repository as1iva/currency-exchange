package org.as1iva.dto;

import java.math.BigDecimal;

public class ExchangeRateResponseDTO {
    private Integer id;
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private BigDecimal rate;

    public ExchangeRateResponseDTO(Integer id, CurrencyResponseDTO baseCurrency, CurrencyResponseDTO targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CurrencyResponseDTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyResponseDTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyResponseDTO getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyResponseDTO targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
