package Ast;

import java.util.List;

public record Sblock(Slist stmts) implements Statement {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSblock(this); }
}
