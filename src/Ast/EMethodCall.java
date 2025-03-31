package Ast;

record EMethodCall(Identifier func, Identifier method) implements Expression{
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEMethodCall(this);
    }
}
