package Ast;

// Constants
sealed public interface Literals permits CNone, CBool, CString, CInt, CDouble, CPi, CEuler{
    <R> R accept(AstVisitor<R> visitor);
}
