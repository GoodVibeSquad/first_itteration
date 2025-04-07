package Ast;

public record ESum(Expression topExpression, Expression bottomExpression, Identifier identifier) implements Expression{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitESum(this);
    }
}
