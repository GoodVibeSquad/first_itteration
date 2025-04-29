package Ast;

public record ESum(Expression topExpression, Expression bottomExpression, Identifier identifier, Expression body) implements Expression{

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitESum(this);
    }
}
