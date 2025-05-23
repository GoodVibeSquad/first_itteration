package Ast;

public record CDouble(double value) implements Literals {

    @Override
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCDouble(this); }
}
