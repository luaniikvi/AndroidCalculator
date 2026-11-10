package com.example.calculator;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExpressionCalculator {

    private static final MathContext MC =
            new MathContext(20, RoundingMode.HALF_UP);

    public static BigDecimal evaluateExpression(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException(
                    "Biểu thức không được để trống"
            );
        }

        Parser parser = new Parser(expression);
        return parser.parse();
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
                        "Ký tự không hợp lệ: '" + input.charAt(pos) + "'"
                );
            }

            return result;
        }

        // expression = term (('+' | '-') term)*
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

        // term = unary (('×' | '÷') unary)*
        private BigDecimal parseTerm() {
            BigDecimal value = parseUnary();

            while (true) {
                skipSpaces();

                if (match('×')) {
                    value = value.multiply(parseUnary(), MC);

                } else if (match('÷')) {
                    BigDecimal divisor = parseUnary();

                    if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                        throw error("Không thể chia cho 0");
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

            if (match('+')) {
                return parseUnary();
            }

            if (match('-')) {
                return parseUnary().negate();
            }

            return parsePostfix();
        }

        // postfix = number ('%')*
        private BigDecimal parsePostfix() {
            BigDecimal value = parseNumber();

            while (true) {
                skipSpaces();

                if (match('%')) {
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

        // number = digits ['.' digits] | '.' digits
        private BigDecimal parseNumber() {
            skipSpaces();

            int start = pos;
            boolean hasDigit = false;

            while (pos < input.length()
                    && Character.isDigit(input.charAt(pos))) {
                pos++;
                hasDigit = true;
            }

            if (match('.')) {
                while (pos < input.length()
                        && Character.isDigit(input.charAt(pos))) {
                    pos++;
                    hasDigit = true;
                }
            }

            if (!hasDigit) {
                throw error("Thiếu số tại vị trí này");
            }

            try {
                return new BigDecimal(input.substring(start, pos));
            } catch (NumberFormatException e) {
                throw error("Số không hợp lệ");
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
                    message + " (vị trí " + pos + ")"
            );
        }
    }
}
