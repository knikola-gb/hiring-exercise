package com.gb.app.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MathUtil implements Serializable {

    private static NumberFormat numberFormat = DecimalFormat.getNumberInstance();

    static {
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(2);
    }

    public static Double multiply(Number num1, Number num2) {
        if (num1 == null || num2 == null) {
            return null;
        }

        return BigDecimal.valueOf(num1.doubleValue()).multiply(BigDecimal.valueOf(num2.doubleValue())).doubleValue();
    }

    public static Double divide(Number num1, Number num2) {
        if (num1 == null || isZeroOrNull(num2)) {
            return null;
        }

        return BigDecimal.valueOf(num1.doubleValue())
                .divide(BigDecimal.valueOf(num2.doubleValue()), 50, RoundingMode.HALF_EVEN).doubleValue();

    }

    public static Double addSubtractImpl(Number num1, Number num2, boolean is_addition) {
        String num1_str = convertToString(num1);
        String num2_str = convertToString(num2);

        if (num1_str == null && num2_str == null) {
            return null;
        }

        BigDecimal first_num = num1_str != null ? new BigDecimal(num1_str) : new BigDecimal(0);
        BigDecimal second_num = num2_str != null ? new BigDecimal(num2_str) : new BigDecimal(0);
        BigDecimal result = is_addition ? first_num.add(second_num) : first_num.subtract(second_num);

        return result.doubleValue();
    }

    public static Double add(Number num1, Number num2) {
        return addSubtractImpl(num1, num2, true);
    }

    public static Double subtract(Number num1, Number num2) {
        return addSubtractImpl(num1, num2, false);
    }

    public static Double roundNumber(Double number, int maxFractionDigit) {
        if (number == null) {
            return null;
        }

        try {
            Double.parseDouble(String.valueOf(number));
        } catch (Exception e) {
            return null;
        }

        return roundNumber(number.doubleValue(), maxFractionDigit);
    }

    public static Double roundNumber(double number, int maxFractionDigit) {
        if (maxFractionDigit < 0) {
            throw new IllegalArgumentException("Fraction Digits to round on can not be less than zero!");
        }

        if (number == 0.0) {
            return number;
        }

        boolean isNegative = (number < 0);

        if (isNegative) {
            number = -number;
        }
        Double roundedNumber = Double.valueOf(numberFormat.format(number));

        if (isNegative && roundedNumber != 0) {
            roundedNumber = -roundedNumber;
        }
        return roundedNumber;
    }

    public static boolean isZeroOrNull(Number number) {
        return number == null || BigDecimal.valueOf(number.doubleValue()).compareTo(BigDecimal.ZERO) == 0;
    }

    public static String convertToString(Object obj) {
        if (obj == null) {
            return null;
        }
        String result = obj.toString().trim();

        if (result.equals("")) {
            result = null;
        }

        return result;
    }
}
