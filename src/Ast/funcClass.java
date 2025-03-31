package Ast;

public enum funcClass {
    NEURALNETWORK(Type.NEURALNETWORK), LAYER(Type.LAYER), ACTIVATIONFUNC(Type.ACTIVATIONFUNC), ARRAY(Type.ARRAY);

    private final Type token;

    funcClass(Type token) {
        this.token = token;
    }
}
