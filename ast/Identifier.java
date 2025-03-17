package Compiler.ast;

class Identifier {
    Location loc;
    String id;

    public Identifier(Location loc, String id) {
        this.loc = loc;
        this.id = id;
    }
}
