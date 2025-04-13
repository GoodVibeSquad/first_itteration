package Parser;

import Parser.Production;
import Parser.Grammar;

public class GrammarBuilder {
    public static Grammar createGrammar(){
        Grammar grammar = new Grammar();

        grammar.setStartSymbol("statementlist");


        //Binary operators
        grammar.add("binaryoperator","PLUS");
        grammar.add("binaryoperator","MINUS");
        grammar.add("binaryoperator","MULTIPLY");
        grammar.add("binaryoperator","DIVISION");
        grammar.add("binaryoperator","MODULUS");
        grammar.add("binaryoperator","COMPARISON");
        grammar.add("binaryoperator","NOT_EQUALS");
        grammar.add("binaryoperator","GREATER_THAN");
        grammar.add("binaryoperator","LESS_THAN");
        grammar.add("binaryoperator","GREATER_OR_EQUALS");
        grammar.add("binaryoperator","LESS_OR_EQUALS");
        grammar.add("binaryoperator","AND");
        grammar.add("binaryoperator","OR");
        grammar.add("binaryoperator","EXPONENT");

        //Constants
        grammar.add("expression"," ");
        grammar.add("expression","BOOL");
        grammar.add("expression","INT");
        grammar.add("expression","DOUBLE");
        grammar.add("expression","STRING");
        grammar.add("expression","EULER");
        grammar.add("expression","PI");

        //expression
        //grammar.add("expression", "constant");
        grammar.add("expression", "OPEN_PARENTHESIS", "expression" , "CLOSED_PARENTHESIS");
        grammar.add("expression", "expression", "binaryoperator", "expression");
        grammar.add("expression", "unaryOperator", "expression");
        grammar.add("expression", "identifier");
        grammar.add("expression", "identifier", "OPEN_PARENTHESIS", "exlist", "CLOSED_PARENTHESIS");
        grammar.add("expression", "SUM", "OPEN_PARENTHESIS", "expression", "expression", "ACTIVATIONFUNCTION", "CLOSED_PARENTHESIS");
        grammar.add("expression", "SQUARE_ROOT","OPEN_PARENTHESIS","expression","CLOSED_PARENTHESIS");
        grammar.add("expression", "MAX","OPEN_PARENTHESIS","exlist","CLOSED_PARENTHESIS");
        grammar.add("expression", "expression","TERNARY","expression","COLON","expression");

        grammar.add("expression", "OPEN_PARENTHESIS","TYPE","CLOSED_PARENTHESIS","expression");

        grammar.add("expression", "TYPE","OPEN_PARENTHESIS","exlist","CLOSED_PARENTHESIS");
        grammar.add("expression", "ID","DOT","ID","OPEN_PARENTHESIS","exlist","CLOSED_PARENTHESIS");
        // Calling methods on TYPE
        grammar.add("expression", "TYPE","DOT","ID","OPEN_PARENTHESIS","exlist","CLOSED_PARENTHESIS");


        //Exlist
        grammar.add("exlist","expression");
        grammar.add("exlist","exlist","COMMA","expression");

        //funcClass
//        grammar.add("funcClass", "NEURALNETWORK");
//        grammar.add("funcClass", "LAYER");
//        grammar.add("funcClass", "ACTIVATIONFUNCTION");
//        grammar.add("funcClass", "ARRAY");

        //Statements
        // Top-level dispatch
        grammar.add("statement", "matched_stmt");
        grammar.add("statement", "unmatched_stmt");

        // Matched if-else and other complete statements
        grammar.add("matched_stmt", "IF", "expression", "matched_stmt", "ELSE", "matched_stmt");
        grammar.add("matched_stmt", "expression", "SEMICOLON");
        grammar.add("matched_stmt", "identifier", "eqop", "expression", "SEMICOLON");
        grammar.add("matched_stmt", "OPEN_CURLY_BRACKET", "statementlist", "CLOSED_CURLY_BRACKET");
        grammar.add("matched_stmt", "FOR", "OPEN_PARENTHESIS", "statement", "expression", "SEMICOLON", "statement", "CLOSED_PARENTHESIS", "statement");
        grammar.add("matched_stmt", "WHILE", "expression", "statement");
        grammar.add("matched_stmt", "BREAK", "SEMICOLON");
        grammar.add("matched_stmt", "CONTINUE", "SEMICOLON");

        // Unmatched: possible dangling else
        grammar.add("unmatched_stmt", "IF", "expression", "statement");
        grammar.add("unmatched_stmt", "IF", "expression", "matched_stmt", "ELSE", "unmatched_stmt");


        //stmtTail
//        grammar.add("stmtTail", "ELSE", "statement");
//        grammar.add("stmtTail", " ");

        //StatementList
        grammar.add("statementlist", "statement");
        grammar.add("statementlist", "statement", "statementlist");




        //equals operator
        grammar.add("eqop","EQUALS");
        grammar.add("eqop","ADDITION_ASSIGNMENT");
        grammar.add("eqop","SUBTRACTION_ASSIGNMENT");

        //identifier
        grammar.add("identifier", "typed_identifier");
        grammar.add("identifier", "ID");


        grammar.add("typed_identifier", "TYPE", "ID");

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