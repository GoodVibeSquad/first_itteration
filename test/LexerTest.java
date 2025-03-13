import Lexer.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



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
        Mockito.when(reader.currentChar()).thenReturn(' ', ' ', '\n', 'a');
        Mockito.doNothing().when(reader).advance();
        lexer.tokenize();
        Mockito.verify(reader, Mockito.atLeast(3)).advance();
    }

    @Test
    void testScanIdentifier() {
        Mockito.when(reader.currentChar()).thenReturn('v', 'a', 'r', ' ', '\0');
        Mockito.doNothing().when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.ID, token.getType());
        Assertions.assertEquals("var", token.getValue());
    }

    @Test
    void testScanNumber() {
        Mockito.when(reader.currentChar()).thenReturn('1', '2', '3', '\0');
        Mockito.doNothing().when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.INT, token.getType());
        Assertions.assertEquals("123", token.getValue());
    }

    @Test
    void testScanDecimalNumber() {
        Mockito.when(reader.currentChar()).thenReturn('1', '2', '.', '3', '4', '\0');
        Mockito.doNothing().when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.DOUBLE, token.getType());
        Assertions.assertEquals("12.34", token.getValue());
    }

    @Test
    void testScanString() {
        Mockito.when(reader.currentChar()).thenReturn('"', 'h', 'e', 'l', 'l', 'o', '"', '\0');
        Mockito.doNothing().when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.STRING, token.getType());
        Assertions.assertEquals("hello", token.getValue());
    }

    @Test
    void testScanCommentSingleLine() {
        Mockito.when(reader.currentChar()).thenReturn('/', '/', 'T', 'e', 's', 't', '\n', '\0');
        Mockito.when(reader.peek()).thenReturn('/');
        Mockito.doNothing().when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.COMMENT, token.getType());
        Assertions.assertEquals("Test", token.getValue());
    }

    @Test
    void testScanCommentMultiLine() {
        Mockito.when(reader.currentChar()).thenReturn('/', '*', 'T', 'e', 's', 't', '*', '/', '\0');
        Mockito.when(reader.peek()).thenReturn('*', '/');
        Mockito.doNothing().when(reader).advance();
        Token token = lexer.tokenize();
        Assertions.assertEquals(TokenType.MULTI_LINE_COMMENT, token.getType());
        Assertions.assertEquals("Test", token.getValue());
    }

    @Test
    void testUnexpectedCharacter() {
        Mockito.when(reader.currentChar()).thenReturn('#');
        Mockito.doNothing().when(reader).advance();
        Assertions.assertThrows(RuntimeException.class, () -> lexer.tokenize());
    }
}
