package Ast;

import Tokens.Token;

public record ETypeconversion(Token targetType, Expression expression) implements Expression{
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitETypeconversion(this);
    }
}
