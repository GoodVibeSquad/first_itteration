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
        Mockito.doAnswer(invocation -> {charQueue.poll();return null;}).when(reader).advance();
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

        Assertions.assertEquals(TokenType.ID, token.getType(), "Token type should be ID");
        Assertions.assertEquals("var", token.getValue(), "Token value should be 'var'");
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
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1','.','0'));

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
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1','.','0'));
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
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('1','0','3'));

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
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('"','h','e','y','"'));

        Mockito.when(reader.currentChar()).thenAnswer(invocation -> charQueue.peek());

        Mockito.doAnswer(invocation -> {
            charQueue.poll();
            return null;
        }).when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.STRING, token.getType(), "Token type should not be DOUBLE");
        Assertions.assertEquals("hey",token.getValue());
    }

    @Test
    void testScanStringWithUnExpectedInput() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('h','e','y'));

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
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('/', '/', 'T', 'e', 's', 't', '\n', '\0'));

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

        Assertions.assertEquals(TokenType.COMMENT, token.getType(), "Token type should be COMMENT");
        Assertions.assertEquals("Test", token.getValue(), "Token value should be 'Test'");
    }

    @Test
    void testScanMultiLineComment() {
        Deque<Character> charQueue = new LinkedList<>(Arrays.asList('/', '*', 'T', 'e', 's', 't', '*', '/', '\0'));
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
        Assertions.assertEquals(TokenType.MULTI_LINE_COMMENT, token.getType(), "Token type should be MULTI_LINE_COMMENT");
        Assertions.assertEquals("Test", token.getValue(), "Token value should be 'Test'");
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
}
