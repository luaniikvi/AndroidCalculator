package com.example.calculator;


import android.content.res.Resources;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
public class ExpressionCalculator {
    private static final Resources appResource = MyApp.getAppContext().getResources();

    private static final MathContext MC = new MathContext(
            appResource.getInteger(R.integer.precision),
            RoundingMode.HALF_UP
    );

    public static  String evaluateExpression_toString(String expression) {
        return evaluateExpression(expression).toString();
    }

    public static BigDecimal evaluateExpression(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException(
                    "Biểu thức không được để trống"
            );
        }

        Parser parser = new Parser(expression);
        BigDecimal result = parser.parse();
        result = result.stripTrailingZeros();

        if (result.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (result.scale() < 0) {
            return result.setScale(0, RoundingMode.UNNECESSARY);
        }

        return result;
    }

    private static class Parser {

        private final String input;
        private int pos = 0;

        Parser(String input) {
            this.input = input;
        }

        // Entry point
        BigDecimal parse() {
            BigDecimal result = parseExpression();

            skipSpaces();

            if (pos < input.length()) {
                throw error(
                        appResource.getString(R.string.error_invalid_format)+": '" + input.charAt(pos) + "'"
                );
            }

            return result;
        }

        private BigDecimal parseExpression() {
            BigDecimal value = parseTerm();

            while (true) {
                skipSpaces();

                if (match('+')) {
                    value = value.add(parseTerm(), MC);

                } else if (match('-')) {
                    value = value.subtract(parseTerm(), MC);

                } else {
                    break;
                }
            }

            return value;
        }

        private BigDecimal parseTerm() {
            BigDecimal value = parseUnary();

            while (true) {
                skipSpaces();

                if (match(Symbols.MULTIPLY.charAt(0))) {
                    value = value.multiply(parseUnary(), MC);

                } else if (match(Symbols.DIVIDE.charAt(0))) {
                    BigDecimal divisor = parseUnary();

                    if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                        throw error(appResource.getString(R.string.error_divide_by_zero));
                    }

                    value = value.divide(divisor, MC);

                } else {
                    break;
                }
            }

            return value;
        }

        // unary = ('+' | '-') unary | postfix
        private BigDecimal parseUnary() {
            skipSpaces();

            if (match(Symbols.PLUS.charAt(0))) {
                return parseUnary();
            }

            if (match(Symbols.MINUS.charAt(0))) {
                return parseUnary().negate();
            }

            return parsePostfix();
        }


        private BigDecimal parsePostfix() {
            BigDecimal value = parseNumber();

            while (true) {
                skipSpaces();

                if (match(Symbols.PERCENT.charAt(0))) {
                    value = value.divide(
                            BigDecimal.valueOf(100),
                            MC
                    );
                } else {
                    break;
                }
            }

            return value;
        }


        private BigDecimal parseNumber() {
            skipSpaces();

            int start = pos;
            boolean hasDigit = false;

            while (pos < input.length()
                    && Character.isDigit(input.charAt(pos))) {
                pos++;
                hasDigit = true;
            }

            if (match(Symbols.DOT.charAt(0))) {
                while (pos < input.length()
                        && Character.isDigit(input.charAt(pos))) {
                    pos++;
                    hasDigit = true;
                }
            }

            if (!hasDigit) {
                throw error(appResource.getString(R.string.error_invalid_format));
            }

            try {
                return new BigDecimal(input.substring(start, pos));
            } catch (NumberFormatException e) {
                throw error(appResource.getString(R.string.error_invalid_format));
            }
        }

        private boolean match(char expected) {
            if (pos < input.length()
                    && input.charAt(pos) == expected) {
                pos++;
                return true;
            }

            return false;
        }

        private void skipSpaces() {
            while (pos < input.length()
                    && Character.isWhitespace(input.charAt(pos))) {
                pos++;
            }
        }

        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException(
                    message
            );
        }
    }
}
