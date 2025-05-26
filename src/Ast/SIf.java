package Ast;

public record SIf(Expression condition, Statement thenBranch, Statement elseBranch) implements Statement {
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSif(this); }
}
