package Ast;

public record SAssign(Identifier name, AssignmentOperator assignmentOperator, Expression expr) implements Statement {
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSassign(this);
    }
}
