package Ast;

record EFuncCall(Identifier func) implements Expression {
    //accept metode (vizitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEcall(this); }
}
