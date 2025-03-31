package Ast;

record CDouble(double value) implements Literals {
   //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCDouble(this); }
}
