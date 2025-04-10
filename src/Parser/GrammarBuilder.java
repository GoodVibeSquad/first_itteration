package Parser;

import Parser.Production;
import Parser.Grammar;

public class GrammarBuilder {
    public static Grammar createGrammar(){
        Grammar grammar = new Grammar();

        grammar.setStartSymbol("statementlist");

        //Constants
        grammar.add("constant"," ");
        grammar.add("constant","BOOL");
        grammar.add("constant","INT");
        grammar.add("constant","DOUBLE");
        grammar.add("constant","STRING");
        grammar.add("constant","EULER");
        grammar.add("constant","PI");

        //expression
        grammar.add("expression", "constant");
        grammar.add("expression", "OPEN_PARENTHESIS", "expression" , "CLOSED_PARENTHESIS");
        grammar.add("expression", "expression", "PLUS", "expression");
        grammar.add("expression", "expression", "MINUS", "expression");
        grammar.add("expression", "expression", "MULTIPLY", "expression");
        grammar.add("expression", "expression", "DIVISION", "expression");
        grammar.add("expression", "expression", "MODULUS", "expression");
        grammar.add("expression", "expression", "COMPARISON", "expression");
        grammar.add("expression", "expression", "NOT_EQUALS", "expression");
        grammar.add("expression", "expression", "GREATER_THAN", "expression");
        grammar.add("expression", "expression", "LESSER_THAN", "expression");
        grammar.add("expression", "expression", "GREATER_OR_EQUALS", "expression");
        grammar.add("expression", "expression", "LESS_OR_EQUALS", "expression");
        grammar.add("expression", "expression", "AND", "expression");
        grammar.add("expression", "expression", "OR", "expression");
        grammar.add("expression", "expression", "EXPONENT", "expression");
        grammar.add("expression", "unaryOperator", "expression");
        grammar.add("expression", "identifier");
        grammar.add("expression", "identifier", "OPEN_PARENTHESIS", "exlist", "CLOSED_PARENTHESIS");
        grammar.add("expression", "SUM", "OPEN_PARENTHESIS", "expression", "expression", "ACTIVATIONFUNCTION", "CLOSED_PARENTHESIS");
        grammar.add("expression", "SQRT","OPEN_PARENTHESIS","expression","CLOSED_PARENTHESIS");
        grammar.add("expression", "MAX","OPEN_PARENTHESIS","exlist","CLOSED_PARENTHESIS");
        grammar.add("expression", "expression","TERNARY","expression","COLON","expression");
        grammar.add("expression", "type","expression");
        grammar.add("expression", "NEW","funcClass");
        grammar.add("expression", "identifier","DOT","identifier");

        //Exlist
        grammar.add("exlist","expression");
        grammar.add("exlist","exlist","COMMA","expression");

        //funcClass
        grammar.add("funcClass", "NEURALNETWORK");
        grammar.add("funcClass", "LAYER");
        grammar.add("funcClass", "ACTIVATIONFUNCTION");
        grammar.add("funcClass", "ARRAY");

        //Statements
        grammar.add("statement", "expression", "SEMICOLON" );
        grammar.add("statement", "IF", "expression", "statement");
        grammar.add("statement", "IF", "expression", "statement","ELSE","statement");

        grammar.add("statement", "identifier", "eqop", "expression", "SEMICOLON");
        grammar.add("statement", "OPEN_CURLY_BRACKET", "statementlist", "CLOSED_CURLY_BRACKET", "SEMICOLON"); // Added SEMICOLON
        grammar.add("statement", "FOR", "OPEN_PARENTHESIS", "statement", "expression", "SEMICOLON", "statement", "CLOSED_PARENTHESIS", "statement");
        grammar.add("statement", "WHILE", "expression", "statement");
        grammar.add("statement", "BREAK", "SEMICOLON");
        grammar.add("statement", "CONTINUE", "SEMICOLON");

        //stmtTail
//        grammar.add("stmtTail", "ELSE", "statement");
//        grammar.add("stmtTail", " ");

        //StatementList
        grammar.add("statementlist", "statement");
        grammar.add("statementlist", "statementlist", "statement", "SEMICOLON"); //Har added SEMICOLON

        //type
        grammar.add("type", "funcClass");
        grammar.add("type", "INT");
        grammar.add("type", "STRING");
        grammar.add("type", "DOUBLE");
        grammar.add("type", "BOOL");

        //equals operator
        grammar.add("eqop","EQUALS");
        grammar.add("eqop","ADDITION_ASSIGNMENT");
        grammar.add("eqop","SUBTRACTION_ASSIGNMENT");

        //identifier
        grammar.add("identifier", "typed_identifier");
        grammar.add("identifier", "ID");

        grammar.add("typed_identifier", "type", "ID");

        //Unaryoperator
        grammar.add("unaryOperator", "MINUS");
        grammar.add("unaryOperator", "NEGATION");

        return grammar;
    }
    public static Grammar createSimpleGrammar(){
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");

        grammar.add("S", "E");
        grammar.add("E", "E", "+", "T");
        grammar.add("E", "T");
        grammar.add("T", "int");

        return grammar;

    }



}