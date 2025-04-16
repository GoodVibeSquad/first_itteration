package Ast;

import java.util.List;

// Function definition
public record Def(Identifier name, List<Parameter> params, Statement body) {
    //accept metode /visitor)
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitDef(this); }
}
