package Ast;

public record CPi() implements Literals{

    @Override
    public <R> R accept(AstVisitor<R> visitor)
    {
        return visitor.visitCPi(this);
    }
}
