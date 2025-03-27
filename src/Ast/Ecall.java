package Ast;

import java.util.List;

record Ecall(Identifier func, List<Expression> args) implements Expression {
    //accept metode (vizitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEcall(this); }
}
