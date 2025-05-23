package Ast;

public record SDeclaration(Identifier var, AssignmentOperator assignmentOperator, Expression expr) implements Statement {

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSDeclaration(this);
    }
}