package Ast;

import Tokens.TokenType;

public enum Type {
    NERUALNETWORK(TokenType.NEURALNETWORK), LAYER(TokenType.LAYER), ACTIVATIONFUNC(TokenType.ACTIVATIONFUNCTION), ARRAY(TokenType.ARRAY),
    STRING(TokenType.STRING), INT(TokenType.INT), DOUBLE(TokenType.DOUBLE), BOOL(TokenType.BOOL);

    private final TokenType token;

    Type(TokenType token) {
        this.token = token;
    }
}
