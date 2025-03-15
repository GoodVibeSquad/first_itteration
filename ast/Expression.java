package Compiler.ast;

// Expressions
 interface  Expression {
    <R> R accept(AstVisitor<R> visitor);
}
