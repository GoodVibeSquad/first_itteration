package Ast;

// Expressions
 sealed public interface  Expression permits Econstant, Ebinaryoperators, Eunaryoperators, EFuncCall, Elist, Eternary, ESum, EMax, ESqrt, ETypeconversion, ENewFunc, EMethodCall {
    <R> R accept(AstVisitor<R> visitor);
}
