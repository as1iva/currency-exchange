package org.as1iva.dto;

public final class ExchangeRequestDTO {
    private final String baseCurrencyCode;
    private final String targetCurrencyCode;
    private final Integer amount;

    public ExchangeRequestDTO(String baseCurrencyCode, String targetCurrencyCode, Integer amount) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.amount = amount;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public Integer getAmount() {
        return amount;
    }
}
