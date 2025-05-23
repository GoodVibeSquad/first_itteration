package Ast;

public record CString(String value) implements Literals {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCString(this); }
}
