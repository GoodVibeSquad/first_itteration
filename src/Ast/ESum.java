package Ast;

record ESum(Expression topExpression, Expression buttomExpression, Identifier identifier) implements Expression{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitESum(this);
    }
}
