package Ast;

record SExpression() implements Statement{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSExpression(this);
    }
}
