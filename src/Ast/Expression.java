package Ast;

// Expressions
 sealed interface  Expression permits Econstant, Eidentifier, Ebinaryoperators, Eunaryoperators, Ecall, Elist, Eget, Eternary {
    <R> R accept(AstVisitor<R> visitor);
}
