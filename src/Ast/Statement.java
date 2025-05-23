package Ast;

// Statements
sealed public interface Statement
        permits SBreak, SContinue, SDeclaration, SExpression, SFunction, SInDeCrement, SWhile, SAssign, SBlock, SFor, SIf, SList, SPrint, SReturn
{
    <R> R accept(AstVisitor<R> visitor);
}