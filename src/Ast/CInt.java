package Ast;

record CInt(long value) implements Literals {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCInt(this); }
}
