package Compiler.ast;

// Constants
interface Literals {
    <R> R accept(AstVisitor<R> visitor);
}
