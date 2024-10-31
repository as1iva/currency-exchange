package org.as1iva.dto;

import java.math.BigDecimal;

public class ExchangeResponseDTO {
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private BigDecimal rate;
    private Integer amount;
    private BigDecimal convertedAmount;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
