package Compiler.ast;

record Eget(Expression list, Expression index) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEget(this); }
}
