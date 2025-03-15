package Compiler.ast;

import java.util.List;

record Elist(List<Expression> elements) implements Expression {
    //accept metode (vistor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitElist(this); }
}
