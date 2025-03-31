package Ast;

import Tokens.TokenType;

public enum AssignmentOperator {
    EQUALS(TokenType.EQUALS), ADDITION_ASSIGNMENT(TokenType.ADDITION_ASSIGNMENT), SUBTRACTION_ASSIGNMENT(TokenType.SUBTRACTION_ASSIGNMENT);

    private final TokenType token;

    AssignmentOperator(TokenType token) {
        this.token = token;
    }
}
