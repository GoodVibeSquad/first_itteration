
package Ast;

import Tokens.TokenType;

public enum Type {
    NEURALNETWORK(TokenType.TYPE, "NeuralNetwork"),
    LAYER(TokenType.TYPE, "Layer"),
    ACTIVATIONFUNC(TokenType.TYPE, "ActivationFunc"),
    ARRAY(TokenType.TYPE, "Array"),
    STRING(TokenType.STRING, "String"),
    //Hvorfor er int, double og bool her?
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

    public static Type fromTokenType(TokenType tokenType) {
        for (Type type : Type.values()) {
            if (type.token == tokenType) {
                return type;
            }
        }
        return null;
    }
}

