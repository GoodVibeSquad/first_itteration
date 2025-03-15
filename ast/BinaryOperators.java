package Compiler.ast;

import Compiler.Lexer.TokenType;

// Binary operators (from TokenType)
enum BinaryOperators {
    PLUS(TokenType.PLUS), MINUS(TokenType.MINUS), MULTIPLY(TokenType.MULTIPLY), DIVISION(TokenType.DIVISION), MODULUS(TokenType.MODULUS),
    COMPARISON(TokenType.COMPARISON), NOT_EQUALS(TokenType.NOT_EQUALS), LESS_THAN(TokenType.LESS_THAN), LESS_OR_EQUALS(TokenType.LESS_OR_EQUALS),
    GREATER_THAN(TokenType.GREATER_THAN), GREATER_OR_EQUALS(TokenType.GREATER_OR_EQUALS),
    AND(TokenType.AND), OR(TokenType.OR);

    private final TokenType token;

    BinaryOperators(TokenType token) {
        this.token = token;
    }
}
