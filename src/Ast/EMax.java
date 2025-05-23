package Ast;

public record EMax(Elist e) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEMax(this);
    }
}
