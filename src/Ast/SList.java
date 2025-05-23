package Ast;

import java.util.List;

public record SList(List<Statement> elements) implements Statement {
    //accept metode (vistor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSlist(this); }
}
