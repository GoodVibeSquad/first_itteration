package Ast;

public record CEuler() implements Literals{

    @Override
    public <R> R accept(AstVisitor<R> visitor)
    {
        return visitor.visitCEuler(this);
    }
}
