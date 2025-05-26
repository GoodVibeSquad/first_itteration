package Ast;

import java.util.List;

public record SBlock(SList stmts) implements Statement {
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSblock(this); }
}
