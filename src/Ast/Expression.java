package Ast;

// Expressions
sealed public interface  Expression permits Econstant, Eidentifier, Ebinaryoperators, Eunaryoperators, EFuncCall, Elist, Eternary, ESum, EMax, ESqrt, ETypeconversion, ENewFunc, EContainsExpression, EMethodCall, FunctionIdentifier {
    <R> R accept(AstVisitor<R> visitor);
}