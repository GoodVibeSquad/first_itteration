package Parser;

import Parser.Production;
import Parser.Grammar;

public class GrammarBuilder {
    public static Grammar createGrammar(){
        Grammar grammar = new Grammar();

        grammar.setStartSymbol("expression");

        grammar.add("expression", "primary", "expression_tail");

        grammar.add("expression_tail");
        grammar.add("expression_tail", "binaryoperator", "primary", "expression_tail");

        grammar.add("binaryoperator", "PLUS");
        grammar.add("binaryoperator", "MULTIPLY");

        grammar.add("primary", "constant");
        grammar.add("constant", "INT");

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
    /*
    * rammar grammar = new Grammar();

        grammar.setStartSymbol("expression");

        grammar.add("expression", "primary", "expression_tail");

        grammar.add("expression_tail");
        grammar.add("expression_tail", "binaryoperator", "primary", "expression_tail");

        grammar.add("binaryoperator", "PLUS");
        grammar.add("binaryoperator", "MULTIPLY");

        grammar.add("primary", "constant");
        grammar.add("constant", "INT");

        return grammar;
        * */
        /*
        *grammar.setStartSymbol("statementlist");

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
        grammar.add("constant"," ");
        grammar.add("constant","BOOL");
        grammar.add("constant","INT");
        grammar.add("constant","DOUBLE");
        grammar.add("constant","STRING");
        grammar.add("constant","EULER");
        grammar.add("constant","PI");

        //Primary
        grammar.add("primary", "constant");
        grammar.add("primary", "identifier");
        grammar.add("primary", "OPEN_PARENTHESIS", "expression", "CLOSED_PARENTHESIS");

        grammar.add("expression_tail", " ");
        grammar.add("expression_tail", "binaryoperator", "primary", "expression_tail");

        //expression
        grammar.add("expression", "primary", "expression_tail");
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
        grammar.add("statement", "IF", "expression", "statement", "stmtTail");
        grammar.add("statement", "identifier", "eqop", "expression", "SEMICOLON");
        grammar.add("statement", "OPEN_CURLY_BRACKET", "statementlist", "CLOSED_CURLY_BRACKET");
        grammar.add("statement", "FOR", "OPEN_PARENTHESIS", "statement", "expression", "SEMICOLON", "statement", "CLOSED_PARENTHESIS", "statement");
        grammar.add("statement", "WHILE", "expression", "statement");
        grammar.add("statement", "BREAK", "SEMICOLON");
        grammar.add("statement", "CONTINUE", "SEMICOLON");

        //stmtTail
        grammar.add("stmtTail", "ELSE", "statement");
        grammar.add("stmtTail", " ");

        //StatementList
        grammar.add("statementlist", "statement");
        grammar.add("statementlist", "statementlist", "statement");

        //type
        grammar.add("type", "funcClass");

        //equals operator
        grammar.add("eqop","EQUALS");
        grammar.add("eqop","ADDITION_ASSIGNMENT");
        grammar.add("eqop","SUBTRACTION_ASSIGNMENT");

        //identifier
        grammar.add("identifier", "typed_identifier");
        grammar.add("identifier", "simple_identifier");

        grammar.add("typed_identifier", "typed", "simple_identifier");

        //Unaryoperator
        grammar.add("unaryOperator", "MINUS");
        grammar.add("unaryOperator", "NEGATION");
        *
        * */


}
