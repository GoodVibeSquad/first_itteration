package Ast;

import java.util.List;

public record Sblock(List<Statement> stmts) implements Statement {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSblock(this); }
}
