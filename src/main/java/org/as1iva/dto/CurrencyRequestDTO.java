package org.as1iva.dto;

public final class CurrencyRequestDTO {
    private final String code;
    private final String name;
    private final String sign;

    public CurrencyRequestDTO(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }
}
