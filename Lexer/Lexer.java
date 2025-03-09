package Lexer;

import java.util.regex.Pattern;

public class Lexer {
    private static final String BLANK = " \t\n\r";
    private static final Pattern identifierPattern = Pattern.compile("[a-zæøå_]+[a-zæøå_0-9]*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern nonSymbolPattern = Pattern.compile("[a-zæøå_0-9]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private final String input;
    private int position = 0;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.currentChar = input.charAt(position);
    }


    // Main function to scan the next token
    public Token tokenize() {
        while (currentChar != '\0') {
            if (BLANK.indexOf(currentChar) != -1) {
                skipWhitespace();
                continue;
            }

            if (currentChar == '/' && (peek() == '/' || peek() == '*')) {
                skipComment();
                continue;
            }

            if (Character.isLetter(currentChar) || currentChar == '_') {
                return scanIdentifier();
            }

            if (Character.isDigit(currentChar)) {
                return scanNumber();
            }

            if (currentChar == '"' || currentChar == '\'') {
                return scanString();
            }

            for (TokenType type : TokenType.values()) {
                if (type.getName() != null && input.startsWith(type.getName(), position)) {
                    String matchedSymbol = type.getName();
                    for (int i = 0; i < matchedSymbol.length(); i++) advance();
                    return new Token(type, matchedSymbol);
                }
            }

            throw new RuntimeException("Unexpected character: " + currentChar);
        }

        return new Token(TokenType.EOF);
    }

    private void advance() {
        position++;
        if (position < input.length()) {
            currentChar = input.charAt(position);
        } else {
            currentChar = '\0';
        }
    }

    private char peek() {
        return (position + 1 < input.length()) ? input.charAt(position + 1) : '\0';
    }

    private void skipWhitespace() {
        while (BLANK.indexOf(currentChar) != -1) {
            advance();
        }
    }

    private void skipComment() {
        if (currentChar == '/' && peek() == '/') {
            while (currentChar != '\n' && currentChar != '\0') {
                advance();
            }
        } else if (currentChar == '/' && peek() == '*') {
            advance();
            advance();
            while (!(currentChar == '*' && peek() == '/') && currentChar != '\0') {
                advance();
            }
            advance();
            advance();
        }
    }

    private Token scanIdentifier() {
        StringBuilder result = new StringBuilder();
        while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            result.append(currentChar);
            advance();
        }
        String word = result.toString().toLowerCase();

        if (TokenType.tokenTypeMap.containsKey(word)) {
            return new Token(TokenType.tokenTypeMap.get(word), word);
        }
        return new Token(TokenType.ID, word);
    }

    private Token scanNumber() {
        StringBuilder num = new StringBuilder();
        TokenType type = TokenType.INT;

        while (Character.isDigit(currentChar)) {
            num.append(currentChar);
            advance();
        }

        if (currentChar == '.') {
            type = TokenType.DOUBLE;
            num.append(currentChar);
            advance();
            while (Character.isDigit(currentChar)) {
                num.append(currentChar);
                advance();
            }
        }

        return new Token(type, num.toString());
    }

    private Token scanString() {
        char quoteType = currentChar;
        advance();
        StringBuilder value = new StringBuilder();

        while (currentChar != quoteType && currentChar != '\0') {
            value.append(currentChar);
            advance();
        }
        advance();
        return new Token(TokenType.STRING, value.toString());
    }


}
