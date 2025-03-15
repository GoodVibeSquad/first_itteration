package Compiler.ast;

record Sassign(Identifier var, Expression expr) implements Statement {
    //accept metode (vistir)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSassign(this); }
}
