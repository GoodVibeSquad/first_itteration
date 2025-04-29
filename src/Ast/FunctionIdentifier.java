package Ast;



public record FunctionIdentifier(Identifier name, Identifier var, Elist params) implements Expression {
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitFunctionIdentifier(this);
    }
}