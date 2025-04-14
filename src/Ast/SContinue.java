package Ast;

public record SContinue() implements Statement{

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSContinue(this);
    }
}
