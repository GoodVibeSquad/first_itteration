package Compiler.ast;

// Statements
 interface Statement  {
    <R> R accept(AstVisitor<R> visitor);
}

