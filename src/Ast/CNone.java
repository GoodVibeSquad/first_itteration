package Ast;

public record CNone() implements Literals {

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitCNone(this);
    }
}
