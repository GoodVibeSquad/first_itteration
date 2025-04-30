package Ast;

import Tokens.TokenType;

import java.util.HashMap;
import java.util.Map;

public enum InDeCrement {
    INCREMENT(TokenType.INCREMENT, 5),
    DECREMENT(TokenType.DECREMENT, 5);

    private final TokenType token;
    private final int precedence;

    InDeCrement(TokenType token, int precedence) {
        this.token = token;
        this.precedence = precedence;
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

    public int getPrecedence() {
        return precedence;
    }
}
