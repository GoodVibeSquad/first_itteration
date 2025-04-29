package Ast;

// Expressions
 sealed public interface  Expression permits EContainsExpression, EFuncCall, EMax, EMethodCall, ENewFunc, ESqrt, ESum, ETypeconversion, Ebinaryoperators, Econstant, Eidentifier, Elist, Eternary, Eunaryoperators, FunctionIdentifier {
    <R> R accept(AstVisitor<R> visitor);
}
