package Ast;

public record Sfor(Identifier var, Statement init , Expression condition, Statement update, Statement body) implements Statement {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSfor(this); }
}
