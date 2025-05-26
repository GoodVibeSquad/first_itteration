package Ast;

import java.util.Objects;

public record FunctionIdentifier(Identifier typedName, EList params) implements Expression {
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitFunctionIdentifier(this);
    }
}