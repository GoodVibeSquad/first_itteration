package Ast;

import Tokens.TokenType;

public enum AssignmentOperator {
    ASSIGN(TokenType.ASSIGN),
    ADD_ASSIGN(TokenType.ADD_ASSIGN),
    SUB_ASSIGN(TokenType.SUB_ASSIGN),
    DIV_ASSIGN(TokenType.DIV_ASSIGN),
    MUL_ASSIGN(TokenType.MUL_ASSIGN),
    MOD_ASSIGN(TokenType.MOD_ASSIGN);

    private final TokenType token;

    AssignmentOperator(TokenType token) {
        this.token = token;
    }
}
