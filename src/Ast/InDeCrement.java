package Ast;

import Tokens.TokenType;

import java.util.HashMap;
import java.util.Map;

public enum InDeCrement {
    INCREMENT(TokenType.INCREMENT),  // -e
    DECREMENT(TokenType.DECREMENT); // not e

    private final TokenType token;

    InDeCrement(TokenType token) {
        this.token = token;
    }

    private static final Map<TokenType, InDeCrement> tokenToOperatorMap = new HashMap<>();

    static {
        for (InDeCrement op : values()) {
            tokenToOperatorMap.put(op.token, op);
        }
    }

    public static InDeCrement fromTokenType(TokenType type) {
        return tokenToOperatorMap.get(type);
    }
}
