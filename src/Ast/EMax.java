package Ast;

public record EMax(EList args) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEMax(this);
    }
}
