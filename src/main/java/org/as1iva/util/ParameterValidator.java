package org.as1iva.util;

import org.as1iva.exceptions.InvalidDataException;

import java.math.BigDecimal;
import java.util.Currency;

public final class ParameterValidator {
    private ParameterValidator() {
    }

    public static void checkName(String name) {
        if (name == null || !(name.length() > 1)) {
            throw new InvalidDataException("Name must be at least 1 character long");
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

    public static void checkCodePair(String codes) {
        if (codes == null || !(codes.length() == 6)) {
            throw new InvalidDataException("Pair of codes must be 6 characters long");
        }
    }

    public static void checkRate(BigDecimal rate) {
        if (rate == null || !(rate.compareTo(BigDecimal.ZERO) > 0)) {
            throw new InvalidDataException("Rate must be greater than 0");
        }
    }

    public static void checkAmount(String amount) {
        int amountValue = Integer.parseInt(amount);
        if (!(amountValue > 0)) {
            throw new InvalidDataException("Amount must be greater than 0");
        }
    }
}
