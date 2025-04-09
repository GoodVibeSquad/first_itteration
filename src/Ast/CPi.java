package Ast;

public record CPi() implements Literals{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor)
    {
        return visitor.visitCPi(this);
    }
}
