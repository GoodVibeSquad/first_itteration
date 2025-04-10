package Ast;

public record ETailNone() implements ExpressionTail{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitETailNone(this); }
}
