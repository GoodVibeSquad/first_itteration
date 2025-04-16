package TypeChecking;

import Ast.*;

import java.util.List;

//public class TypeCheckerVisitor implements AstVisitor<TypeCheck> {
//    SymbolTable symbolTable;
//
//    public TypeCheckerVisitor(SymbolTable symbolTable) {
//        this.symbolTable = symbolTable;
//    }
//
//
//    // Constants
//    @Override
//    public TypeCheck visitCNone(CNone c) {
//        //wtf?
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitCBool(CBool c) {
//
//        return TypeCheck.BOOL;
//
//    }
//
//    @Override
//    public TypeCheck visitCString(CString c) {
//        return TypeCheck.STRING;
//    }
//
//    @Override
//    public TypeCheck visitCInt(CInt c) {
//        return TypeCheck.INT;
//    }
//
//    @Override
//    public TypeCheck visitCDouble(CDouble c) {
//        return TypeCheck.DOUBLE;
//    }
//
//    @Override
//    public TypeCheck visitCEuler(CEuler c) {
//        return TypeCheck.DOUBLE;
//    }
//
//    @Override
//    public TypeCheck visitCPi(CPi c) {
//        return TypeCheck.DOUBLE;
//    }
//
//    // Expressions
//    @Override
//    public TypeCheck visitEconstant(Econstant e) {
//        return e.value().accept(this);
//    }
//
//    @Override
//    public TypeCheck visitEidentifier(Eidentifier e) {
//        String name = e.name().getId();
//
//        if (symbolTable.contains(name)) {
//            return symbolTable.lookup(name);
//        } else {
//            System.err.println("Undeclared variable: " + name);
//            return TypeCheck.ERROR;
//        }
//
//    }
//
//    @Override
//    public TypeCheck visitEbinaryoperators(Ebinaryoperators e) {
//        TypeCheck leftType = e.left().accept(this);
//        TypeCheck rightType = e.right().accept(this);
//        BinaryOperators op = e.op();
//
//        switch (op) {
//            //arithmetic operators
//            case PLUS, MINUS, MULTIPLY, DIVISION, MODULUS, EXPONENT -> {
//                if (leftType == TypeCheck.INT && rightType == TypeCheck.INT) {
//                    return TypeCheck.INT;
//                } else if (isNumeric(leftType) && isNumeric(rightType)) {
//                    return TypeCheck.DOUBLE;
//                } else {
//                    System.err.println("Invalid arithmetic: " + leftType + " " + op + " " + rightType);
//                    return TypeCheck.ERROR;
//                }
//            }
//            //comparison operators
//            case EQUALS, NOT_EQUALS, GREATER_OR_EQUALS, LESS_OR_EQUALS -> {
//                if ((leftType == rightType) || (isNumeric(leftType)) && isNumeric(rightType)) {
//                    return TypeCheck.BOOL;
//                } else {
//                    System.err.println("Invalid comparison: " + leftType + " " + op + " " + rightType);
//                    return TypeCheck.ERROR;
//                }
//            }
//
//            //Logic operators
//            case AND, OR -> {
//                if (leftType == TypeCheck.BOOL && rightType == TypeCheck.BOOL) {
//                    return TypeCheck.BOOL;
//                } else {
//                    System.err.println("Logical operation requires booleans: " + leftType + " " + op + " " + rightType);
//                    return TypeCheck.ERROR;
//                }
//
//            }
//
//            default -> {
//                System.err.println("Unhandled binary operator: " + op);
//                return TypeCheck.ERROR;
//            }
//
//        }
//
//    }
//
//    @Override
//    public TypeCheck visitEunaryoperators(Eunaryoperators e) {
//        TypeCheck type = e.expr().accept(this);
//        UnaryOperators op = e.op();
//
//        switch (op) {
//            case NEG -> {
//                if (type == TypeCheck.INT) {
//                    return TypeCheck.INT;
//                } else if (type == TypeCheck.DOUBLE) {
//                    return TypeCheck.DOUBLE;
//                } else {
//                    System.err.println("Invalid unary operator: " + op + " " + type);
//                    return TypeCheck.ERROR;
//                }
//
//            }
//            case NOT -> {
//                if (type == TypeCheck.BOOL) {
//                    return TypeCheck.BOOL;
//                } else {
//                    System.err.println("Invalid unary operator: " + op + " " + type);
//                    return TypeCheck.ERROR;
//                }
//            }
//            default -> {
//                System.err.println("Unhandled unary operator: " + op);
//                return TypeCheck.ERROR;
//            }
//        }
//
//    }
//
//    @Override
//    public TypeCheck visitEcall(EFuncCall e) {
//        String funcName = e.func().getId(); // or .toString() if you've overridden it
//
//        if (!symbolTable.isFunction(funcName)) {
//            System.err.println("Undeclared function: " + funcName);
//            return TypeCheck.ERROR;
//        }
//
//        SymbolTable.FunctionSignature signature = symbolTable.getFunction(funcName);
//        List<TypeCheck> expected = signature.paramTypes;
//        List<Expression> actualArgs = e.args().elements(); // from the Elist
//
//        if (expected.size() != actualArgs.size()) {
//            System.err.println("Function '" + funcName + "' expects " + expected.size() +
//                    " arguments, got " + actualArgs.size());
//            return TypeCheck.ERROR;
//        }
//
//        for (int i = 0; i < expected.size(); i++) {
//            TypeCheck actual = actualArgs.get(i).accept(this);
//            if (actual != expected.get(i)) {
//                System.err.println("Argument " + (i + 1) + " to function '" + funcName +
//                        "' has wrong type. Expected " + expected.get(i) +
//                        ", got " + actual);
//                return TypeCheck.ERROR;
//            }
//        }
//
//        return signature.returnType;
//    }
//
//    @Override
//    public TypeCheck visitElist(Elist e) {
//        if (e.elements().isEmpty()) {
//            return TypeCheck.ERROR;
//        }
//
//        TypeCheck firstElement = e.elements().getFirst().accept(this);
//        Boolean mixed = false;
//
//        for (Expression ex : e.elements().subList(1, e.elements().size())) {
//            TypeCheck element = ex.accept(this);
//
//
//            if (element != firstElement) {
//                mixed = true;
//                break;
//            }
//
//        }
//
//        return mixed ? TypeCheck.ANY : firstElement;
//    }
//
//    @Override
//    public TypeCheck visitEternary(Eternary e) {
//        TypeCheck condition = e.condition().accept(this);
//        if (condition != TypeCheck.BOOL) {
//            System.err.println("Terneary condition must be of type BOOL, but got: " + condition);
//            return TypeCheck.ERROR;
//        }
//
//        TypeCheck trueType = e.trueExpr().accept(this);
//        TypeCheck falseType = e.falseExpr().accept(this);
//
//        if (trueType == falseType) {
//            return trueType;
//        }
//
//        if (isNumeric(trueType) && isNumeric(falseType)) {
//            return TypeCheck.DOUBLE;
//        }
//
//
//        System.err.println("Ternary expression branches must have compatible types, but got: " + trueType + " and " + falseType);
//        return TypeCheck.ERROR;
//    }
//
//    @Override
//    public TypeCheck visitESum(ESum e) {
//        TypeCheck topType = e.topExpression().accept(this);
//        TypeCheck botType = e.bottomExpression().accept(this);
//
//        if (!isNumeric(topType) || !isNumeric(botType)) {
//            System.err.println("ESum bounds must be numeric, but got: " + botType + " and " + topType);
//            return TypeCheck.ERROR;
//        }
//
//        // Declare iteration variable
//        String loopVar = e.identifier().getId();
//        symbolTable.declareVariable(loopVar, TypeCheck.INT);
//
//        TypeCheck bodyType = e.body().accept(this);
//
//        if (!isNumeric(bodyType)) {
//            System.err.println("ESum body must be numeric, but got: " + bodyType);
//            return TypeCheck.ERROR;
//        }
//
//        return TypeCheck.DOUBLE;
//    }
//
//    @Override
//    public TypeCheck visitEMax(EMax e) {
//        TypeCheck expressionType = e.e().accept(this);
//
//        if (expressionType == TypeCheck.INT || expressionType == TypeCheck.DOUBLE) {
//            return expressionType;
//        } else {
//            System.err.println("Emax expects a numeric expression, but got: " + expressionType);
//            return TypeCheck.ERROR;
//        }
//    }
//
//    @Override
//    public TypeCheck visitESqrt(ESqrt e) {
//        TypeCheck expressionType = e.expression().accept(this);
//
//        if (expressionType == TypeCheck.INT || expressionType == TypeCheck.DOUBLE) {
//            return TypeCheck.DOUBLE;
//        } else {
//            System.err.println(" expects a numeric expression, but got: " + expressionType);
//            return TypeCheck.ERROR;
//        }
//    }
//
//    @Override
//    public TypeCheck visitETypeconversion(ETypeconversion e) {
//        Type targetType = e.type();
//        TypeCheck actualType = resolveType(targetType);
//
//        TypeCheck expressionType = e.expression().accept(this);
//
//        if(canConvert(expressionType, actualType)){
//            return actualType;
//        } else {
//            System.err.println("Invalid type conversion from " + expressionType + " to " + actualType);
//            return TypeCheck.ERROR;
//        }
//
//    }
//
//    @Override
//    public TypeCheck visitENewFunc(ENewFunc e) {
//        TypeCheck expressionType = e.e().accept(this);
//
//        if (expressionType == TypeCheck.ERROR){
//            System.err.println("Constructor call inside New Func failed to type check");
//        }
//
//        if(!isConstructableType(expressionType)){
//            System.err.println("New Func does not result in a constructable object type: " + expressionType);
//            return TypeCheck.ERROR;
//        }
//
//        return expressionType;
//    }
//
//
//    @Override
//    public TypeCheck visitEContainsExpression(EContainsExpression e) {
//       return e.expression().accept(this);
//
//    }
//
//    @Override
//    public TypeCheck visitEMethodCall(EMethodCall e) {
//        TypeCheck receiverType = e.receiver().accept(this);
//        String methodName = e.method().getId();
//
//        if (!symbolTable.hasMethod(receiverType, methodName)) {
//            System.err.println("No method '" + methodName + "' for type " + receiverType);
//            return TypeCheck.ERROR;
//        }
//
//        SymbolTable.MethodSignature sig = symbolTable.getMethod(receiverType, methodName);
//        List<Expression> actualArgs = e.args().elements();
//
//        if (sig.paramTypes.size() != actualArgs.size()) {
//            System.err.println("Method '" + methodName + "' expects " + sig.paramTypes.size()
//                    + " args but got " + actualArgs.size());
//            return TypeCheck.ERROR;
//        }
//
//        for (int i = 0; i < sig.paramTypes.size(); i++) {
//            TypeCheck actual = actualArgs.get(i).accept(this);
//            TypeCheck expected = sig.paramTypes.get(i);
//            if (actual != expected) {
//                System.err.println("Type mismatch in arg " + (i + 1) + ": expected " +
//                        expected + ", got " + actual);
//                return TypeCheck.ERROR;
//            }
//        }
//
//        return sig.returnType;
//    }
//
//    // Statements
//    @Override
//    public TypeCheck visitSif(Sif s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSassign(Sassign s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSprint(Sprint s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSblock(Sblock s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSfor(Sfor s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSExpression(SExpression s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSWhile(SWhile s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSBreak(SBreak s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSContinue(SContinue s) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitSlist(Slist slist) {
//        return TypeCheck.VOID;
//    }
//
//    // Other
//    @Override
//    public TypeCheck visitDef(Def d) {
//        return TypeCheck.VOID;
//    }
//
//    @Override
//    public TypeCheck visitFile(File f) {
//        return TypeCheck.VOID;
//    }
//
//
//    // Helper functions
//    private boolean isNumeric(TypeCheck t) {
//        return t == TypeCheck.INT || t == TypeCheck.DOUBLE;
//    }
//
////    private TypeCheck resolveType(Type type) {
////        return switch (type) {
////            case INT -> TypeCheck.INT;
////            case DOUBLE -> TypeCheck.DOUBLE;
////            case BOOL -> TypeCheck.BOOL;
////            case STRING -> TypeCheck.STRING;
////            case NEURALNETWORK -> TypeCheck.NEURALNETWORK;
////            case LAYER -> TypeCheck.LAYER;
////            case ACTIVATIONFUNC -> TypeCheck.ACTIVATIONFUNC;
////            case ARRAY -> TypeCheck.ARRAY;
////            default -> TypeCheck.ERROR;
////        };
////
////    }
//
//
//    private boolean canConvert(TypeCheck from, TypeCheck to) {
//
//        if ((from == TypeCheck.INT || from == TypeCheck.DOUBLE) &&
//                (to == TypeCheck.INT || to == TypeCheck.DOUBLE)) {
//            return true;
//        }
//
//
//        if (to == TypeCheck.STRING) return true;
//
//
//        return from == to;
//    }
//
//    private boolean isConstructableType(TypeCheck type) {
//        return switch (type) {
//            case NEURALNETWORK, LAYER, ACTIVATIONFUNC -> true;
//            default -> false;
//        };
//    }
//}


/*

 */


