package Ast;

public record Eidentifier(Identifier name) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEidentifier(this); }
}

