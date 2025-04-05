package Ast;

public record CPi(double value) implements Literals{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor)
    {
        return visitor.visitCPi(this);
    }
}
