package Compiler.ast;

import java.util.List;

// Function definition
record Def(Identifier name, List<Identifier> params, Statement body) {
    //accept metode /visitor)
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitDef(this); }
}
