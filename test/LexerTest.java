import CodeReader.SourceCodeReader2;
import Lexer.*;
import Tokens.Token;
import Tokens.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;


public class LexerTest {
    private Lexer lexer;
    private SourceCodeReader2 reader;

    @BeforeEach
    void setUp() {
        reader = Mockito.mock(SourceCodeReader2.class);
        lexer = new Lexer(reader);
    }

    @Test
    void testTokenizeEOF() {
        Mockito.when(reader.isEOF()).thenReturn(true);
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    void testSkipWhitespace() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList(' ', ' ', '\n', 'a'));
        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();
        lexer.tokenize();
        Mockito.verify(reader, Mockito.atLeast(3)).advance();
    }

    @Test
    void testScanIdentifierWithExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('v', 'a', 'r', ' ', '\0'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();
        Token token = lexer.tokenize();

        Assertions.assertEquals(TokenType.ID, token.getType(), "Token targetType should be ID");
        Assertions.assertEquals("var", token.getValue(), "Token expr should be 'returnType'");
    }

    @Test
    void testScanIdentifierWithUnExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1', '2', '3', ' ', '\0'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();

        Token token = lexer.tokenize();
        Assertions.assertNotEquals(TokenType.ID, token.getType(), "Token type should not be ID");

    }

    @Test
    void testScanNumberWithExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1', '2', '3', '\0'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();

        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.INT, token.getType(), "Token type should be INT");

    }

    @Test
    void testScanNumberWithUnExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1', '.', '0'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());

        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();

        Token token = lexer.tokenize();
        Assertions.assertNotEquals(TokenType.INT, token.getType(), "Token type should be INT");

    }

    @Test
    void testScanDoubleWithExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1', '.', '0'));
        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());

        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.DOUBLE, token.getType(), "Token type should be Double");
        Assertions.assertEquals("1.0", token.getValue());
    }

    @Test
    void testScanDoubleWithUnExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1', '0', '3'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());

        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertNotEquals(TokenType.DOUBLE, token.getType(), "Token type should not be DOUBLE");

    }

    @Test
    void testScanStringWithExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('"', 'h', 'e', 'y', '"'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());

        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.STRING, token.getType(), "Token type should not be DOUBLE");
        Assertions.assertEquals("hey", token.getValue());
    }

    @Test
    void testScanStringWithUnExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('h', 'e', 'y'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());

        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertNotEquals(TokenType.STRING, token.getType(), "Token type should not be DOUBLE");
    }


    @Test
    void testScanCommentSingleLine() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('/', '/', 'T', 'e', 's', 't', '\n', 'f', 'o', 'o', '\0'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
        Mockito.when(reader.peek()).thenAnswer(invocation -> {
            if (charQueue.size() > 1) {
                return (Character) charQueue.toArray()[1];
            }
            return '\0';
        });
        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();

        Token token = lexer.tokenize();

        Assertions.assertEquals("foo", token.getValue(), "Lexer should skip single-line comment and return next token");
    }


    @Test
    void testScanMultiLineComment() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('/', '*', 'T', 'e', 's', 't', '*', '/', 'b', 'a', 'r', '\0'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
        Mockito.when(reader.peek()).thenAnswer(invocation -> {
            if (charQueue.size() > 1) {
                return (Character) charQueue.toArray()[1];
            }
            return '\0';
        });
        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();

        Token token = lexer.tokenize();

        Assertions.assertEquals("bar", token.getValue(), "Lexer should skip multi-line comment and return next token");
    }


    @Test
    void testScanUnExpectedCharacter() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('#'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());

        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();

        Assertions.assertThrows(RuntimeException.class, () -> lexer.tokenize());
    }

    @Test
    void testScanOperatorTokens() {
        List<String> operators = Arrays.asList("+", "-", "=", "!", "||", "&&");

        for (String op : operators) {
            Deque<Character> charQueue = new LinkedList<>();
            for (char c : op.toCharArray()) {
                charQueue.add(c);
            }
            charQueue.add('\0');

            Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
            Mockito.when(reader.peek()).thenAnswer(invocation -> {
                if (charQueue.size() > 1) {
                    return (Character) charQueue.toArray()[1];
                }
                return '\0';
            });

            Mockito.doAnswer(invocation -> {
                charQueue.poll();
                return null;
            }).when(reader).advance();

            Token token = lexer.tokenize();

            Assertions.assertEquals(TokenType.tokenTypeMap.get(op), token.getType(),
                    "Token type should match operator: " + op);
            Assertions.assertEquals(op, token.getValue(), "Token value should match operator: " + op);
        }
    }


    @Test
    void testScanKeywordTokens() {
        List<String> keywords = Arrays.asList("if", "else", "while", "return");

        for (String keyword : keywords) {
            Deque<Character> charQueue = new LinkedList<>();
            for (char c : keyword.toCharArray()) {
                charQueue.add(c);
            }
            charQueue.add('\0');

            Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
            Mockito.doAnswer(invocation -> {
                charQueue.poll();
                return null;
            }).when(reader).advance();

            Token token = lexer.tokenize();
            Assertions.assertEquals(TokenType.tokenTypeMap.get(keyword), token.getType(),
                    "Token type should match keyword: " + keyword);
            Assertions.assertEquals(keyword, token.getValue());
        }
    }

    @Test
    void testScanBooleanTokens() {
        List<String> bools = Arrays.asList("true", "false");

        for (String boolVal : bools) {
            Deque<Character> charQueue = new LinkedList<>();
            for (char c : boolVal.toCharArray()) {
                charQueue.add(c);
            }
            charQueue.add('\0');

            Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
            Mockito.doAnswer(invocation -> {
                charQueue.poll();
                return null;
            }).when(reader).advance();

            Token token = lexer.tokenize();
            Assertions.assertEquals(TokenType.BOOL, token.getType(), "Token type should be BOOL");
            Assertions.assertEquals(boolVal, token.getValue());
        }
    }

    @Test
    void testScanTypeTokens() {
        List<String> types = Arrays.asList("int", "string", "double", "bool");

        for (String type : types) {
            Deque<Character> charQueue = new LinkedList<>();
            for (char c : type.toCharArray()) {
                charQueue.add(c);
            }
            charQueue.add('\0');

            Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
            Mockito.doAnswer(invocation -> {
                charQueue.poll();
                return null;
            }).when(reader).advance();

            Token token = lexer.tokenize();
            Assertions.assertEquals(TokenType.TYPE, token.getType(), "Token type should be TYPE");
            Assertions.assertEquals(type, token.getValue());
        }
    }


    @Test
    void testScanSymbolTokens() {
        List<String> symbols = Arrays.asList("(", ")", "{", "}", "[", "]", ";", ",");

        for (String symbol : symbols) {
            Deque<Character> charQueue = new LinkedList<>();
            for (char c : symbol.toCharArray()) {
                charQueue.add(c);
            }
            charQueue.add('\0');

            Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
            Mockito.doAnswer(invocation -> {
                charQueue.poll();
                return null;
            }).when(reader).advance();

            Token token = lexer.tokenize();
            Assertions.assertEquals(TokenType.tokenTypeMap.get(symbol), token.getType(),
                    "Token type should match symbol: " + symbol);
            Assertions.assertEquals(symbol, token.getValue());
        }
    }


    @Test
    void testTokenOrderMatchesSourceCode() {
        // Simulating: if (1) { print(1); }
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList(
                'i', 'f', ' ', '(', '1', ')', ' ', '{', ' ',
                'p', 'r', 'i', 'n', 't', '(', '1', ')', ';', ' ', '}', '\0'
        ));

        // Mock reader behavior
        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());
        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();

        // Expected tokens in correct order
        List<TokenType> expectedTypes = Arrays.asList(
                TokenType.IF,                     // "if"
                TokenType.OPEN_PARENTHESIS,       // "("
                TokenType.INT,                    // "1"
                TokenType.CLOSED_PARENTHESIS,     // ")"
                TokenType.OPEN_CURLY_BRACKET,     // "{"
                TokenType.PRINT,                  // "print"
                TokenType.OPEN_PARENTHESIS,       // "("
                TokenType.INT,                    // "1"
                TokenType.CLOSED_PARENTHESIS,     // ")"
                TokenType.SEMICOLON,              // ";"
                TokenType.CLOSED_CURLY_BRACKET    // "}"
        );

        // Tokenize and assert order
        for (TokenType expectedType : expectedTypes) {
            Token token = lexer.tokenize();
            Assertions.assertEquals(expectedType, token.getType(),
                    "Unexpected token type: " + token.getType());
        }
    }
}

