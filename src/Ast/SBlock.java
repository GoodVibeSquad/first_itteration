package Ast;

public record SBlock(SList stmts) implements Statement {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSblock(this); }
}
