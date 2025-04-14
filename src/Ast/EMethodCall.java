package Ast;

/*
receiver: the object you're calling the method on (could be an Eidentifier, EFuncCall, etc.)

method: the method name (like "train")

args: list of argument expressions
 */
public record EMethodCall(Expression receiver, Identifier method, Elist args) implements Expression {
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitEMethodCall(this);
    }
}
