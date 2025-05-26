package Ast;

import java.util.List;

public record Elist(List<Expression> elements) implements Expression {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitElist(this); }
}
