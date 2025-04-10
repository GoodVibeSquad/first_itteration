package Ast;

public record PParenthesis(Expression expression) implements Primary{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitPParenthesis(this);
    }
}
