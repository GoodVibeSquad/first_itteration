package Lexer;//Define the token types
import java.util.HashMap;
import java.util.Map;

public enum TokenType{
    INT(null),
    STRING(null),
    BOOL(null),
    DOUBLE(null),
    ID(null),
    COMMENT(null),
    MULTI_LINE_COMMENT(null),
    FOR("for"),
    WHILE("while"),
    ARRAY("["),
    PLUS("+"),
    MINUS("-"),
    DOT("."),
    MULTIPLY("*"),
    DIVISION("/"),
    MODULUS("%"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    EQUALS("="),
    NEGATION("!"),
    GREATER_OR_EQUALS(">="),
    LESS_OR_EQUALS("<="),
    NOT_EQUALS("!="),
    COMPARISON("=="),
    OPEN_PARENTHESIS("("),
    CLOSED_PARENTHESIS(")"),
    OPEN_CURLY_BRACKET("{"),
    CLOSED_CURLY_BRACKET("}"),
    COMMA(","),
    COLON(":"),
    SEMICOLON(";"),
    PRINT("print"),
    IF("if"),
    ELSE("else"),
    OR("||"),
    AND("&&"),
    RETURN("return"),
    BREAK("break"),
    CONTINUE("continue"),
    TERNARY("?"),
    INCREMENT("++"),
    DECREMENT("--"),
    ADDITION_ASSIGNMENT("+="),
    SUBTRACTION_ASSIGNMENT("-="),
    NEW("new"),
    THIS("This"),
    SUM("Sum"),
    SQUARE_ROOT("Sqrt"),
    EULER("E"),
    PI("Pi"),
    EXPONENT("^"),
    MAX("Max"),
    LAYER("Layer"),
    NEURALNETWORK("NeuralNetwork"),
    ACTIVATIONFUNCTION("ActivationFunction"),
    RELU("Relu"),
    EOF("EOF");


    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public static final Map<String, TokenType> tokenTypeMap = new HashMap<>();

    static {
        for(final TokenType tokenType : TokenType.values()) {
            tokenTypeMap.put(tokenType.name, tokenType);
        }
    }

    public String getName() {
        return name;
    }
}

