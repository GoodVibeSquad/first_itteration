package Ast;

public enum funcClass {
    NERUALNETWORK(Type.NERUALNETWORK), LAYER(Type.LAYER), ACTIVATIONFUNC(Type.ACTIVATIONFUNC), ARRAY(Type.ARRAY);

    private final Type token;

    funcClass(Type token) {
        this.token = token;
    }
}
