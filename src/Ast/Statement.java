package Ast;

// Statements
 sealed interface Statement permits Sif, Sassign, Sprint, Sblock, Sfor , SExpression, SWhile, SBreak, SContinue{
    <R> R accept(AstVisitor<R> visitor);
}

