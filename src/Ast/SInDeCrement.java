package Ast;

public record SInDeCrement(Identifier identifier, InDeCrement inDeCrement) implements Statement{
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSInDeCrement(this);
    }
}
