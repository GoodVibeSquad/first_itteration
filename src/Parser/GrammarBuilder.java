package Parser;

import Parser.Production;
import Parser.Grammar;

public class GrammarBuilder {
    public static Grammar createGrammar(){
        Grammar grammar = new Grammar();

        grammar.setStartSymbol("statementlist");


        // ====== BINARY OPS ======
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

        // IDEA FOR FUNCTION CREATION
//        grammar.add("functionIdentifier", "identifier", "OPEN_PARENTHESIS", "expr_list", "CLOSED_PARENTHESIS");
//        grammar.add("functionIdentifier", "identifier", "OPEN_PARENTHESIS", "CLOSED_PARENTHESIS"); // For functions with no parameters
//        grammar.add("function", "functionIdentifier", "OPEN_CURLY_BRACKET", "statementlist", "CLOSED_CURLY_BRACKET");
//        grammar.add("matched_stmt", "function");

        // ====== CONSTANTS ======
        grammar.add("expression"," ");
        grammar.add("expression","BOOL");
        grammar.add("expression","INT");
        grammar.add("expression","DOUBLE");
        grammar.add("expression","STRING");
        grammar.add("expression","EULER");
        grammar.add("expression","PI");


        // ====== EXPRESSIONS ======
        grammar.add("expression", "OPEN_PARENTHESIS", "expression" , "CLOSED_PARENTHESIS");
        grammar.add("expression", "expression", "binaryoperator", "expression");
        grammar.add("expression", "unaryOperator", "expression");
        grammar.add("expression", "identifier");
        grammar.add("expression", "identifier", "OPEN_PARENTHESIS", "expr_list", "CLOSED_PARENTHESIS");
        grammar.add("expression", "SUM", "OPEN_PARENTHESIS", "expression","COMMA", "expression","COMMA", "ID", "CLOSED_PARENTHESIS");
        grammar.add("expression", "SQUARE_ROOT","OPEN_PARENTHESIS","expression","CLOSED_PARENTHESIS");
        grammar.add("expression", "MAX","OPEN_PARENTHESIS","expr_list","CLOSED_PARENTHESIS");
        grammar.add("expression", "expression","TERNARY","expression","COLON","expression");
        grammar.add("expression", "OPEN_PARENTHESIS","TYPE","CLOSED_PARENTHESIS","expression");
        grammar.add("expression", "NEW","TYPE","OPEN_PARENTHESIS","expr_list","CLOSED_PARENTHESIS");
        grammar.add("expression", "ID","DOT","ID","OPEN_PARENTHESIS","expr_list","CLOSED_PARENTHESIS");

        // Calling methods on TYPE
        grammar.add("expression", "TYPE","DOT","ID","OPEN_PARENTHESIS","expr_list","CLOSED_PARENTHESIS");

        // ====== EXPRESSION LIST ======
        grammar.add("expr_list","expression");
        grammar.add("expr_list","expr_list","COMMA","expression");

        // ====== STATEMENTS ======
        // Top-level dispatch
        grammar.add("statement", "matched_stmt");
        grammar.add("statement", "unmatched_stmt");

        // Matched if-else and other complete statements
        grammar.add("matched_stmt", "IF", "expression", "matched_stmt", "ELSE", "matched_stmt");
        grammar.add("matched_stmt", "expression", "SEMICOLON");
        grammar.add("matched_stmt", "identifier", "assop", "expression", "SEMICOLON");
        grammar.add("matched_stmt", "OPEN_CURLY_BRACKET", "statementlist", "CLOSED_CURLY_BRACKET");
        grammar.add("matched_stmt", "FOR", "OPEN_PARENTHESIS", "statement", "expression", "SEMICOLON", "statement", "CLOSED_PARENTHESIS", "statement");
        grammar.add("matched_stmt", "WHILE", "expression", "statement");
        grammar.add("matched_stmt", "BREAK", "SEMICOLON");
        grammar.add("matched_stmt", "CONTINUE", "SEMICOLON");
        grammar.add("matched_stmt", "ID", "In/deCrement", "SEMICOLON");
        grammar.add("matched_stmt", "PRINT", "OPEN_PARENTHESIS","expr_list","CLOSED_PARENTHESIS", "SEMICOLON");

        // Unmatched: possible dangling else
        grammar.add("unmatched_stmt", "IF", "expression", "statement");
        grammar.add("unmatched_stmt", "IF", "expression", "matched_stmt", "ELSE", "unmatched_stmt");



        // ====== STATEMENT LIST ======
        grammar.add("statementlist", "statement");
        grammar.add("statementlist", "statement", "statementlist");



        // ====== ASSIGNMENT OPS ======
        grammar.add("assop", "ASSIGN");
        grammar.add("assop", "ADD_ASSIGN");
        grammar.add("assop", "SUB_ASSIGN");
        grammar.add("assop", "MUL_ASSIGN");
        grammar.add("assop", "DIV_ASSIGN");
        grammar.add("assop", "MOD_ASSIGN");

        // ====== IDENTIFIER ======
        grammar.add("identifier", "typed_identifier");
        grammar.add("identifier", "ID");
        grammar.add("typed_identifier", "TYPE", "ID");

        // ====== UNARY OPERATORS ======
        grammar.add("unaryOperator", "MINUS");
        grammar.add("unaryOperator", "NEGATION");

        // ====== In/deCrement OPERATORS ======
        grammar.add("In/deCrement", "INCREMENT");
        grammar.add("In/deCrement", "DECREMENT");

        System.out.println("Productions in grammar:");


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