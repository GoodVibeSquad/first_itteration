package Ast;

public record ETernary(Expression condition, Expression thenBranch, Expression elseBranch) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEternary(this); }
}
