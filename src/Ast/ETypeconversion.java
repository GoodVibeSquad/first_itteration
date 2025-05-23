package Ast;

import Tokens.Token;

public record ETypeconversion(Token type, Expression expression) implements Expression{

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitETypeconversion(this);
    }
}
