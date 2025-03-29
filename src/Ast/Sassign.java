package Ast;

record Sassign(Identifier var, AssignmentOpperator assignmentOpperator,Expression expr) implements Statement {
    //accept metode (vistir)
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visitSassign(this);
    }
}
