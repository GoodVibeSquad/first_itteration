package Ast;

public record Eunaryoperators(UnaryOperators op, Expression expr) implements Expression {


    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEunaryoperators(this); }
}
