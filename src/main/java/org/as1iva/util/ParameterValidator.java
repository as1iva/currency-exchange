package org.as1iva.util;

import org.as1iva.exception.InvalidDataException;

import java.math.BigDecimal;
import java.util.Currency;

public final class ParameterValidator {
    private ParameterValidator() {
    }

    public static void checkName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidDataException("Name must be at least 1 character long");
        }

        if (!name.matches("[A-Za-z\\s]+")) {
            throw new InvalidDataException("Name must be a string with english characters");
        }

        if (!(name.length() < 25)) {
            throw new InvalidDataException("Name must be shorter than 25");
        }
    }

    public static void checkCode(String code) {
        if (code == null || !(code.length() == 3)) {
            throw new InvalidDataException("Code must be 3 characters long");
        }

        try {
            Currency.getInstance(code);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("This currency doesn't exist");
        }
    }

    public static void checkSign(String sign) {
        if (sign == null || sign.isEmpty() || !(sign.length() <= 4)) {
            throw new InvalidDataException("Sign must be up to 4 characters long");
        }
    }

    public static void checkCodePair(String baseCode, String targetCode) {
        String codes = baseCode + targetCode;

        if (baseCode.equals(targetCode)) {
            throw new InvalidDataException("Pair of codes can't be equal");
        }

        if (codes.length() != 6) {
            throw new InvalidDataException("Pair of codes must be 6 characters long");
        }
    }

    public static void checkRate(String rate) {
        if (rate == null || rate.isEmpty()) {
            throw new InvalidDataException("Rate can't be empty");
        }

        if (!rate.matches("^-?\\d+(\\.\\d+)?$")) {
            throw new InvalidDataException("Rate must be a number, which can be an integer or a decimal");
        }

        if (!(rate.length() < 10)) {
            throw new InvalidDataException("Rate is too long");
        }

        BigDecimal rateValue = new BigDecimal(rate);
        if (!(rateValue.compareTo(BigDecimal.ZERO) > 0)) {
            throw new InvalidDataException("Rate must be greater than 0");
        }
    }

    public static void checkAmount(String amount) {
        if (amount == null || amount.isEmpty()) {
            throw new InvalidDataException("Amount can't be empty");
        }

        if (!amount.matches("-?\\d+")) {
            throw new InvalidDataException("Amount must be an integer");
        }

        int amountValue = Integer.parseInt(amount);
        if (!(amountValue > 0)) {
            throw new InvalidDataException("Amount must be greater than 0");
        }
    }
}
