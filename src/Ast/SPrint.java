package Ast;

public record SPrint(Expression expr) implements Statement {
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSprint(this); }
}
