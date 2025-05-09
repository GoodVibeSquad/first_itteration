package Ast;

public record EMax(Elist e) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEMax(this);
    }
}
