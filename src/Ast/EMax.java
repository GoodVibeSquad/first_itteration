package Ast;

record EMax(Expression e) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEMax(this);
    }
}
