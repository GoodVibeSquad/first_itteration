package Parser;


import java.util.List;

import Ast.*;
import Tokens.Token;
import Tokens.TokenType;


public class ASTBuilder {

    public Object buildAst(Production production, List<Object> children) {
        int prodSize = production.getRhs().size();

        switch (production.lhs) {

            case "binaryoperator" -> {
                Object operatorValue = children.getFirst();
                if (operatorValue instanceof TokenType) {
                    BinaryOperators binaryOperator = BinaryOperators.fromTokenType((TokenType) operatorValue);
                    if (binaryOperator != null){
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
                if (operatorValue instanceof TokenType) {
                    UnaryOperators unaryOperators = UnaryOperators.fromTokenType((TokenType) operatorValue);
                    if (unaryOperators != null){
                        return unaryOperators;
                    }
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                } else {
                    System.err.println("Invalid operator at: " + operatorValue);
                    throw new RuntimeException();
                }
            }

            case "identifier" -> {
                Object identifier = children.getFirst();
                if(identifier instanceof Token id){
                    return new Identifier(id.getValue());
                } else if (identifier instanceof Identifier) {
                    return identifier;
                }
            }

            case "typed_identifier" ->{
                Object type = children.getFirst();
                Object identifier = children.getLast();
                if(type instanceof Token typed
                        && identifier instanceof Token id){
                    return new Identifier(id.getValue(),typed.getValue());
                }
            }

            case "In/deCrement" ->{
                Object operatorValue = children.getFirst();
                if (operatorValue instanceof TokenType) {
                    InDeCrement inDeCrement = InDeCrement.fromTokenType((TokenType) operatorValue);
                    if (inDeCrement != null){
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
                if (operatorValue instanceof TokenType) {
                    AssignmentOperator assop = AssignmentOperator.fromTokenType((TokenType) operatorValue);
                    if (assop != null){
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
                Object expressionValue = children.getFirst();

                if(children.size() == 1 && expressionValue instanceof Token){
                    Token type = (Token)expressionValue;
                    switch (type.getType().toString()) {
                        case "INT" -> {
                            return new Econstant(new CInt(Integer.parseInt(type.getValue())));
                        }
                        case "STRING" -> {

                            return new Econstant(new CString(type.getValue()));
                        }
                        case "BOOL" -> {
                            return new Econstant(new CBool(Boolean.parseBoolean(type.getValue())));
                        }
                        case "DOUBLE" ->{
                            return new Econstant(new CDouble(Double.parseDouble(type.getValue())));
                        }
                        default -> {
                            System.err.println("Invalid Constant at: " + expressionValue);
                            throw new RuntimeException();

                        }
                    }
                }
                else if (children.size() == 3 && expressionValue instanceof Token ){
                    if(children.get(1) instanceof Expression){
                       return new EContainsExpression((Expression) children.get(1));
                   }else {
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

                        return new Ebinaryoperators(op, expr1, expr2);
                    } else {
                        System.err.println("Invalid Expression at: " + second);
                        throw new RuntimeException();
                    }
                }
                else if (children.size() == 2) {
                    Object first = children.getFirst();
                    Object second = children.getLast();

                    if (first instanceof UnaryOperators op && second instanceof Expression expr) {
                        return new Eunaryoperators(op, expr);
                    } else {
                        System.err.println("Invalid Expression at: " + second);
                        throw new RuntimeException();
                    }
                }
                else if (children.size() == 1) {
                    Object first = children.getFirst();

                    if(first instanceof Identifier id){
                        return new Eidentifier(id);
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }

                }
                else if (children.size() == 4 && expressionValue instanceof Identifier){
                    Object first = children.getFirst();
                    Object second = children.get(1);
                    Object third = children.get(2);
                    Object fourth = children.getLast();

                    if (first instanceof Identifier id &&
                        second instanceof Token &&
                        third instanceof Elist exprList &&
                        fourth instanceof Token) {

                        return new EFuncCall(id, exprList);

                    } else {
                        System.err.println("Invalid Expression at: " + third);
                        throw new RuntimeException();
                    }
                }
                else if (expressionValue instanceof Token token && token.getType() == TokenType.SUM){

                    Object first = children.get(2);
                    Object second = children.get(4);
                    Object third = children.get(6);


                    if(first instanceof Expression top &&
                       second instanceof Expression bot &&
                        third instanceof Token id){

                        return new ESum(top,bot, new Identifier(id.getValue()));
                    } else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }

                }
                else if (expressionValue instanceof Token token && token.getType() == TokenType.SQUARE_ROOT){
                    Object first = children.get(2);
                    if (first instanceof Expression expr){
                        return new ESqrt(expr);
                    }  else {
                        System.err.println("Invalid Expression at: " + first);
                        throw new RuntimeException();
                    }
                }
                else if (expressionValue instanceof Token token && token.getType() == TokenType.MAX) {
                        Object first = children.get(2);
                        if (first instanceof Elist args ){
                            return new EMax(args);
                        } else {
                            System.err.println("Invalid Expression at: " + first);
                            throw new RuntimeException();
                        }
                }
                else if (children.get(1) instanceof Token token && token.getType() == TokenType.TERNARY){

                }

            }

            case "exlist" -> {

            }

            case "statement" -> {
                Object statement = children.getFirst();
                if(statement instanceof Statement){
                    return statement;
                } else {
                    System.err.println("Invalid Statement at: " + statement);
                    throw new RuntimeException();
                }
            }

            case "matched_stmt" -> {

            }
            
            case "unmatched_stmt" -> {
                if (children.size() == 3){
                    Object expresionObject = children.get(1);
                    Object statementObject = 
                } else if (children.size() == 5) {

                }
            }

            case "statementlist" -> {

            }




            

        }
        return null;
    }
}

 