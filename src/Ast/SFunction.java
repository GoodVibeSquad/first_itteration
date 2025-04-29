package Ast;


public record SFunction(FunctionIdentifier functionIdentifier, Slist body) implements Statement {
    // accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSFunction(this); }
}