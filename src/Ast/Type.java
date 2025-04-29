package Ast;

import Tokens.TokenType;

public enum Type {
    NEURALNETWORK(TokenType.TYPE), LAYER(TokenType.TYPE), ACTIVATIONFUNC(TokenType.TYPE), ARRAY(TokenType.TYPE),
    STRING(TokenType.STRING), INT(TokenType.INT), DOUBLE(TokenType.DOUBLE), BOOL(TokenType.BOOL);

    private final TokenType token;

    Type(TokenType token) {
        this.token = token;
    }
}