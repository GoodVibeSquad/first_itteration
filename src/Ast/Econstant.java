package Ast;

public record Econstant(Literals value) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEconstant(this); }
}
