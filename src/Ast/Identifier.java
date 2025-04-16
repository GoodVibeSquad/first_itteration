package Ast;

public class Identifier {
    String id;
    String type;


    public Identifier( String id) {
        this.id = id;
        this.type = null;
    }

    public Identifier(String id, String type){
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType(){
        return type;
    }
}
