package Ast;

import Tokens.TokenType;

public enum Type {
    NEURALNETWORK(TokenType.TYPE, "NeuralNetwork"),
    LAYER(TokenType.TYPE, "Layer"),
    ACTIVATIONFUNCTION(TokenType.TYPE, "ActivationFunction"),
    ARRAY(TokenType.TYPE, "Array"),
    STRING(TokenType.STRING, "String"),
    INT(TokenType.INT, "int"),
    DOUBLE(TokenType.DOUBLE, "double"),
    BOOL(TokenType.BOOL, "boolean");

    private final TokenType token;
    private final String typeName;

    Type(TokenType token, String typeName) {
        this.token = token;
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

}