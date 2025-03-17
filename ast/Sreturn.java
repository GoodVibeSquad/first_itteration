package Compiler.ast;

record Sreturn(Expression expr) implements Statement {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSreturn(this); }
}

