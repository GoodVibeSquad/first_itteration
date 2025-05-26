package TypeChecking;

import Ast.*;

import java.util.ArrayList;
import java.util.List;

public class TypeCheckerVisitor implements AstVisitor<TypeCheck> {
    SymbolTable symbolTable;

    public TypeCheckerVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    private int loopDepth = 0;

    //for sfunction + sreturn
    private TypeCheck currentExpectedReturnType = null;

    // Constants
    @Override
    public TypeCheck visitCNone(CNone c) {

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
                    System.err.println("Invalid arithmetic: " + leftType.toString() + " " + op.toSymbol() + " " + rightType);
                    return TypeCheck.ERROR;
                }
            }
            //comparison operators
            case EQUALS, NOT_EQUALS  -> {
                if (leftType == rightType) {
                    return TypeCheck.BOOL;
                } else {
                    System.err.println("Invalid comparison: " + leftType.name() + " " + op.toSymbol() + " " + rightType);
                    return TypeCheck.ERROR;
                }
            }

            case GREATER_OR_EQUALS, LESS_OR_EQUALS, LESS_THAN, GREATER_THAN -> {
                if((isNumeric(leftType)) && isNumeric(rightType)) {
                    return TypeCheck.BOOL;
                }
                else {
                    System.err.println("Invalid comparison: " + leftType.toString() + " " + op.toSymbol() + " " + rightType);
                    return TypeCheck.ERROR;
                }
            }

            //Logic operators
            case AND, OR -> {
                if (leftType == TypeCheck.BOOL && rightType == TypeCheck.BOOL) {
                    return TypeCheck.BOOL;
                } else {
                    System.err.println("Invalid logic: " + leftType + " " + op + " " + rightType);
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

        System.err.println("Ternary expression must have same type in both branches, but got: " + trueType + " and " + falseType);
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

        /* SHOULD RETURN INT OR DOUBLE */
        if (topType == TypeCheck.INT && botType == TypeCheck.INT && bodyType == TypeCheck.INT) {
            return TypeCheck.INT;
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
        String targetType = e.type().getValue();
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
        String typeName = e.type().getTypeName();
        TypeCheck type = resolveType(typeName);


        if (!symbolTable.hasType(typeName)) {
            System.err.println("Type '" + typeName + "' is not a valid type.");
            return TypeCheck.ERROR;
        }

        // Get argument expressions and resolve their types
        List<TypeCheck> argTypes = new ArrayList<>();
        for (Expression argExpr : e.e().elements()) {
            TypeCheck argType = argExpr.accept(this);
            argTypes.add(argType);
        }

        // Try to match against any declared constructor
        for (List<TypeCheck> expectedTypes : symbolTable.getAllConstructors(type)) {
            if (expectedTypes.size() != argTypes.size()) continue;

            boolean match = true;
            for (int i = 0; i < expectedTypes.size(); i++) {
                if (expectedTypes.get(i) != argTypes.get(i)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return type;
            }
        }

        System.err.println("No matching constructor for type '" + typeName +
                "' with argument types: " + argTypes);
        return TypeCheck.ERROR;
    }


    @Override
    public TypeCheck visitEContainsExpression(EContainsExpression e) {
       return e.expression().accept(this);

    }


    @Override
    public TypeCheck visitEMethodCall(EMethodCall e) {
        String objectName = e.object().getId(); //nn
        String methodName = e.method().getId(); //native neural network method fx train
        TypeCheck objectType;

        if (symbolTable.contains(objectName)) {
            objectType = symbolTable.lookup(objectName);
        } else {
            System.err.println("Undeclared variable: " + objectName);
            return TypeCheck.ERROR;
        }

        SymbolTable.MethodSignature sig = symbolTable.getMethod(objectType, methodName);
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
        symbolTable.enterScope();
        TypeCheck thenType = s.thenBranch().accept(this);
        symbolTable.exitScope();
        if (thenType == TypeCheck.ERROR) {
            System.err.println("Error in then branch of if statement");
            return TypeCheck.ERROR;
        }

        if (s.elseBranch() != null) {
            symbolTable.enterScope();
            TypeCheck elseType = s.elseBranch().accept(this);
            symbolTable.exitScope();
            if (elseType == TypeCheck.ERROR) {
                System.err.println("Error in else branch of if statement");
                return TypeCheck.ERROR;
            }
        }

        return TypeCheck.VOID;
    }


    @Override
    public TypeCheck visitSDeclaration(SDeclaration s) {
        String varName = s.var().getId();
        TypeCheck varType = resolveType(s.var().getType());

        if (symbolTable.declaredInScope(varName)) {
            System.err.println("Variable '" + varName + "' already declared in this scope");
            return TypeCheck.ERROR;
        }

        // Handle uninitialized variables (when expr is null)
        if (s.expr() == null) {
            if (!symbolTable.contains(varName)) {
                symbolTable.declareVariable(varName, varType);
                return TypeCheck.VOID;
            }
            System.err.println("Variable '" + varName + "' already declared");
            return TypeCheck.ERROR;
        }

        // Existing code for initialized variables
        TypeCheck exprType = s.expr().accept(this);
        if (exprType == TypeCheck.ERROR) {
            System.err.println("Error in initialization of " + varName + ": " + exprType);
            return TypeCheck.ERROR;
        }
        if (varType != exprType) {
            System.err.println("Type mismatch in declaration of " + varName +
                             ": expected " + varType + ", got " + exprType);
            return TypeCheck.ERROR;
        }

        if (!symbolTable.contains(varName)) {
            symbolTable.declareVariable(varName, varType);
            return TypeCheck.VOID;
        }
        System.err.println("Variable '" + varName + "' already declared");
        return TypeCheck.ERROR;
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

        // Handle compound assignment operators
        if (s.assignmentOperator() != AssignmentOperator.ASSIGN) {
            // For compound operators like +=, -=, etc.
            if (isNumeric(declaredType) && isNumeric(exprType)) {
                // Allow operations between numeric types
                if (declaredType == TypeCheck.INT && exprType == TypeCheck.DOUBLE) {
                    System.err.println("Warning: Possible loss of precision in compound assignment: " +
                            "implicit conversion from double to int");
                }
                return TypeCheck.VOID;
            }
        } else {
            // For simple assignment (=)
            if (declaredType != exprType) {
                // Allow INT -> DOUBLE assignment
                if (declaredType == TypeCheck.DOUBLE && exprType == TypeCheck.INT) {
                    return TypeCheck.VOID;
                }

                System.err.println("Type mismatch in assignment to " + varName +
                        ": expected " + declaredType + ", got " + exprType);
                return TypeCheck.ERROR;
            }
        }

        return TypeCheck.VOID;
    }


    @Override
    public TypeCheck visitSprint(Sprint s) {
        TypeCheck exprType = s.expr().accept(this);
        if (exprType == TypeCheck.ERROR) {
            System.err.println("Error in print statement: " + exprType);
            return TypeCheck.ERROR;
        }

        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSblock(Sblock s) {
        symbolTable.enterScope();

        TypeCheck result = TypeCheck.VOID;
        for (Statement stmt : s.stmts().elements()) {
            TypeCheck stmtResult = stmt.accept(this);
            // Track the return type if the statement is a return
            if (stmt instanceof SReturn) {
                result = stmtResult;
            }
            if (stmtResult == TypeCheck.ERROR) {
                result = TypeCheck.ERROR;
                break;
            }
        }

        symbolTable.exitScope();
        return result;
    }


    @Override
    public TypeCheck visitSfor(Sfor s) {

        symbolTable.enterScope();
       // Check the assigment (initialization)
       if (s.init() != null ){
           TypeCheck assignmentType = s.init().accept(this);
           if (assignmentType == TypeCheck.ERROR) {
               System.err.println("Error in for loop initialization: " + s.var().getId());
               return TypeCheck.ERROR;
           }
       }

       // Check the comparison (must be BOOL)
        if (s.condition() != null) {
            TypeCheck compType = s.condition().accept(this);
            if (compType != TypeCheck.BOOL) {
                System.err.println("For-loop comparison must be of type BOOL.");
                return TypeCheck.ERROR;
            }
        }

        // Check the iteration step
        if (s.update() != null) {
            TypeCheck iterateType = s.update().accept(this);
            if (iterateType == TypeCheck.ERROR) {
                System.err.println("Error in for loop update: " + s.var().getId());
                return TypeCheck.ERROR;
            }
        }


        // Check the body type
        loopDepth++;
        if (s.body() != null) {
            TypeCheck bodyType = s.body().accept(this);
            if (bodyType == TypeCheck.ERROR) {
                System.err.println("Error in body of for loop: " + s.var().getId());
                loopDepth--;
                return TypeCheck.ERROR;
            }
        }
        loopDepth--;
        symbolTable.exitScope();

    return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSExpression(SExpression s) {
        // Check if this is actually a declaration like "int x;"
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
            System.err.println("Error in body of while loop");
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
            System.err.println("Error: 'continue' used outside of a loop!!");
            return TypeCheck.ERROR;
        }
        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSlist(Slist s) {
        for (Statement stmt : s.elements()) {
            TypeCheck result = stmt.accept(this);
            if(result == TypeCheck.ERROR){
                return TypeCheck.ERROR;
            }
        }
        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSInDeCrement(SInDeCrement s)  {
        String varName = s.identifier().getId();

        if (!symbolTable.contains(varName)) {
            System.err.println("Undeclared variable in increment/decrement: " + varName);
            return TypeCheck.ERROR;
        }

        TypeCheck varType = symbolTable.lookup(varName);
        if (!isNumeric(varType)) {
            System.err.println("In/Decrement operations require INT or DOUBLE types: " + varType);
            return TypeCheck.ERROR;
        }

        return TypeCheck.VOID;
    }

    @Override
    public TypeCheck visitSReturn(SReturn s) {
        if (s.expr() == null) {
            System.err.println("Return statement missing expression");
            return TypeCheck.ERROR;
        }

        TypeCheck actual = s.expr().accept(this);

        if (currentExpectedReturnType == null) {
            System.err.println("Return statement outside of function.");
            return TypeCheck.ERROR;
        }

        if (actual != currentExpectedReturnType) {
            System.err.println("Return type mismatch: expected " +
                    currentExpectedReturnType + ", got " + actual);
            return TypeCheck.ERROR;
        }

        return actual;
    }

    @Override
    public TypeCheck visitSFunction(SFunction sFunction) {
        FunctionIdentifier f = sFunction.functionIdentifier();

        String funcName = f.typedName().getId();
        String returnTypeStr = f.typedName().getType();
        TypeCheck returnType = resolveType(returnTypeStr);


        if (returnType == TypeCheck.ERROR) {
            System.err.println("Unknown return type for function '" + funcName + "': " + returnTypeStr);
            return TypeCheck.ERROR;
        }

        // === Parameters ===

        List<TypeCheck> paramTypes = new ArrayList<>();
        for (Expression p : f.params().elements()) {
            if (p instanceof Eidentifier e) {
                //if no type return error
                String paramTypeStr = e.name().getType();
                if (paramTypeStr == null) {
                    System.err.println("Missing type for parameter: " + e.name().getId());
                    return TypeCheck.ERROR;
                }

                TypeCheck paramType = resolveType(paramTypeStr);
                if (paramType == TypeCheck.ERROR) {
                    System.err.println("Invalid parameter type: " + paramTypeStr);
                    return TypeCheck.ERROR;
                }

                paramTypes.add(paramType);
            }
            else {
                System.err.println("Invalid parameter (not identifier) in function '" + funcName + "'");
                return TypeCheck.ERROR;
            }
        }

        if (symbolTable.isFunction(funcName)) {
            System.err.println("Function '" + funcName + "' already declared.");
            return TypeCheck.ERROR;
        } // if not in symboltable will save
        symbolTable.declareFunction(funcName, paramTypes, returnType);

        symbolTable.enterScope();

        // Register parameter as a variable in the scope
        for (int i = 0; i < f.params().elements().size(); i++) {
            Expression p = f.params().elements().get(i);
            if (p instanceof Eidentifier e) {
                symbolTable.declareVariable(e.name().getId(), paramTypes.get(i));
            }
        }

        TypeCheck previousExpected = currentExpectedReturnType;
        currentExpectedReturnType = returnType;
        TypeCheck body = sFunction.body().accept(this);
        currentExpectedReturnType = previousExpected;

        symbolTable.exitScope();

        if (body == TypeCheck.ERROR) {
            System.err.println("Function body error in '" + funcName + "'");
            return TypeCheck.ERROR;
        }

        if (body != returnType) {
            System.err.println("Return type mismatch in function '" + funcName +
                    "': expected " + returnType + ", got " + body);
            return TypeCheck.ERROR;
        }

        return TypeCheck.VOID;
    }

    //tpyechecks in sFunction
    @Override
    public TypeCheck visitFunctionIdentifier(FunctionIdentifier functionIdentifier) {
        return TypeCheck.ERROR;
    }

    /* ===== HELPER FUNCTIONS ===== */
    private boolean isNumeric(TypeCheck t) {
        return t == TypeCheck.INT || t == TypeCheck.DOUBLE;
    }

    private TypeCheck resolveType(String type) {
        return switch (type) {
            case "int" -> TypeCheck.INT;
            case "double" -> TypeCheck.DOUBLE;
            case "bool" -> TypeCheck.BOOL;
            case "string" -> TypeCheck.STRING;
            case "NeuralNetwork" -> TypeCheck.NEURALNETWORK;
            case "Layer" -> TypeCheck.LAYER;
            case "ActivationFunction" -> TypeCheck.ACTIVATIONFUNC;
            case "Array" -> TypeCheck.ARRAY;
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