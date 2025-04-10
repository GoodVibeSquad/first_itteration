package Ast;

public sealed interface Primary permits PConst, PIdentifier, PParenthesis{
    <R> R accept(AstVisitor<R> visitor);
}
