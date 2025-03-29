package Ast;

// Expressions
 sealed interface  Expression permits Econstant, Eidentifier, Ebinaryoperators, Eunaryoperators, EFuncCall, Elist, Eternary, ESum, EMax, ESqrt, ETypeconversion, ENewFunc, EExpression, EMethodCall {
    <R> R accept(AstVisitor<R> visitor);
}
