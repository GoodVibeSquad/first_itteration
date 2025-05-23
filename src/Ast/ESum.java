package Ast;

public record ESum(Identifier index, Expression bottomExpression, Expression topExpression, Expression body) implements Expression{

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitESum(this);
    }
}
