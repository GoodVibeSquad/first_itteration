package Ast;

public record EInDeCrement(Identifier id, InDeCrement inDeCrement, boolean isPostfix) implements Expression{
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSInDeCrement(this);
    }
}
