package Ast;

import Compiler.Lexer.TokenType;

// Unary operators (from TokenType)
enum UnaryOperators {
    NEG(TokenType.MINUS),  // -e
    NOT(TokenType.NEGATION); // not e

    private final TokenType token;

    UnaryOperators(TokenType token) {
        this.token = token;
    }
}
