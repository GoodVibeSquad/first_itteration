package Ast;

import Tokens.TokenType;

import java.util.HashMap;
import java.util.Map;

// Binary operators (from TokenType)
public enum BinaryOperators {
    PLUS(TokenType.PLUS), MINUS(TokenType.MINUS), MULTIPLY(TokenType.MULTIPLY), DIVISION(TokenType.DIVISION), MODULUS(TokenType.MODULUS), EXPONENT(TokenType.EXPONENT),
    COMPARISON(TokenType.COMPARISON), NOT_EQUALS(TokenType.NOT_EQUALS), LESS_THAN(TokenType.LESS_THAN), LESS_OR_EQUALS(TokenType.LESS_OR_EQUALS),
    GREATER_THAN(TokenType.GREATER_THAN), GREATER_OR_EQUALS(TokenType.GREATER_OR_EQUALS),
    AND(TokenType.AND), OR(TokenType.OR);

    private final TokenType token;

    BinaryOperators(TokenType token) {
        this.token = token;
    }

    private static final Map<TokenType, BinaryOperators> tokenToOperatorMap = new HashMap<>();

    static {
        for (BinaryOperators op : values()) {
            tokenToOperatorMap.put(op.token, op);
        }
    }

    public static BinaryOperators fromTokenType(TokenType type) {
        return tokenToOperatorMap.get(type);
    }

    public String toSymbol() {
        return switch (this) {
            case PLUS -> "+";
            case MINUS -> "-";
            case MULTIPLY -> "*";
            case DIVISION -> "/";
            case MODULUS -> "%";
            case EXPONENT -> "**";
            case COMPARISON -> "==";
            case NOT_EQUALS -> "!=";
            case LESS_THAN -> "<";
            case LESS_OR_EQUALS -> "<=";
            case GREATER_THAN -> ">";
            case GREATER_OR_EQUALS -> ">=";
            case AND -> "and";
            case OR -> "or";
        };
    }


}
