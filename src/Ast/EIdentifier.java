package Ast;

public record EIdentifier(Identifier name) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEidentifier(this); }
}

