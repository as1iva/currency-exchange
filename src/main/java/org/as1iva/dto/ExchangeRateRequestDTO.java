package org.as1iva.dto;

import java.math.BigDecimal;

public class ExchangeRateRequestDTO {
    private Integer id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;

    public ExchangeRateRequestDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public ExchangeRateRequestDTO(Integer id, BigDecimal rate) {
        this.id = id;
        this.rate = rate;
    }

    public ExchangeRateRequestDTO(String baseCurrencyCode, String targetCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
