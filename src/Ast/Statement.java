package Ast;

// Statements
 sealed public interface Statement permits SBreak, SContinue, SExpression, SWhile, Sassign, Sblock, Sfor, Sif, Slist, Sprint, EInDeCrement, SDeclaration {
    <R> R accept(AstVisitor<R> visitor);
}

