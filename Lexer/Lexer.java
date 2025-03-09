import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int position = 0;
    private int lastTokenLength = 1; // Default to 1 to ensure progress
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        while (position < input.length()) {
            char currentChar = input.charAt(position);


            // Whitespace
            if (Character.isWhitespace(currentChar)) {
                position++;
                continue;
            }

            // Single-line comment
            if (currentChar == '/' && peekChar() == '/') {
                tokens.add(skipSingleLineComment());
                continue;
            }

            // Multi-line comment
            if (currentChar == '/' && peekChar() == '*') {
                tokens.add(skipMultiLineComment());
                continue;
            }

            // Identifiers (variable names, function names, etc.)
            if (Character.isLetter(currentChar) || currentChar == '_') {
                tokens.add(scanIdentifier());
                continue;
            }

            // Numbers (integers and decimals)
            if (Character.isDigit(currentChar)) {
                tokens.add(scanNumber());
                continue;
            }

            // String literals
            if (currentChar == '"') {
                tokens.add(scanString());
                continue;
            }


            // Check if token exists (operators, keywords, etc.)
            Token token = checkIfTokenExists();
            if (token != null) {
                tokens.add(token);
                continue;
            }

            // If no match, move forward to avoid infinite loops
            position++;
        }

        // Add EOF token at the end
        tokens.add(new Token(TokenType.EOF, "EOF"));
        return tokens;
    }




    /** Check if the current token exists in TokenType */
    private Token checkIfTokenExists() {
        for (TokenType type : TokenType.values()) {
            String symbol = type.getName(); // Use actual token string
            if (symbol != null && input.startsWith(symbol, position)) {
                position++; // Move position correctly
                return new Token(type, symbol);
            }
        }
        return null;
    }

    /** Scans identifiers (variable names, function names, etc.) */
    private Token scanIdentifier() {
        int start = position;
        while (position < input.length() &&
               (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            position++;
        }
        String value = input.substring(start, position);
        TokenType type = TokenType.tokenTypeMap.getOrDefault(value, TokenType.IDENTIFIER);
        lastTokenLength = value.length(); // Store the correct length
        return new Token(type, value);
    }

    /** Scans numbers (integers and decimals) */
    private Token scanNumber() {
        int start = position;
        boolean isDecimal = false;

        while (position < input.length() &&
               (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            if (input.charAt(position) == '.') {
                isDecimal = true;
            }
            position++;
        }

        String value = input.substring(start, position);
        lastTokenLength = value.length(); // Store the correct length
        return new Token(isDecimal ? TokenType.DOUBLE : TokenType.INT, value);
    }

    /** Scans string literals */
    private Token scanString() {
        position++; // Skip opening quote
        int start = position;

        while (position < input.length() && input.charAt(position) != '"') {
            position++;
        }

        String value = input.substring(start, position);
        if (position < input.length() && input.charAt(position) == '"') {
            position++; // Skip closing quote
        }

        return new Token(TokenType.STRING, value);
    }

    /** Skips single-line comments */
    private Token skipSingleLineComment() {
        int start = position;
        while (position < input.length() && input.charAt(position) != '\n') {
            position++;
        }
        position++;
        return new Token(TokenType.SINGLE_LINE_COMMENT);

    }

    /** Skips multi-line comments */
    private Token skipMultiLineComment() {
        int start = position;
        position += 2; // Skip `/*`

        while (position < input.length() - 1 &&
               !(input.charAt(position) == '*' && input.charAt(position + 1) == '/')) {
            position++;
        }

        position += 2; // Skip closing `*/`
        position++;
        return new Token(TokenType.MULTI_LINE_COMMENT);

    }

    /** Peeks at the next character without moving position */
    private char peekChar() {
        return (position + 1 < input.length()) ? input.charAt(position + 1) : '\0';
    }
}
