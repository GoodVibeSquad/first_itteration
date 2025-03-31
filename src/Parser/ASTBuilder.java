package Parser;


import java.util.List;

import Ast.*;
import Lexer.*;

public class ASTBuilder {

    public Object buildAst(Production production, List<Token> children) {
        int prodSize = production.getRhs().size();

        switch (production.lhs) {

            case "constant" -> {
                String constantValue = children.getFirst().getValue();
                if ("true".equals(production.getRhs()) || "false".equals(production.getRhs())) {
                    return new CBool(Boolean.parseBoolean(constantValue));
                } else if (isDoubleLit(constantValue)) {
                    return new CDouble(Double.parseDouble(constantValue));
                } else if (isIntegerLit(constantValue)) {
                    return new CInt(Integer.parseInt(constantValue));
                } else if (isStringLit(children.getFirst())) {
                    return new CString(constantValue);
                } else if (isPi(children.getFirst())) {
                    return new CDouble(Double.parseDouble(constantValue));
                } else if (isEuler(children.getFirst())) {
                    return new CDouble(Double.parseDouble(constantValue));
                } else {
                    //return cNone
                    return null;
                }

            }

            case "expression" -> {

                if (prodSize == 3 && "(".equals(production.getRhs().getFirst())) {
                    //wtf skal den lave???
                    return null;
                } else if (prodSize == 3 && "binaryoperator".equals(production.getRhs().get(1))) {
                    return null;
                } else if (prodSize == 2 && "unaryoperator".equals(production.getRhs().getFirst())) {
                    // return new Eunaryoperators(children.getFirst().getValue(), children.get(1).getValue());
                    return null;
                } else if (prodSize == 1 && "identifier".equals(production.getRhs().getFirst())) {
                    //return new Eidentifer(children.getFirst().getValue()); ??
                    return null;
                } else if (prodSize == 4 && "exlist".equals(production.getRhs().get(3))) {
                    return null;
                } else if ("Sum".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("Sqrt".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("Max".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("?".equals(production.getRhs().get(2))) {
                    return null;
                } else if ("type".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("funcClass".equals(production.getRhs().get(1))) {
                    return null;

                } else if (prodSize == 3 && ".".equals(production.getRhs().get(1))) {
                    return null;
                } else {
                    //return Econstant
                    return null;
                }

            }
            case "exlist" -> {
                if ("exlist".equals(production.getRhs().getFirst())) {
                    return null;
                } else {
                    return null;
                }
            }
            case "funcClass" -> {
                if ("NeuralNetwork".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("Layer".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("ActivationFunction".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("Array".equals(production.getRhs().getFirst())) {
                    return null;
                }

            }
            case "statement" -> {
                if (prodSize == 6 && "if".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("identifier".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("{".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("for".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("while".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("break".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("continue".equals(production.getRhs().getFirst())) {
                    return null;
                } else {
                    return null;
                }
            }
            case "statementlist" -> {
                if ("statementlist".equals(production.getRhs().getFirst())) {
                    return null;
                } else {
                    return null;
                }
            }

            case "type" -> {
                if ("funcClass".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("int".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("string".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("double".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("bool".equals(production.getRhs().getFirst())) {
                    return null;
                }

            }
            case "eqop" -> {
                if ("+".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("-".equals(production.getRhs().getFirst())) {
                    return null;
                } else {
                    return null;
                }

            }
            case "identifier" -> {
                if (prodSize == 1) {
                    return null;
                } else if (prodSize == 2) {
                    return null;
                }
            }
            case "unaryOperator" -> {
                if ("!".equals(production.getRhs().getFirst())) {
                    return null;
                } else if ("-".equals(production.getRhs().getFirst())) {
                    return null;
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
