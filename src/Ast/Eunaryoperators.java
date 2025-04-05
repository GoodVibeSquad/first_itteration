package Ast;

public record Eunaryoperators(UnaryOperators op, Expression expr) implements Expression {

    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEunaryoperators(this); }
}
