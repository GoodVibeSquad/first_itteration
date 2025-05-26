package Ast;

public record Sprint(Expression expr) implements Statement {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSprint(this); }
}
