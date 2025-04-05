package Ast;

import Tokens.TokenType;

// Unary operators (from TokenType)
public enum UnaryOperators {
    NEG(TokenType.MINUS),  // -e
    NOT(TokenType.NEGATION); // not e

    private final TokenType token;

    UnaryOperators(TokenType token) {
        this.token = token;
    }

}
