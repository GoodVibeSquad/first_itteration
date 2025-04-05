package Ast;

public record SExpression(Expression value) implements Statement{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSExpression(this);
    }
}
