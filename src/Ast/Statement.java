package Ast;

// Statements
 sealed public interface Statement permits SBreak, SContinue, SExpression, SWhile, Sassign, Sblock, Sfor, Sif, Slist, Sprint, SInDeCrement {
    <R> R accept(AstVisitor<R> visitor);
}

