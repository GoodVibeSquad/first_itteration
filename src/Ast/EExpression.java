package Ast;

record EExpression(Expression expression) implements Expression {
    //accept metode (vizitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEExpression(this);
    }
}
