package Ast;

sealed public interface ExpressionTail permits ETailNone, EtailBPEtail{
    <R> R accept(AstVisitor<R> visitor);
}
