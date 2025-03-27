package Ast;

// Constants
interface Literals {
    <R> R accept(AstVisitor<R> visitor);
}
