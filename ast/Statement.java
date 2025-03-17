package Compiler.ast;

// Statements
 sealed interface Statement permits Sif, Sreturn, Sassign, Sprint, Sblock, Sfor, Seval, Sset {
    <R> R accept(AstVisitor<R> visitor);
}

