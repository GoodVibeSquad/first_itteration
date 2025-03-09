package Lexer;

import java.util.regex.Pattern;

public class Lexer {
    private static final String BLANK = " \t\n\r";
    private static final Pattern identifierPattern = Pattern.compile("[a-zæøå_]+[a-zæøå_0-9]*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern nonSymbolPattern = Pattern.compile("[a-zæøå_0-9]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private final String input;
    private int position;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.currentChar = input.charAt(position);
    }

  

}
