package Ast;

import Tokens.TokenType;

import java.util.HashMap;
import java.util.Map;

// Binary operators (from TokenType)
public enum BinaryOperators {
    PLUS(TokenType.PLUS, 2, Associativity.LEFT),
    MINUS(TokenType.MINUS, 2, Associativity.LEFT),
    MULTIPLY(TokenType.MULTIPLY, 3, Associativity.LEFT),
    DIVISION(TokenType.DIVISION, 3, Associativity.LEFT),
    MODULUS(TokenType.MODULUS, 3, Associativity.LEFT),
    EXPONENT(TokenType.EXPONENT, 4, Associativity.RIGHT),
    EQUALS(TokenType.EQUALS, 1, Associativity.LEFT),
    NOT_EQUALS(TokenType.NOT_EQUALS, 1, Associativity.LEFT),
    LESS_THAN(TokenType.LESS_THAN, 1, Associativity.LEFT),
    LESS_OR_EQUALS(TokenType.LESS_OR_EQUALS, 1, Associativity.LEFT),
    GREATER_THAN(TokenType.GREATER_THAN, 1, Associativity.LEFT),
    GREATER_OR_EQUALS(TokenType.GREATER_OR_EQUALS, 1, Associativity.LEFT),
    AND(TokenType.AND, 0, Associativity.LEFT),
    OR(TokenType.OR, 0, Associativity.LEFT);

    private final TokenType token;
    private final int precedence;
    private final Associativity associativity;

    BinaryOperators(TokenType token, int precedence, Associativity associativity) {
        this.token = token;
        this.precedence = precedence;
        this.associativity = associativity;
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

    public int getPrecedence() {
        return precedence;
    }

    public Associativity getAssociativity() {
        return associativity;
    }

    public enum Associativity { LEFT, RIGHT }


    public String toSymbol() {
        return switch (this) {
            case PLUS -> "+";
            case MINUS -> "-";
            case MULTIPLY -> "*";
            case DIVISION -> "/";
            case MODULUS -> "%";
            case EXPONENT -> "**";
            case EQUALS -> "==";
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

