package Ast;

public record CInt(long value) implements Literals {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCInt(this); }
}
