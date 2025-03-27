package Ast;


record Sset(Expression list, Expression index, Expression value) implements Statement {
   //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSset(this); }
}
