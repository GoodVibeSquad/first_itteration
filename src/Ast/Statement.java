package Ast;

// Statements
 sealed public interface Statement permits SBreak, SContinue, SExpression, SFunction, SInDeCrement, SWhile, Sassign, Sblock, Sfor, Sif, Slist, Sprint {
    <R> R accept(AstVisitor<R> visitor);
}

