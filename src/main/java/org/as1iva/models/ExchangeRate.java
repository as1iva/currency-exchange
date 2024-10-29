package org.as1iva.models;

import java.math.BigDecimal;

public class ExchangeRate {
    private Integer id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

    public ExchangeRate(Integer id, int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public ExchangeRate(Integer id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public ExchangeRate() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", baseCurrencyId=" + baseCurrencyId +
                ", targetCurrencyId=" + targetCurrencyId +
                ", rate=" + rate +
                '}';
    }
}
