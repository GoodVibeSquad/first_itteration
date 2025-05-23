package Ast;

public record EFuncCall(Identifier func, EList args) implements Expression {
    //accept metode (vizitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEcall(this); }
}
