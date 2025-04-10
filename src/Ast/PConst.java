package Ast;

public record PConst(Literals literal) implements Primary{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitPConst(this);
    }
}
