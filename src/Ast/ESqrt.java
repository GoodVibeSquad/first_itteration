package Ast;

public record ESqrt(Expression expression) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitESqrt(this);
    }
}
