package Ast;

import java.util.List;

public record Slist(List<Statement> elements) implements Statement {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitSlist(this); }
}
