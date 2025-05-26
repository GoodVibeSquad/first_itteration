package Parser;


import java.util.ArrayList;
import java.util.List;

import Ast.*;
import Tokens.Token;
import Tokens.TokenType;


public class ASTBuilder {

    public Object buildAst(Production production, List<Object> children) {
        int prodSize = production.getRhs().size();
        System.out.println("Children:" + children);
        switch (production.lhs) {

            case "functionIdentifier" -> {
                if (children.size() == 5) {  // Function of this form: TYPE ID ( expr_list )
                    Object type = children.get(0);
                    Object id = children.get(1);
                    Object exprList = children.get(3);

                    if (type instanceof Token typeToken && id instanceof Token idToken && exprList instanceof EList params) {
                        // Brug to-strengs constructor for at sætte både id og type rigtigt
                        Identifier typedName = new Identifier(idToken.getValue(), typeToken.getValue()); // her er type sat korrekt
                        return new FunctionIdentifier(typedName, params);
                    }
                } else if (children.size() == 4) { // Function of this form: TYPE ID ( )
                    Object type = children.get(0);
                    Object id = children.get(1);

                    if (type instanceof Token typeToken && id instanceof Token idToken) {
                        Identifier typedName = new Identifier(idToken.getValue(), typeToken.getValue()); // type gemmes
                        return new FunctionIdentifier(typedName, new EList(List.of()));
                    }
                }
            }

            case "function" -> {
                if (children.size() == 2) {
                    Object functionIdentifier = children.get(0);
                    Object statement = children.get(1);

                    if (functionIdentifier instanceof FunctionIdentifier identifier && statement instanceof Statement body) {
                        return new SFunction(identifier, body);
                    }
                }
            }
            case "binaryoperator" -> {
                Object operatorValue = children.getFirst();
                if (operatorValue instanceof Token t) {
                    BinaryOperators binaryOperator = BinaryOperators.fromTokenType(t.getType());
                    if (binaryOperator != null) {
                        return binaryOperator;
                    }
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                } else {
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                }
            }

            case "unaryOperator" -> {
                Object operatorValue = children.getFirst();
                if (operatorValue instanceof Token t) {
                    UnaryOperators unaryOperators = UnaryOperators.fromTokenType(t.getType());
                    if (unaryOperators != null) {
                        return unaryOperators;
                    }
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                } else {
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                }
            }

            case "index" -> {
                Object identifier = children.getFirst();
                if (identifier instanceof Token id) {
                    return new Identifier(id.getValue());
                } else if (identifier instanceof Identifier) {
                    return identifier;
                }
            }

            case "typed_identifier" -> {
                Object type = children.getFirst();
                Object identifier = children.getLast();
                if (type instanceof Token typed
                        && identifier instanceof Token id) {
                    return new Identifier(id.getValue(), typed.getValue());
                }
            }

            case "In/deCrement" -> {
                Object operatorValue = children.getFirst();
                if (operatorValue instanceof Token t) {
                    InDeCrement inDeCrement = InDeCrement.fromTokenType(t.getType());
                    if (inDeCrement != null) {
                        return inDeCrement;
                    }
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                } else {
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                }

            }

            case "assop" -> {
                Object operatorValue = children.getFirst();
                if (operatorValue instanceof Token t) {
                    AssignmentOperator assop = AssignmentOperator.fromTokenType(t.getType());
                    if (assop != null) {
                        return assop;
                    }
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                } else {
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                }
            }

            case "expression" -> {
                System.out.println("Expression children " + children);
                Object expressionValue = children.getFirst();
                if (children.size() == 1 && expressionValue instanceof Token) {
                    Token type = (Token) expressionValue;
                    switch (type.getType().toString()) {
                        case "INT" -> {
                            return new EConstant(new CInt(Integer.parseInt(type.getValue())));
                        }
                        case "STRING" -> {

                            return new EConstant(new CString(type.getValue()));
                        }
                        case "BOOL" -> {
                            return new EConstant(new CBool(Boolean.parseBoolean(type.getValue())));
                        }
                        case "DOUBLE" -> {
                            return new EConstant(new CDouble(Double.parseDouble(type.getValue())));
                        }
                        case "PI" -> {
                            return new EConstant(new CPi());
                        }
                        case "EULER" -> {
                            return new EConstant(new CEuler());
                        }
                        default -> {
                            System.err.println("Invalid Constant at: " + expressionValue);
                            throw new RuntimeException();

                        }
                    }
                }
                else if (children.size() == 3 && expressionValue instanceof Token) {
                    if (children.get(1) instanceof Expression) {
                        return new EContainsExpression((Expression) children.get(1));
                    } else {
                        System.err.println("Invalid Expression at: " + children.get(1));
                        throw new RuntimeException();
                    }
                }
                else if (children.size() == 3) {
                    Object first = children.getFirst();
                    Object second = children.get(1);
                    Object third = children.getLast();

                    if (first instanceof Expression expr1 &&
                            second instanceof BinaryOperators op &&
                            third instanceof Expression expr2) {

                        return Precedence(op,expr1,expr2);
                    } else {
                        System.err.println("Invalid Expression at: " + second);
                        throw new RuntimeException();
                    }
                }
                else if (children.size() == 2) {
                    Object first = children.getFirst();
                    Object second = children.getLast();

                    if (first instanceof UnaryOperators op && second instanceof Expression expr) {
                        return new EUnaryoperators(op, expr);
                    } else {
                        System.err.println("Invalid Expression at: " + second);
                        throw new RuntimeException();
                    }
                }
                else if (children.size() == 1) {
                    Object first = children.getFirst();

                    if (first instanceof Identifier id) {
                        return new EIdentifier(id);
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }

                }
                    else if (children.size() == 4 && expressionValue instanceof Token id && id.getType() == TokenType.ID
                            && children.get(1) instanceof Token openParen && openParen.getType() == TokenType.OPEN_PARENTHESIS
                            && children.get(2) instanceof EList
                            && children.get(3) instanceof Token closeParen && closeParen.getType() == TokenType.CLOSED_PARENTHESIS) {
                        Object first = children.getFirst();
                        Object second = children.get(1);
                        Object third = children.get(2);
                        Object fourth = children.get(3);

                        if (first instanceof Token &&
                                second instanceof Token &&
                                third instanceof EList exprList &&
                                fourth instanceof Token) {

                            return new EFuncCall(new Identifier(((Token) first).getValue()), exprList);

                        } else {
                            System.err.println("Invalid Expression at: " + third);
                            throw new RuntimeException();
                        }

                    } else if (expressionValue instanceof Token token && token.getType() == TokenType.SUM) {

                    Object first = children.get(2);
                    Object second = children.get(4);
                    Object third = children.get(6);
                    Object fourth = children.get(8);

                    if (first instanceof Expression top &&
                            second instanceof Expression bot &&
                            third instanceof Token id &&
                            fourth instanceof Expression body) {

                        return new ESum(new Identifier(id.getValue()), bot, top, body);
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }

                }
                else if (expressionValue instanceof Token token && token.getType() == TokenType.SQUARE_ROOT) {
                    Object first = children.get(2);
                    if (first instanceof Expression expr) {
                        return new ESqrt(expr);
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }
                }
                else if (expressionValue instanceof Token token && token.getType() == TokenType.MAX) {
                    Object first = children.get(2);
                    if (first instanceof EList args) {
                        return new EMax(args);
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }
                }
                else if (children.get(1) instanceof Token token && token.getType() == TokenType.TERNARY) {
                    Object first = children.getFirst();
                    Object second = children.get(2);
                    Object third = children.getLast();

                    if (first instanceof Expression condition &&
                        second instanceof Expression trueexpr &&
                        third instanceof Expression falseexpr) {
                        return new ETernary(condition, trueexpr, falseexpr);
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }
                }
                else if (children.get(1) instanceof Token token && token.getType() == TokenType.TYPE && children.size() == 4) {
                        Object first = children.get(1);
                        Object second = children.getLast();

                        if(first instanceof Token type &&
                            type.getType() == TokenType.TYPE &&
                            second instanceof  Expression expr ){

                            return new ETypeconversion(type, expr);
                        } else {
                            System.err.println("Invalid Expression at: " + first);
                            throw new RuntimeException();
                        }
                }
//

                else if (children.size() == 5 &&
                        children.get(0) instanceof Token newToken &&
                        newToken.getType() == TokenType.NEW &&
                        children.get(1) instanceof Token typeToken &&
                        children.get(3) instanceof EList args) {

                    String typeName = typeToken.getValue().toUpperCase();  // e.g., "LAYER"

                    // Try to match based on the expr of the token
                    try {
                        Type type = Type.valueOf(typeName);  // Match to enum expr
                        return new ENewFunc(type, args);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Unknown type: " + typeName);
                        throw new RuntimeException("Unknown type: " + typeName);
                    }
                }



                else if(expressionValue instanceof Token token && token.getType() == TokenType.ID
                        && children.get(1) instanceof Token token1 && token1.getType() == TokenType.DOT
                        &&children.get(4) instanceof EList){
                        Object first = children.getFirst();
                        Object second = children.get(2); // changed to 2, otherwise it just gets a dot as method name
                        Object third = children.get(4);

                        if(first instanceof Token object &&
                           second instanceof Token method &&
                           third instanceof EList args ){

                            return new EMethodCall(new Identifier(object.getValue()), new Identifier(method.getValue()), args);
                        }else {
                            System.err.println("Invalid Expression at: " + first);
                            throw new RuntimeException();
                        }
                }

            }
            case "expr_list" -> {
                if(children.size() == 1){
                    Object first = children.getFirst();

                    if(first instanceof Expression expr){
                        return new EList(List.of(expr));
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }
                }
                else if(children.size() == 3){
                    Object first = children.getFirst();
                    Object second = children.getLast();

                    if(first instanceof EList list &&
                       second instanceof Expression expr){

                        List<Expression> newElements = new ArrayList<>(list.elements());
                        newElements.add(expr);

                        return new EList(newElements);
                    }
                }
            }

            case "statement" -> {
                Object statement = children.getFirst();
                if (statement instanceof Statement) {
                    return statement;
                } else {
                    System.err.println("Invalid Statement at: " + statement);
                    throw new RuntimeException();
                }
            }

            case "matched_stmt" -> {
                Object first = children.getFirst();

                if (first instanceof SFunction sFunction) {
                    return sFunction;
                }

                if (first instanceof Identifier id && children.size() == 2) {
                    Object semiObject = children.getLast();
                    if (semiObject instanceof Token semi && semi.getType() == TokenType.SEMICOLON) {
                        return new SDeclaration(id, AssignmentOperator.ASSIGN, null);
                    }
                } else if (first instanceof EIdentifier eid && children.size() == 2) {
                    Object semiObject = children.getLast();
                    if (semiObject instanceof Token semi && semi.getType() == TokenType.SEMICOLON) {
                        Identifier id = eid.name();
                        if (id.getType() != null) {
                            return new SDeclaration(id, AssignmentOperator.ASSIGN, null);
                        } else {
                            return new SExpression(eid);
                        }
                    }
                } else if (first instanceof Token firstToken) {

                    switch (firstToken.getType().toString()) {
                        case "IF" -> {
                            Object expresionObject = children.get(1);
                            Object statementObject1 = children.get(2);
                            Object statementObject2 = children.getLast();
                            if (expresionObject instanceof Expression expression
                                    && statementObject1 instanceof Statement statement1
                                    && statementObject2 instanceof Statement statement2) {
                                return new SIf(expression, statement1, statement2);
                            } else {
                                System.err.println("Invalid Statement if at: " + expresionObject + ", " + statementObject1 + ", " + statementObject2);
                                throw new RuntimeException();
                            }
                        }
                        case "OPEN_CURLY_BRACKET" -> {
                            Object statementListObject = children.get(1);
                            if (statementListObject instanceof SList slist) {
                                return new SBlock(slist);  // Create a proper block instead of just returning Slist

                            }
                        }
                        case "FOR" -> {
                            Object assignObject = children.get(2); // int i = 0
                            Object comparisonObject = children.get(3); // i > 20
                            Object incrementObject = children.get(5); //i++
                            Object boddyObject = children.get(7);
                            if (assignObject instanceof SAssign assagin
                                    && comparisonObject instanceof Expression comp
                                    && incrementObject instanceof Statement increase
                                    && boddyObject instanceof Statement body) {
                                return new SFor(assagin, comp, increase, body);
                            } else if (assignObject instanceof SDeclaration declaration
                                    && comparisonObject instanceof Expression comp
                                    && incrementObject instanceof Statement increase
                                    && boddyObject instanceof Statement body) {
                                return new SFor(declaration, comp, increase, body);
                            }
                        }
//
                            case "WHILE" -> {  // New while for the grammar, so it has parentheses e.g. while(..): "WHILE", "OPEN_PARENTHESIS", "expression", "CLOSED_PARENTHESIS", "statement");
                              Object comparisonObject = children.get(2);
                                Object boddyObject = children.get(4);
                                if (comparisonObject instanceof Expression comp
                                        && boddyObject instanceof Statement buddy) {
                                    return new SWhile(comp, buddy);
                                }
                            }
                        case "BREAK" -> {
                            Object last = children.getLast();
                            if (last instanceof Token t
                                    && t.getType() == TokenType.SEMICOLON) {
                                return new SBreak();
                            }

                            }
                            case "CONTINUE" -> {
                                Object last = children.getLast();
                                if (last instanceof Token t
                                        && t.getType() == TokenType.SEMICOLON) {
                                    return new SContinue();
                                }
                            }
                            case "PRINT" -> {
                                // Checks if print statement is at 0 and if it's a token
                                // Inserts the expression list into the Sprint
                                Object printObject = children.get(0);
                                Object exprObject = children.get(2);

                                if (printObject instanceof Token) {
                                    return new SPrint((Expression) exprObject);
                                }
                            }
                            case "ID" -> {
                                Object second = children.get(1);
                                if (first instanceof Token id &&
                                        second instanceof InDeCrement inDe) {
                                    return new SInDeCrement(new Identifier(id.getValue()), inDe);
                                }
                            }
                            case "RETURN" -> {
                                Object second = children.get(1);
                                if (firstToken.getType() == TokenType.RETURN && second instanceof Expression) {
                                    return new SReturn((Expression)second);
                                }
                            }
                            default -> {
                                System.err.println("Invalid Statement at: " + firstToken.getType().toString());
                                throw new RuntimeException();
                            }
                        }

                    } else if (first instanceof Expression firstE) {
                        if (children.get(1) instanceof Token token) {
                            if (token.getType() == TokenType.SEMICOLON) {
                                return new SExpression(firstE);
                            }
                        } else {
                            System.err.println("Invalid Statement at: " + firstE);
                            throw new RuntimeException();
                        }
                    } else if (first instanceof Identifier id && children.size() == 4) {
                        Object assopObject = children.get(1);
                        Object expressionObject = children.get(2);
                        Object semiObject = children.get(3);
                        if (assopObject instanceof AssignmentOperator assop
                                && expressionObject instanceof Expression e
                                && semiObject instanceof Token semi) {
                            if (semi.getType() == TokenType.SEMICOLON) {
                                if (id.getType() != null) {
                                    return new SDeclaration(id, assop, e);
                                } else {
                                    return new SAssign(id, assop, e);
                                }
                            } else {
                                System.err.println("Invalid Statement at: " + id.getId());
                                throw new RuntimeException();
                            }
                        }
                    }
                }

            case "unmatched_stmt" -> {
                if (children.size() == 3) {
                    Object expresionObject = children.get(1);
                    Object statementObject = children.getLast();
                    if (expresionObject instanceof Expression expression
                            && statementObject instanceof Statement statement) {
                        return new SIf(expression, statement, new SExpression(new EConstant(new CNone())));
                    }else {
                        System.err.println("Invalid Statement if at: " + expresionObject + ", " + statementObject);
                        throw new RuntimeException();
                    }
                } else if (children.size() == 5) {
                    Object expresionObject = children.get(1);
                    Object statementObject1 = children.get(2);
                    Object statementObject2 = children.getLast();
                    if (expresionObject instanceof Expression expression
                            && statementObject1 instanceof Statement statement1
                            && statementObject2 instanceof Statement statement2) {
                        return new SIf(expression, statement1, statement2);
                    }else {
                        System.err.println("Invalid Statement if at: " + expresionObject + ", " + statementObject1 + ", " +  statementObject2);
                        throw new RuntimeException();
                    }
                }else {
                    System.err.println("Invalid Statement if at Unmatched: " + children.size());
                    throw new RuntimeException();
                }
            }

            case "statementlist" -> {

                if(children.getFirst() instanceof  Statement stmt && children.size() == 1 ){
                   return new SList(List.of(stmt));
                }
                else if (children.size() == 2){
                    Object first = children.getFirst();
                    Object second = children.getLast();

                    if( first instanceof Statement stmt &&
                        second instanceof SList slist){

                        List<Statement> newStatements = new ArrayList<>(slist.elements());
                        newStatements.addFirst(stmt);

                        return new SList(newStatements);


                    } else {
                        System.err.println("Invalid Statement at statementlist " );
                        throw new RuntimeException();
                    }

                } else{
                    System.err.println("Invalid Statement at statementlist ");
                    throw new RuntimeException();
                }

            }


        }
        return null;
    }

