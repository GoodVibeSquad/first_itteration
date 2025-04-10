package Ast;

public record PIdentifier(Identifier identifier) implements Primary {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitPIdentifier(this);
    }
}
