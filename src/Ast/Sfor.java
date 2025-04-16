package Ast;

public record Sfor(Identifier var, Sassign assignment, Expression comparison, Statement iterrate, Statement body) implements Statement {
    // accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSfor(this); }
}