    private Expression Precedence(BinaryOperators op, Expression expr1, Expression expr2) {
        // Recursively apply precedence normalization to subtrees
        if (expr1 instanceof EBinaryoperators leftBin) {
            expr1 = Precedence(leftBin.op(), leftBin.left(), leftBin.right());
        }
        if (expr2 instanceof EBinaryoperators rightBin) {
            expr2 = Precedence(rightBin.op(), rightBin.left(), rightBin.right());
        }

        int currentPrec = op.getPrecedence();
        BinaryOperators.Associativity assoc = op.getAssociativity();

        // Check left child — rotate if lower precedence or equal and right-associative
        if (expr1 instanceof EBinaryoperators leftBin) {
            int leftPrec = leftBin.op().getPrecedence();

            if (leftPrec < currentPrec ||
                    (leftPrec == currentPrec && assoc == BinaryOperators.Associativity.RIGHT)) {
                // Left rotate: ( (a leftOp b) op c ) → ( a leftOp (b op c) )
                System.out.println();
                Expression newRight = Precedence(op, leftBin.right(), expr2);
                return new EBinaryoperators(leftBin.op(), leftBin.left(), newRight);

            }
        }

        // Check right child — rotate if higher precedence or equal and left-associative
        if (expr2 instanceof EBinaryoperators rightBin) {
            int rightPrec = rightBin.op().getPrecedence();
            if (rightPrec < currentPrec ||
                    (rightPrec == currentPrec && assoc == BinaryOperators.Associativity.LEFT)) {
                // Right rotate: ( a op (b rightOp c) ) → ( (a op b) rightOp c )
                Expression newLeft = Precedence(op, expr1, rightBin.left());

                return new EBinaryoperators(rightBin.op(), newLeft, rightBin.right());
            }
        }
        return new EBinaryoperators(op, expr1, expr2);
    }
}