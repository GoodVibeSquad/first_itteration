package Ast;

record Econstant(Literals value) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEconstant(this); }
}
