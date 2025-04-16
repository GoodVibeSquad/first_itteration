package TypeChecking;

import Ast.*;

import java.util.List;

public class TypeCheckerVisitor implements AstVisitor<TypeCheck> {
    SymbolTable symbolTable;

    public TypeCheckerVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    private int loopDepth = 0;


    // Constants
    @Override
    public TypeCheck visitCNone(CNone c) {
        //wtf?
        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitCBool(CBool c) {

        return TypeCheck.BOOL;

    }

    @Override
    public TypeCheck visitCString(CString c) {
        return TypeCheck.STRING;
    }

    @Override
    public TypeCheck visitCInt(CInt c) {
        return TypeCheck.INT;
    }

    @Override
    public TypeCheck visitCDouble(CDouble c) {
        return TypeCheck.DOUBLE;
    }

    @Override
    public TypeCheck visitCEuler(CEuler c) {
        return TypeCheck.DOUBLE;
    }

    @Override
    public TypeCheck visitCPi(CPi c) {
        return TypeCheck.DOUBLE;
    }

    // Expressions
    @Override
    public TypeCheck visitEconstant(Econstant e) {
        return e.value().accept(this);
    }

    @Override
    public TypeCheck visitEidentifier(Eidentifier e) {
        String name = e.name().getId();

        if (symbolTable.contains(name)) {
            return symbolTable.lookup(name);
        } else {
            System.err.println("Undeclared variable: " + name);
            return TypeCheck.ERROR;
        }

    }

    @Override
    public TypeCheck visitEbinaryoperators(Ebinaryoperators e) {
        TypeCheck leftType = e.left().accept(this);
        TypeCheck rightType = e.right().accept(this);
        BinaryOperators op = e.op();

        switch (op) {
            //arithmetic operators
            case PLUS, MINUS, MULTIPLY, DIVISION, MODULUS, EXPONENT -> {
                if (leftType == TypeCheck.INT && rightType == TypeCheck.INT) {
                    return TypeCheck.INT;
                } else if (isNumeric(leftType) && isNumeric(rightType)) {
                    return TypeCheck.DOUBLE;
                } else {
                    System.err.println("Invalid arithmetic: " + leftType + " " + op + " " + rightType);
                    return TypeCheck.ERROR;
                }
            }
            //comparison operators
            case EQUALS, NOT_EQUALS, GREATER_OR_EQUALS, LESS_OR_EQUALS -> {
                if ((leftType == rightType) || (isNumeric(leftType)) && isNumeric(rightType)) {
                    return TypeCheck.BOOL;
                } else {
                    System.err.println("Invalid comparison: " + leftType + " " + op + " " + rightType);
                    return TypeCheck.ERROR;
                }
            }

            //Logic operators
            case AND, OR -> {
                if (leftType == TypeCheck.BOOL && rightType == TypeCheck.BOOL) {
                    return TypeCheck.BOOL;
                } else {
                    System.err.println("Logical operation requires booleans: " + leftType + " " + op + " " + rightType);
                    return TypeCheck.ERROR;
                }

            }

            default -> {
                System.err.println("Unhandled binary operator: " + op);
                return TypeCheck.ERROR;
            }

        }

    }

    @Override
    public TypeCheck visitEunaryoperators(Eunaryoperators e) {
        TypeCheck type = e.expr().accept(this);
        UnaryOperators op = e.op();

        switch (op) {
            case NEG -> {
                if (type == TypeCheck.INT) {
                    return TypeCheck.INT;
                } else if (type == TypeCheck.DOUBLE) {
                    return TypeCheck.DOUBLE;
                } else {
                    System.err.println("Invalid unary operator: " + op + " " + type);
                    return TypeCheck.ERROR;
                }

            }
            case NOT -> {
                if (type == TypeCheck.BOOL) {
                    return TypeCheck.BOOL;
                } else {
                    System.err.println("Invalid unary operator: " + op + " " + type);
                    return TypeCheck.ERROR;
                }
            }
            default -> {
                System.err.println("Unhandled unary operator: " + op);
                return TypeCheck.ERROR;
            }
        }

    }

    @Override
    public TypeCheck visitEcall(EFuncCall e) {
        String funcName = e.func().getId(); // or .toString() if you've overridden it

        if (!symbolTable.isFunction(funcName)) {
            System.err.println("Undeclared function: " + funcName);
            return TypeCheck.ERROR;
        }

        SymbolTable.FunctionSignature signature = symbolTable.getFunction(funcName);
        List<TypeCheck> expected = signature.paramTypes;
        List<Expression> actualArgs = e.args().elements(); // from the Elist

        if (expected.size() != actualArgs.size()) {
            System.err.println("Function '" + funcName + "' expects " + expected.size() +
                    " arguments, got " + actualArgs.size());
            return TypeCheck.ERROR;
        }

        for (int i = 0; i < expected.size(); i++) {
            TypeCheck actual = actualArgs.get(i).accept(this);
            if (actual != expected.get(i)) {
                System.err.println("Argument " + (i + 1) + " to function '" + funcName +
                        "' has wrong type. Expected " + expected.get(i) +
                        ", got " + actual);
                return TypeCheck.ERROR;
            }
        }

        return signature.returnType;
    }

    @Override
    public TypeCheck visitElist(Elist e) {
        if (e.elements().isEmpty()) {
            return TypeCheck.ERROR;
        }

        TypeCheck firstElement = e.elements().getFirst().accept(this);
        Boolean mixed = false;

        for (Expression ex : e.elements().subList(1, e.elements().size())) {
            TypeCheck element = ex.accept(this);


            if (element != firstElement) {
                mixed = true;
                break;
            }

        }

        return mixed ? TypeCheck.ANY : firstElement;
    }

    @Override
    public TypeCheck visitEternary(Eternary e) {
        TypeCheck condition = e.condition().accept(this);
        if (condition != TypeCheck.BOOL) {
            System.err.println("Terneary condition must be of type BOOL, but got: " + condition);
            return TypeCheck.ERROR;
        }

        TypeCheck trueType = e.trueExpr().accept(this);
        TypeCheck falseType = e.falseExpr().accept(this);

        if (trueType == falseType) {
            return trueType;
        }

        if (isNumeric(trueType) && isNumeric(falseType)) {
            return TypeCheck.DOUBLE;
        }


        System.err.println("Ternary expression branches must have compatible types, but got: " + trueType + " and " + falseType);
        return TypeCheck.ERROR;
    }

    @Override
    public TypeCheck visitESum(ESum e) {
        TypeCheck topType = e.topExpression().accept(this);
        TypeCheck botType = e.bottomExpression().accept(this);

        if (!isNumeric(topType) || !isNumeric(botType)) {
            System.err.println("ESum bounds must be numeric, but got: " + botType + " and " + topType);
            return TypeCheck.ERROR;
        }

        // Declare iteration variable
        String loopVar = e.identifier().getId();
        symbolTable.declareVariable(loopVar, TypeCheck.INT);

        TypeCheck bodyType = e.body().accept(this);

        if (!isNumeric(bodyType)) {
            System.err.println("ESum body must be numeric, but got: " + bodyType);
            return TypeCheck.ERROR;
        }

        return TypeCheck.DOUBLE;
    }

    @Override
    public TypeCheck visitEMax(EMax e) {
        TypeCheck expressionType = e.e().accept(this);

        if (expressionType == TypeCheck.INT || expressionType == TypeCheck.DOUBLE) {
            return expressionType;
        } else {
            System.err.println("Emax expects a numeric expression, but got: " + expressionType);
            return TypeCheck.ERROR;
        }
    }

    @Override
    public TypeCheck visitESqrt(ESqrt e) {
        TypeCheck expressionType = e.expression().accept(this);

        if (expressionType == TypeCheck.INT || expressionType == TypeCheck.DOUBLE) {
            return TypeCheck.DOUBLE;
        } else {
            System.err.println(" expects a numeric expression, but got: " + expressionType);
            return TypeCheck.ERROR;
        }
    }

    @Override
    public TypeCheck visitETypeconversion(ETypeconversion e) {
        Type targetType = e.type();
        TypeCheck actualType = resolveType(targetType);

        TypeCheck expressionType = e.expression().accept(this);

        if(canConvert(expressionType, actualType)){
            return actualType;
        } else {
            System.err.println("Invalid type conversion from " + expressionType + " to " + actualType);
            return TypeCheck.ERROR;
        }

    }

    @Override
    public TypeCheck visitENewFunc(ENewFunc e) {
        TypeCheck expressionType = e.e().accept(this);

        if (expressionType == TypeCheck.ERROR){
            System.err.println("Constructor call inside New Func failed to type check");
        }

        if(!isConstructableType(expressionType)){
            System.err.println("New Func does not result in a constructable object type: " + expressionType);
            return TypeCheck.ERROR;
        }

        return expressionType;
    }


    @Override
    public TypeCheck visitEContainsExpression(EContainsExpression e) {
       return e.expression().accept(this);

    }


    //  @Override

    /// /    public TypeCheck visitEMethodCall(EMethodCall e) {
    /// /        TypeCheck receiverType = e.receiver().accept(this);
    /// /        String methodName = e.method().getId();
    /// /
    /// /        if (!symbolTable.hasMethod(receiverType, methodName)) {
    /// /            System.err.println("No method '" + methodName + "' for type " + receiverType);
    /// /            return TypeCheck.ERROR;
    /// /        }
    /// /
    ///
    ///
    ///

    @Override
    public TypeCheck visitEMethodCall(EMethodCall e) {
        String objectName = e.object().getId();
        TypeCheck objectType;

        if (symbolTable.contains(objectName)) {
            objectType = symbolTable.lookup(objectName);
        } else {
            System.err.println("Undeclared variable: " + objectName);
            return TypeCheck.ERROR;
        }

        SymbolTable.MethodSignature sig = symbolTable.getMethod(objectType, objectName);
        List<Expression> actualArgs = e.args().elements();

        if (sig.paramTypes.size() != actualArgs.size()) {
            System.err.println("Method '" + objectName + "' expects " + sig.paramTypes.size()
                    + " args but got " + actualArgs.size());
            return TypeCheck.ERROR;
        }

        for (int i = 0; i < sig.paramTypes.size(); i++) {
            TypeCheck actual = actualArgs.get(i).accept(this);
            TypeCheck expected = sig.paramTypes.get(i);
            if (actual != expected) {
                System.err.println("Type mismatch in arg " + (i + 1) + ": expected " +
                        expected + ", got " + actual);
                return TypeCheck.ERROR;
            }
        }

        return sig.returnType;
    }

    @Override
    public TypeCheck visitSif(Sif s) {
        TypeCheck conditionType = s.condition().accept(this);
        if (conditionType != TypeCheck.BOOL) {
            System.err.println("Condition of if statement must be BOOL");
            return TypeCheck.ERROR;
        }

        TypeCheck thenType = s.thenBranch().accept(this);
        if (thenType == TypeCheck.ERROR) {
            return TypeCheck.ERROR;
        }

        if (s.elseBranch() != null) {
            TypeCheck elseType = s.elseBranch().accept(this);
            if (elseType == TypeCheck.ERROR) {
                return TypeCheck.ERROR;
            }
        }

        return TypeCheck.VOID;
    }


    @Override
    public TypeCheck visitSassign(Sassign s) {
        String varName = s.var().getId();
        TypeCheck exprType = s.expr().accept(this);

        if (!symbolTable.contains(varName)) {
            System.err.println("Undeclared variable in assignment: " + varName);
            return TypeCheck.ERROR;
        }

        TypeCheck declaredType = symbolTable.lookup(varName);
        if (declaredType != exprType) {
            System.err.println("Type mismatch in assignment to " + varName + ": expected " + declaredType + ", got " + exprType);
            return TypeCheck.ERROR;
        }

        return TypeCheck.VOID;
    }


    @Override
    public TypeCheck visitSprint(Sprint s) {
        TypeCheck exprType = s.expr().accept(this);
        if (exprType == TypeCheck.ERROR) {
            System.err.println("Cannot print Error:" + exprType);
            return TypeCheck.ERROR;
        }

        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSblock(Sblock s) {
        for (Statement stmt : s.stmts()) {
            stmt.accept(this);
        }
        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSfor(Sfor s) {
       // symbolTable.declareVariable(s.var.getId(), TypeCheck.INT); skal den være en int? eller må den også iterate over double??

       // Check the assigment (initialization)
       if (s.assignment() != null ){
           TypeCheck assignmentType = s.assignment().accept(this);
           if (assignmentType == TypeCheck.ERROR) return TypeCheck.ERROR;
       }

       // Check the comparison (must be BOOL)
        if (s.comparison() != null) {
            TypeCheck compType = s.comparison().accept(this);
            if (compType != TypeCheck.BOOL) {
                System.err.println("For-loop comparison must be of tyoe BOOL.");
                return TypeCheck.ERROR;
            }
        }

        // Check the iteration step
        if (s.iterrate() != null) {
            TypeCheck iterateType = s.iterrate().accept(this);
            if (iterateType == TypeCheck.ERROR) {
                System.err.println("iteration must not be error 😒");
                return TypeCheck.ERROR;
            }
        }

        // Check the body type
        loopDepth++;
        if (s.body() != null) {
            TypeCheck bodyType = s.body().accept(this);
            if (bodyType == TypeCheck.ERROR) {
                System.err.println("we discriminate, go to the gym");
                loopDepth--;
                return TypeCheck.ERROR;
            }
        }
        loopDepth--;

    return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSExpression(SExpression s) {
        s.value().accept(this);
        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSWhile(SWhile s) {
        TypeCheck exprType = s.expression().accept(this);
        if (exprType != TypeCheck.BOOL) {
            System.err.println("Condition of while loop must be BOOL");
            return TypeCheck.ERROR;
        }
        loopDepth++;
        TypeCheck body = s.body().accept(this);
        loopDepth--;

        if (body == TypeCheck.ERROR) {
            System.err.println("call customer service please, im unavailable atm");
            return TypeCheck.ERROR;
        }
        return TypeCheck.VOID;
    }
    @Override
    public TypeCheck visitSBreak(SBreak s) {
        if (loopDepth == 0) {
            System.err.println("Error: 'break' used outside of a loop!!");
            return TypeCheck.ERROR;
        }
        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSContinue(SContinue s) {
        if (loopDepth == 0) {
            System.err.println("Error: continue used outside of a loop!!");
            return TypeCheck.ERROR;
        }
        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSlist(Slist s) {
        for (Statement stmt : s.elements()) {
            stmt.accept(this);
        }
        return TypeCheck.VOID;
    }

    // Other (This is chat solution, check correctness)
    @Override
    public TypeCheck visitDef(Def d) {
        List<TypeCheck> paramTypes = d.params().stream()
                .map(Parameter::type)
                .toList();

        symbolTable.declareFunction(d.name().getId(), paramTypes, TypeCheck.VOID);

        symbolTable.enterScope(); // 🔥 enter function scope

        for (Parameter param : d.params()) {
            String paramName = param.name().getId();
            if (symbolTable.contains(paramName)) {
                System.err.println("Parameter " + paramName + " already declared.");
                symbolTable.exitScope();
                return TypeCheck.ERROR;
            }
            symbolTable.declareVariable(paramName, param.type());
        }

        TypeCheck bodyType = d.body().accept(this);

        symbolTable.exitScope(); // 🔥 exit function scope

        if (bodyType == TypeCheck.ERROR) {
            System.err.println("Error in body of function: " + d.name().getId());
            return TypeCheck.ERROR;
        }

        return TypeCheck.VOID;
    }

    /* ===== HELPER FUNCTIONS ===== */
    private boolean isNumeric(TypeCheck t) {
        return t == TypeCheck.INT || t == TypeCheck.DOUBLE;
    }

    private TypeCheck resolveType(Type type) {
        return switch (type) {
            case INT -> TypeCheck.INT;
            case DOUBLE -> TypeCheck.DOUBLE;
            case BOOL -> TypeCheck.BOOL;
            case STRING -> TypeCheck.STRING;
            case NEURALNETWORK -> TypeCheck.NEURALNETWORK;
            case LAYER -> TypeCheck.LAYER;
            case ACTIVATIONFUNC -> TypeCheck.ACTIVATIONFUNC;
            case ARRAY -> TypeCheck.ARRAY;
            default -> TypeCheck.ERROR;
        };

    }

    private boolean canConvert(TypeCheck from, TypeCheck to) {

        if ((from == TypeCheck.INT || from == TypeCheck.DOUBLE) &&
                (to == TypeCheck.INT || to == TypeCheck.DOUBLE)) {
            return true;
        }

        if (to == TypeCheck.STRING) return true;

        return from == to;
    }

    private boolean isConstructableType(TypeCheck type) {
        return switch (type) {
            case NEURALNETWORK, LAYER, ACTIVATIONFUNC -> true;
            default -> false;
        };
    }
}

/*

 */


