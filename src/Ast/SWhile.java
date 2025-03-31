package Ast;

public record SWhile(Expression expression, Statement body) implements Statement {
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSWhile(this);
    }
}
