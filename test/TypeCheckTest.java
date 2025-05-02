import Ast.Slist;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;
import TypeChecking.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;



public class TypeCheckTest {


    static TypeCheckerVisitor typeVisitor;
    static SymbolTable symbols;
    static String testDir = "test/TestFiles/TypeChecking/";

    @BeforeEach
    public void setUp() {

        Grammar grammar = GrammarBuilder.createGrammar();
        new TableGenerator(grammar);
        symbols = new SymbolTable();
        typeVisitor = new TypeCheckerVisitor(symbols);

    }

    @Test
    public void testBasicVariableDeclaration(){
        TokenGetter tokenGetter = new TokenGetter("BasicVariableDeclarationAndUsage.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        // Test variable declaration and assignments

        assertTrue(symbols.contains("x"));
        assertTrue(symbols.contains("y"));
        assertTrue(symbols.contains("z"));
        assertEquals(TypeCheck.INT, symbols.lookup("x") );
        assertEquals(TypeCheck.INT, symbols.lookup("y") );
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("z") );

        assertEquals(result, TypeCheck.VOID);
    }

    @Test
    public void testTypeMismatchIntToBool() {
        TokenGetter tokenGetter = new TokenGetter("TypeMismatchIntBool.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result,"Assigning a bool to an int should result in an error");


    }

    @Test
    public void testTypeMismatchBoolToInt() {
        TokenGetter tokenGetter = new TokenGetter("TypeMismatchBoolInt.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result,"Assigning an int to a bool should result in an error");


    }

    @Test
    public void testTypeMismatchDoubleString() {
        TokenGetter tokenGetter = new TokenGetter("TypeMismatchDoubleString.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result,"Assigning a String to a Double should result in an error");
    }

    @Test
    public void testAssigningToUndeclaredVariable() {
        TokenGetter tokenGetter = new TokenGetter("AssigningToUndeclaredVariable.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result,"Assigning to an undeclared variable should result in an error");

    }
    @Test
    public void testAssigningUndeclaredVariableToDeclaredVariable() {
        TokenGetter tokenGetter = new TokenGetter("AssigningUndeclaredVariableToDeclaredVariable.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result,"Using an undeclared variable in assignment should result in an error");
    }

    @Test
    public void testIfElseStatementsNonBooleanCondition() {
        TokenGetter tokenGetter = new TokenGetter("If-ElseStatementsNon-BooleanCondition.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result,"Using a non-Boolean condition should result in an error");

    }

    @Test
    public void testIfElseStatementsBooleanCondition() {
        TokenGetter tokenGetter = new TokenGetter("If-ElseStatementBoolCondition.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a boolean condition should result in no error");


    }

    @Test
    public void testIfElseStatementsNumericComparison() {
        TokenGetter tokenGetter = new TokenGetter("If-ElseStatementsNumericComparison.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a numeric comparison should result in no error");
    }

    @Test
    public void testContinueBreakInLoop() {
        TokenGetter tokenGetter = new TokenGetter("continueBreak-InLoop.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using continue or break in a loop should not give an error and return void");
    }

    @Test
    public void testBreakOutsideLoop() {
        TokenGetter tokenGetter = new TokenGetter("breakOutsideLoop.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Using break outside of a loop should give an error");
    }
    @Test
    public void testContinueOutsideLoop() {
        TokenGetter tokenGetter = new TokenGetter("continueOutsideLoop.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Using continue outside of a loop should give an error");
    }

    @Test
    public void testForLoop() {
        TokenGetter tokenGetter = new TokenGetter("ForLoop.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.VOID, result, "Using continue outside of a loop should give an error");
    }

    @Test
    public void testInnerDeclarationShadowsOuterDeclaration() {
        TokenGetter tokenGetter = new TokenGetter("InnerDeclarationShadowsOuterDeclaration.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }

    @Test
    public void testInnerDeclarationNotVisibleOutsideScope() {
        TokenGetter tokenGetter = new TokenGetter("InnerDeclarationNotVisibleOutsideScope.txt",testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }
}