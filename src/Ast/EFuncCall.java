package Ast;

public record EFuncCall(Identifier func, Elist args) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEcall(this); }
}
