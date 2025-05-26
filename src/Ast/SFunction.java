package Ast;

public record SFunction(FunctionIdentifier functionIdentifier, Statement body) implements Statement {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSFunction(this); }
}