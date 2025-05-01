package Ast;

public record SDeclaration(Identifier var, AssignmentOperator assignmentOperator, Expression expr) implements Statement {
    //accept metode (vistor)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSDeclaration(this);
    }
}