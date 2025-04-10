package Ast;

public record EtailBPEtail(BinaryOperators binaryOperators, Primary primary, ExpressionTail expressionTail) implements ExpressionTail{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEtailBPEtail(this);
    }
}
