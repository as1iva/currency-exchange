package org.as1iva.dto;

public final class CurrencyResponseDTO {
    private final Integer id;
    private final String code;
    private final String name;
    private final String sign;

    public CurrencyResponseDTO(Integer id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public Integer getId() {
        return id;
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
