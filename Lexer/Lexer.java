package Lexer;

public class Lexer {
    private static final String BLANK = " \t\n\r";

    private final String input;
    private int position = 0;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.currentChar = input.length() > 0 ? input.charAt(position) : '\0';
    }

    public Token tokenize() {
        while (currentChar != '\0') {
            if (BLANK.indexOf(currentChar) != -1) {
                skipWhitespace();
                continue;
            }

            if (currentChar == '/' && (peek() == '/' || peek() == '*')) {
                return scanComment();
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

    private Token scanComment() {
        StringBuilder comment = new StringBuilder();

        if (currentChar == '/' && peek() == '/') { // Single-line comment
            advance(); // Skip '/'
            advance(); // Skip '/'
            while (currentChar != '\n' && currentChar != '\0') {
                comment.append(currentChar);
                advance();
            }
            return new Token(TokenType.COMMENT, comment.toString().trim());
        } else if (currentChar == '/' && peek() == '*') { // Multi-line comment
            advance(); // Skip '/'
            advance(); // Skip '*'
            while (!(currentChar == '*' && peek() == '/') && currentChar != '\0') {
                comment.append(currentChar);
                advance();
            }
            advance(); // Skip '*'
            advance(); // Skip '/'
            return new Token(TokenType.MULTI_LINE_COMMENT, comment.toString().trim());
        }
        return null;
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
