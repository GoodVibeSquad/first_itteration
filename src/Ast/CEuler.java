package Ast;

public record CEuler() implements Literals{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor)
    {
        return visitor.visitCEuler(this);
    }
}
