package Lexer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private static final String BLANK = " \t\n\r";
    private static final String OPERATORS = "+=-!|&";
    private final SourceCodeReader2 reader;

    public Lexer(SourceCodeReader2 reader) {
        this.reader = reader;
        
    }

    public Token tokenize() {

        while (!reader.isEOF()) {


            if(reader.isEOF()) {
                return new Token(TokenType.EOF);
            }

            if (BLANK.indexOf(reader.currentChar()) != -1) {
                skipWhitespace();
                continue;
            }

            if (reader.currentChar() == '/' && (reader.peek() == '/' || reader.peek() == '*')) {
                return scanComment();
            }


            if (Character.isLetter(reader.currentChar()) || reader.currentChar() == '_') {
                return scanIdentifier();
            }

            if (Character.isDigit(reader.currentChar())) {
                return scanNumber();
            }

            if (reader.currentChar() == '"') {
                return scanString();
            }

            if (OPERATORS.indexOf(reader.currentChar()) != -1){
                return scanOperators();
            }

            //måske lav om så den faktisk bruger vores hash
            for (TokenType type : TokenType.values()) {
                String symbol = type.getName();

                if (symbol != null && matchSymbol(symbol)) {
                    return new Token(type, symbol);
                }
            }

            throw new RuntimeException("Unexpected character: " + reader.currentChar());
        }

        return new Token(TokenType.EOF);
    }




    private void skipWhitespace() {
        while (BLANK.indexOf(reader.currentChar()) != -1) {
            reader.advance();
        }
    }

    private Token scanComment() {
        StringBuilder comment = new StringBuilder();

        if (reader.currentChar() == '/' && reader.peek() == '/') { // Single-line comment
            reader.advance(); // Skip '/'
            reader.advance(); // Skip '/'
            while (reader.currentChar() != '\n' && reader.currentChar() != '\0') {
                comment.append(reader.currentChar());
                reader.advance();
            }
            return new Token(TokenType.COMMENT, comment.toString().trim());
        } else if (reader.currentChar() == '/' && reader.peek() == '*') { // Multi-line comment
            reader.advance(); // Skip '/'
            reader.advance(); // Skip '*'
            while (!(reader.currentChar() == '*' && reader.peek() == '/') && reader.currentChar() != '\0') {
                comment.append(reader.currentChar());
                reader.advance();
            }
            reader.advance(); // Skip '*'
            reader.advance(); // Skip '/'
            return new Token(TokenType.MULTI_LINE_COMMENT, comment.toString().trim());
        }
        return null;
    }

    private Token scanIdentifier() {
        StringBuilder result = new StringBuilder();
        while (Character.isLetterOrDigit(reader.currentChar()) || reader.currentChar() == '_') {
            result.append(reader.currentChar());
            reader.advance();
        }
        String word = result.toString();

        // Check if it's a keyword instead of returning an identifier
        if (TokenType.tokenTypeMap.containsKey(word)) {
            return new Token(TokenType.tokenTypeMap.get(word), word);
        }

        ArrayList<String> types = new ArrayList<>();
        types.add("int");
        types.add("string");
        types.add("double");
        types.add("bool");
        if(types.contains(word)){
            return new Token(TokenType.TYPE, word);
        }

        if(word.equals("true") || word.equals("false")){
            return new Token(TokenType.BOOL, word);
        }

        return new Token(TokenType.ID, word);
    }


    private Token scanNumber() {
        StringBuilder num = new StringBuilder();
        TokenType type = TokenType.INT;

        while (Character.isDigit(reader.currentChar())) {
            num.append(reader.currentChar());
            reader.advance();
        }

        if (reader.currentChar() == '.') {
            type = TokenType.DOUBLE;
            num.append(reader.currentChar());
            reader.advance();
            while (Character.isDigit(reader.currentChar())) {
                num.append(reader.currentChar());
                reader.advance();
            }
        }

        return new Token(type, num.toString());
    }

    private Token scanString() {
        char quoteChar = reader.currentChar();
        reader.advance();
        StringBuilder value = new StringBuilder();

        while (reader.currentChar() != quoteChar && reader.currentChar() != '\0') {
            value.append(reader.currentChar());
            reader.advance();
        }
        reader.advance();
        return new Token(TokenType.STRING, value.toString());
    }

    private Token scanOperators() {
        StringBuilder op = new StringBuilder();

        while (OPERATORS.indexOf(reader.currentChar()) != -1) {
            op.append(reader.currentChar());
            reader.advance();
        }
        reader.advance();

        String opString = op.toString();
        if (TokenType.tokenTypeMap.containsKey(opString)) {
            return new Token(TokenType.tokenTypeMap.get(opString), opString);
        }

        throw new RuntimeException("Unexpected operator: " + opString);
    }

    private boolean matchSymbol(String symbol) {
        int length = symbol.length();

        for (int i = 0; i < length; i++) {
            if (reader.isEOF() || reader.currentChar() != symbol.charAt(i)) {
                return false; // If any character does not match, return false
            }
            reader.advance(); // Move to the next character
        }

        return true; // If all characters match, return true
    }

}
