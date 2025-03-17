package Compiler.ast;

public record Eternary(Expression condition, Expression trueExpr, Expression falseExpr) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEternary(this); }
}
