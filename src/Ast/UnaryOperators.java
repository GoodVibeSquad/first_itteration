package Ast;

import Tokens.TokenType;

import java.util.HashMap;
import java.util.Map;

// Unary operators (from TokenType)
public enum UnaryOperators {
    NEG(TokenType.MINUS),  // -e
    NOT(TokenType.NEGATION); // not e

    private final TokenType token;

    UnaryOperators(TokenType token) {
        this.token = token;
    }

    private static final Map<TokenType, UnaryOperators> tokenToOperatorMap = new HashMap<>();

    static {
        for (UnaryOperators op : values()) {
            tokenToOperatorMap.put(op.token, op);
        }
    }

    public static UnaryOperators fromTokenType(TokenType type) {
        return tokenToOperatorMap.get(type);
    }

    public String toSymbol() {
        return switch (this) {
            case NEG -> "-";
            case NOT -> "!";
        };
    }

}
