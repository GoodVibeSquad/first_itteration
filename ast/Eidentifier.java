package Compiler.ast;

record Eidentifier(Identifier ident) implements Expression {
    //accept metode (visitor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitEidentifier(this); }
}

