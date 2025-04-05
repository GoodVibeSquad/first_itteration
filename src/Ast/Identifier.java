package Ast;

public class Identifier {
    Location loc;
    String id;
    Type type;

    public Identifier(Location loc, String id) {
        this.loc = loc;
        this.id = id;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
