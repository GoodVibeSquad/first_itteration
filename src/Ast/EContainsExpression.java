package Ast;

public record EContainsExpression(Expression expression) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEContainsExpression(this);
    }
}
