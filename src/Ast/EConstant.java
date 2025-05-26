package Ast;

public record EConstant(Literals value) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEconstant(this); }
}
