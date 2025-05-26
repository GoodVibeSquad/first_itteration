package Ast;

public record EBinaryoperators(BinaryOperators op, Expression left, Expression right) implements Expression {
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEbinaryoperators(this); }
}
