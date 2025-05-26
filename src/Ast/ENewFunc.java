package Ast;

public record ENewFunc(Type type, EList e) implements Expression{
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitENewFunc(this);
    }
}
