package Compiler.ast;

import java.util.List;

record File(List<Def> functions, Statement mainBlock) {
    //accept metode (visitor)
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitFile(this); }
}
