package Parser;

import Parser.Production;
import Parser.Grammar;

public class GrammarBuilder {
    public static Grammar createGrammar(){
        Grammar grammar = new Grammar();

        grammar.setStartSymbol("statementlist");

        //Binary operators
        grammar.add("binaryoperator","+");
        grammar.add("binaryoperator","-");
        grammar.add("binaryoperator","*");
        grammar.add("binaryoperator","/");
        grammar.add("binaryoperator","%");
        grammar.add("binaryoperator","==");
        grammar.add("binaryoperator","!=");
        grammar.add("binaryoperator",">");
        grammar.add("binaryoperator","<");
        grammar.add("binaryoperator",">=");
        grammar.add("binaryoperator","<=");
        grammar.add("binaryoperator","&&");
        grammar.add("binaryoperator","||");
        grammar.add("binaryoperator","^");

        //Constants
        grammar.add("constant"," ");
        grammar.add("constant","bool");
        grammar.add("constant","int");
        grammar.add("constant","double");
        grammar.add("constant","string");
        grammar.add("constant","Euler");
        grammar.add("constant","Pi");

        //expression
        grammar.add("expression", "constant");
        grammar.add("expression", "(", "expression" , ")");
        grammar.add("expression", "expression", "binaryoperator", "expression");
        grammar.add("expression", "unaryoperator", "expression");
        grammar.add("expression", "identifier");
        grammar.add("expression", "identifier", "(", "exlist", ")");
        grammar.add("expression", "Sum", "(", "expression", "expression", "ActivationFunction", ")");
        grammar.add("expression", "Sqrt","(","expression",")");
        grammar.add("expression", "Max","(","exlist",")");
        grammar.add("expression", "expression","?","expression",":","expression");
        grammar.add("expression", "type","expression");
        grammar.add("expression", "new","funcClass");
        grammar.add("expression", "identifier",".","identifier");

        //Exlist
        grammar.add("exlist","expression");
        grammar.add("exlist","exlist",",","expression");

        //funcClass
        grammar.add("funcClass", "NeuralNetwork");
        grammar.add("funcClass", "Layer");
        grammar.add("funcClass", "ActivationFunction");
        grammar.add("funcClass", "Array");

        //Statements
        grammar.add("statement", "expression");
        grammar.add("statement", "if", "expression", "statement", "stmtTail");
        grammar.add("statement", "identifier", "eqop", "expression", ";");
        grammar.add("statement", "{", "statementlist", "}");
        grammar.add("statement", "for",  "(", "statement", "expression", ";", "statement", ")", "statement");
        grammar.add("statement", "while", "expression", "statement");
        grammar.add("statement", "break", ";");
        grammar.add("statement", "continue", ";");

        //stmtTail
        grammar.add("stmtTail", "else", "statement");
        grammar.add("stmtTail", " ");

        //StatementList
        grammar.add("statementlist", "statement");
        grammar.add("statementlist", "statementlist", "statement");

        //type
        grammar.add("type", "funcClass");
        grammar.add("type", "int");
        grammar.add("type", "string");
        grammar.add("type", "double");
        grammar.add("type", "bool");

        //equals operator
        grammar.add("eqop","=");
        grammar.add("eqop","+=");
        grammar.add("eqop","-=");

        //identifier
        grammar.add("identifier", "identifier");
        grammar.add("identifier", "type", "identifier");

        //Unaryoperator
        grammar.add("unaryOperator", "-");
        grammar.add("unaryOperator", "!");

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
