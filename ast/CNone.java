package Compiler.ast;

record CNone() implements Literals {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitCNone(this);
    }
}
