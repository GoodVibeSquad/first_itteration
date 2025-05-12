package Ast;

public record SReturn(Expression expr) implements  Statement{
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSReturn(this); }
}
