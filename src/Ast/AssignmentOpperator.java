package Ast;

import Tokens.TokenType;

public enum AssignmentOpperator {
    EQUALS(TokenType.EQUALS), ADDITION_ASSIGNMENT(TokenType.ADDITION_ASSIGNMENT), SUBTRACTION_ASSIGNMENT(TokenType.SUBTRACTION_ASSIGNMENT);

    private final TokenType token;

    AssignmentOpperator(TokenType token) {
        this.token = token;
    }
}
