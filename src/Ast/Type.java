package Ast;

import Tokens.TokenType;

public enum Type {
    NEURALNETWORK(TokenType.FUNCCLASS), LAYER(TokenType.FUNCCLASS), ACTIVATIONFUNC(TokenType.FUNCCLASS), ARRAY(TokenType.FUNCCLASS),
    STRING(TokenType.STRING), INT(TokenType.INT), DOUBLE(TokenType.DOUBLE), BOOL(TokenType.BOOL);

    private final TokenType token;

    Type(TokenType token) {
        this.token = token;
    }
}
