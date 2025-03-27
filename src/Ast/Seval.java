package Ast;

record Seval(Expression expr) implements Statement {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSeval(this); }
}
