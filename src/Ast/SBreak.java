package Ast;

record SBreak() implements Statement{

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSBreak(this);
    }
}
