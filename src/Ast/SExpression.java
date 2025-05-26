package Ast;

public record SExpression(Expression expr) implements Statement{
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSExpression(this);
    }
}
