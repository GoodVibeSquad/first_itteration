package Ast;

// Expressions
sealed public interface  Expression permits EConstant, EIdentifier, EBinaryoperators, EUnaryoperators, EFuncCall, EList, ETernary, ESum, EMax, ESqrt, ETypeconversion, ENewFunc, EContainsExpression, EMethodCall, FunctionIdentifier {
    <R> R accept(AstVisitor<R> visitor);
}