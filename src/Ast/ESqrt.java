package Ast;

record ESqrt() implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitESqrt(this);
    }
}
