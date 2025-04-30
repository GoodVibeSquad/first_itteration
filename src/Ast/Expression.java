package Ast;

// Expressions
 sealed public interface  Expression permits EContainsExpression, EFuncCall, EInDeCrement, EMax, EMethodCall, ENewFunc, ESqrt, ESum, ETypeconversion, Ebinaryoperators, Econstant, Eidentifier, Elist, Eternary, Eunaryoperators {
    <R> R accept(AstVisitor<R> visitor);
}
