package Ast;

public record ENewFunc(Elist e) implements Expression{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitENewFunc(this);
    }
}
