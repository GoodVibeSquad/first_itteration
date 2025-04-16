package Ast;

import TypeChecking.TypeCheck;

public record Parameter(TypeCheck type, Identifier name) {
}

