package Parser;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.UnaryOperator;

import Ast.*;
import Tokens.Token;
import Tokens.TokenType;



public class ASTBuilder {

    public Object buildAst(Production production, List<Object> children) {
        int prodSize = production.getRhs().size();

        switch (production.lhs) {

            case "constant" -> {
                Object constantValue = children.getFirst();
                if(constantValue instanceof Token){
                    Token constant = (Token)constantValue;
                    switch (constant.getType().toString()) {
                        case "INT":
                            CInt cInt = new CInt(Integer.parseInt(constant.getValue()));
                            return new Econstant(cInt);
                        case "STRING":
                            CString cString = new CString(constant.getValue());
                            return cString;
                        case "BOOL":
                            CBool cBool = new CBool(Boolean.parseBoolean(constant.getValue()));
                            return cBool;
                        case "PI":
                            CPi cPi = new CPi();
                            return cPi;
                        case "EULER":
                            CEuler cEuler = new CEuler();
                            return cEuler;
                        case "NONE":
                            CNone cNone = new CNone();
                            return cNone;
                    }
                }else{
                    throw new RuntimeException("Syntax error at: " + constantValue);
                }

            }

            case "expression" -> {

            

                if (prodSize == 3 && "(".equals(production.getRhs().getFirst())) {
                    //wtf skal den lave???
                    return children.getFirst().getValue();

                } else if (prodSize == 3 && "binaryoperator".equals(production.getRhs().get(1))) {
                    Object binOperator = children.getFirst().getValue();
                    Object expressionLeft = children.get(1).getValue();
                    Object expressionRight = children.get(2).getValue();
                    return new Ebinaryoperators((BinaryOperators) binOperator, (Expression) expressionLeft, (Expression) expressionRight);

                } else if (prodSize == 2 && "unaryoperator".equals(production.getRhs().getFirst())) {
                    Object unaryoperator = children.getFirst().getValue();
                    Object expression = children.get(1).getValue();
                     return new Eunaryoperators((UnaryOperators) unaryoperator, (Expression) expression);

                } else if (prodSize == 1 && "identifier".equals(production.getRhs().getFirst())) {
                    Object identifier = children.getFirst().getValue();
                    return new Eidentifier((Identifier) identifier);

                } else if (prodSize == 4 && "exlist".equals(production.getRhs().get(3))) { //Hvordan referer vi til Exlist casen bruuh???
                    Object expression = children.getFirst().getValue();
                    List<Expression> expressions = new ArrayList<>();
                    expressions.add((Expression) expression);
                    return new Elist(expressions);

                } else if ("Sum".equals(production.getRhs().getFirst())) {
                    Object topExpression = children.getFirst().getValue();
                    Object bottomEpresssion = children.get(1).getValue();
                    Object aFunction = children.get(2).getValue();
                    return new ESum((Expression) topExpression, (Expression) bottomEpresssion, (Identifier) aFunction);

                } else if ("Sqrt".equals(production.getRhs().getFirst())) {
                    Object expression = children.getFirst().getValue();
                    return new ESqrt((Expression) expression);

                } else if ("Max".equals(production.getRhs().getFirst())) {
                    Object ex = children.getFirst();
                    return new EMax((Expression) ex);

                } else if ("?".equals(production.getRhs().get(2))) {
                    Object ex = children.getFirst();
                    Object ex2 = children.get(1);
                    Object ex3 = children.get(2);
                    return new Eternary((Expression) ex, (Expression) ex2, (Expression) ex3);

                } else if ("type".equals(production.getRhs().getFirst())) {
                    Object type = children.getFirst();
                    Object ex = children.get(1);
                    return new ETypeconversion((Type) type, (Expression) ex);

                } else if ("funcClass".equals(production.getRhs().get(1))) {
                    Object func = children.get(1);
                    return new ENewFunc((Expression) func); //???

                } else if (prodSize == 3 && ".".equals(production.getRhs().get(1))) {
                    Object id = children.getFirst();
                    Object id2 = children.get(2);
                    return new EMethodCall((Identifier) id, (Identifier) id2);

                } else {
                    Object constant = children.getFirst();
                    return new Econstant((Literals) constant);
                }

            }

            case "exlist" -> {
                if ("exlist".equals(production.getRhs().getFirst())) {
                    Object expression = children.getFirst().getValue();
                    List<Expression> expressions = new ArrayList<>();
                    expressions.add((Expression) expression);
                    return new Elist(expressions);

                } else if(prodSize == 3 && ",".equals(production.getRhs().get(1))) {
                    Object exlist = children.getFirst().getValue();
                    Object expression = children.get(2).getValue();

                    //Vi laver vores exlist(første parameter?) til en liste og tilføjer vores expression til listen
                    Elist list = (Elist) exlist;
                    list.elements().add((Expression) expression);

                    return list;
                }
            }
            case "funcClass" -> {
                if ("NeuralNetwork".equals(production.getRhs().getFirst())) {
                    return new funcClass[Integer.parseInt(children.getFirst().getValue())];
                } else if ("Layer".equals(production.getRhs().getFirst())) {
                    return new funcClass[Integer.parseInt(children.getFirst().getValue())];
                } else if ("ActivationFunction".equals(production.getRhs().getFirst())) {
                    return new funcClass[Integer.parseInt(children.getFirst().getValue())];
                } else if ("Array".equals(production.getRhs().getFirst())) {
                    return new funcClass[Integer.parseInt(children.getFirst().getValue())];
                }

            }
            case "statement" -> {
                if (prodSize == 6 && "if".equals(production.getRhs().getFirst())) {
                    Object statement = children.getFirst();
                    Object expression = children.get(1);
                    Object tail = children.get(2);
                    return new Sif((Expression) expression,(Statement) statement, (Statement) tail);

                } else if ("identifier".equals(production.getRhs().getFirst())) {
                    Object identifier = children.getFirst().getValue();
                    Object operator = children.get(1).getValue();
                    Object expression = children.get(2).getValue();
                    return new Sassign((Identifier) identifier, (AssignmentOperator) operator, (Expression) expression);

                } else if ("{".equals(production.getRhs().getFirst())) {
                    return children.getFirst();

                } else if ("for".equals(production.getRhs().getFirst())) {
                    Object identifier = children.getFirst();
                    Object assignment = children.get(2);
                    Object expression = children.get(3);
                    Object iterate = children.get(5);
                    Object body = children.get(7);
                    return new Sfor((Identifier) identifier, (Statement) assignment, (Expression) expression, (Statement) iterate, (Statement) body );

                } else if ("while".equals(production.getRhs().getFirst())) {
                    Object expression = children.getFirst();
                    Object body = children.get(1);
                    return new SWhile((Expression) expression, (Statement) body);

                } else if ("break".equals(production.getRhs().getFirst())) {
                    return children.getFirst().getValue();

                } else if ("continue".equals(production.getRhs().getFirst())) {
                    return children.getFirst().getValue();

                } else {
                    Object expression = children.getFirst();
                    return new SExpression((Expression) expression);
                }
            }
            case "statementlist" -> {
                if ("statementlist".equals(production.getRhs().getFirst())) {
                    Object statement = children.getFirst().getValue();
                    List<Statement> statements = new ArrayList<>();
                    statements.add((Statement) statement);
                    return new Slist(statements);

                } else {
                    Object stlist = children.getFirst().getValue();
                    Object statement = children.get(2).getValue();

                    Slist list = (Slist) stlist;
                    list.elements().add((Statement) statement);

                    return list;
                }
            }

            case "type" -> {
                if ("funcClass".equals(production.getRhs().getFirst())) {
                    return new Type[Integer.parseInt(children.getFirst().getValue())];

                } else if ("int".equals(production.getRhs().getFirst())) {
                    return new Type[Integer.parseInt(children.getFirst().getValue())];

                } else if ("string".equals(production.getRhs().getFirst())) {
                    return new Type[Integer.parseInt(children.getFirst().getValue())];

                } else if ("double".equals(production.getRhs().getFirst())) {
                    return new Type[Integer.parseInt(children.getFirst().getValue())];

                } else if ("bool".equals(production.getRhs().getFirst())) {
                    return new Type[Integer.parseInt(children.getFirst().getValue())];
                }

            }
            case "assop" -> {
                if ("+".equals(production.getRhs().getFirst())) {
                    return new AssignmentOperator[Integer.parseInt(children.getFirst().getValue())];

                } else if ("-".equals(production.getRhs().getFirst())) {
                    return new AssignmentOperator[Integer.parseInt(children.getFirst().getValue())];

                } else if ("=".equals(production.getRhs().getFirst())){
                    return new AssignmentOperator[Integer.parseInt(children.getFirst().getValue())];
                } else {
                    return new AssignmentOperator[Integer.parseInt(children.getFirst().getValue())];
                }
            }

            case "identifier" -> {
                if (prodSize == 1) {
                    Object id = children.getFirst();
                    
                    return new Eidentifier((Identifier) id);

                } else if (prodSize == 2) {    //???????
                    Object type = children.getFirst();
                    Object id = children.get(1);
                    return new Eidentifier((Identifier) id);
                }
            }
            case "unaryOperator" -> {
                if ("!".equals(production.getRhs().getFirst())) {
                    return new UnaryOperators[Integer.parseInt(children.getFirst().getValue())];
                } else if ("-".equals(production.getRhs().getFirst())) {
                    return new UnaryOperators[Integer.parseInt(children.getFirst().getValue())];
                }
            }
            default -> throw new RuntimeException("No AST rule for production: " + production);

        }


        return null;
    }

    private boolean isIntegerLit(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDoubleLit(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isStringLit(Token token) {
        return token.getType() == TokenType.STRING;
    }

    private boolean isPi(Token token) {
        return token.getType() == TokenType.PI;
    }

    private boolean isEuler(Token token) {
        return token.getType() == TokenType.EULER;
    }


}


 