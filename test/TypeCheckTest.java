import Ast.Slist;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;
import TypeChecking.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;


public class TypeCheckTest {
    // ===== Typecheck testing readme is in testfiles/typechecking =====

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

    // ===== Basic Variable Declaration and Usage =====
    @Test
    public void testBasicVariableDeclaration() {
        TokenGetter tokenGetter = new TokenGetter("BasicVariableDeclarationAndUsage.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        // Test variable declaration and assignments

        assertTrue(symbols.contains("x"));
        assertTrue(symbols.contains("y"));
        assertTrue(symbols.contains("z"));
        assertEquals(TypeCheck.INT, symbols.lookup("x"));
        assertEquals(TypeCheck.INT, symbols.lookup("y"));
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("z"));

        assertEquals(result, TypeCheck.VOID);
    }

    // ===== Type Mismatch Errors =====
    @Test
    public void testTypeMismatchIntToBool() {
        TokenGetter tokenGetter = new TokenGetter("TypeMismatchIntBool.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Assigning a bool to an int should result in an error");


    }

    @Test
    public void testTypeMismatchBoolToInt() {
        TokenGetter tokenGetter = new TokenGetter("TypeMismatchBoolInt.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Assigning an int to a bool should result in an error");


    }

    @Test
    public void testTypeMismatchDoubleString() {
        TokenGetter tokenGetter = new TokenGetter("TypeMismatchDoubleString.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Assigning a String to a Double should result in an error");
    }
    // ===== Undeclared Variable Usage =====
    @Test
    public void testAssigningToUndeclaredVariable() {
        TokenGetter tokenGetter = new TokenGetter("AssigningToUndeclaredVariable.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Assigning to an undeclared variable should result in an error");

    }

    @Test
    public void testAssigningUndeclaredVariableToDeclaredVariable() {
        TokenGetter tokenGetter = new TokenGetter("AssigningUndeclaredVariableToDeclaredVariable.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Using an undeclared variable in assignment should result in an error");
    }
    // ===== If-Else Statements and Conditions =====
    @Test
    public void testIfElseStatementsNonBooleanCondition() {
        TokenGetter tokenGetter = new TokenGetter("If-ElseStatementsNon-BooleanCondition.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using a non-Boolean condition should result in an error");

    }

    @Test
    public void testIfElseStatementsBooleanCondition() {
        TokenGetter tokenGetter = new TokenGetter("If-ElseStatementBoolCondition.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a boolean condition should result in no error");


    }

    @Test
    public void testIfElseStatementsNumericComparison() {
        TokenGetter tokenGetter = new TokenGetter("If-ElseStatementsNumericComparison.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a numeric comparison should result in no error");
    }

    // ===== Loops and Break/Continue =====
    @Test
    public void testContinueBreakInLoop() {
        TokenGetter tokenGetter = new TokenGetter("continueBreak-InLoop.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using continue or break in a loop should not give an error and return void");
    }

    @Test
    public void testBreakOutsideLoop() {
        TokenGetter tokenGetter = new TokenGetter("breakOutsideLoop.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Using break outside of a loop should give an error");
    }

    @Test
    public void testContinueOutsideLoop() {
        TokenGetter tokenGetter = new TokenGetter("continueOutsideLoop.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Using continue outside of a loop should give an error");
    }

    @Test
    public void testForLoop() {
        TokenGetter tokenGetter = new TokenGetter("ForLoop.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.VOID, result, "Using continue outside of a loop should give an error");
    }

    // ===== Nested Scopes and Variable Shadowing =====
    @Test
    public void testInnerDeclarationShadowsOuterDeclaration() {
        TokenGetter tokenGetter = new TokenGetter("InnerDeclarationShadowsOuterDeclaration.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }

    @Test
    public void testInnerDeclarationNotVisibleOutsideScope() {
        TokenGetter tokenGetter = new TokenGetter("InnerDeclarationNotVisibleOutsideScope.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }

    // ===== Operators and Expressions =====
    @Test
    public void testOperatorsAndExpressionsValidComputations() {
        TokenGetter tokenGetter = new TokenGetter("OperatorsAndExpressionsValidComputations.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);


        assertEquals(TypeCheck.INT, symbols.lookup("z"));
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("e"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("c"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("f"));

        assertEquals(TypeCheck.VOID, result);

    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationIntBool() {
        TokenGetter tokenGetter = new TokenGetter("testOperatorsAndExpressionsInvalidComputationIntBool.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Computing an int with a bool should result in an error");
    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationLogicOp() {
        TokenGetter tokenGetter = new TokenGetter("testOperatorsAndExpressionsInvalidComputationLogicOp.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Using logical operators without booleans should result in an error");
    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationComparison() {
        TokenGetter tokenGetter = new TokenGetter("testOperatorsAndExpressionsInvalidComputationComparison.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Comparing different types should result in an error");
    }

    // ===== Complex Nested Structures =====
    @Test
    public void testComplexNestedStructure() {
        TokenGetter tokenGetter = new TokenGetter("ComplexNestedStructure.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.VOID, result, "Testing a complex nested structure should not give an error");
    }

    // ===== Ternary Operator =====
    @Test
    public void testTernaryOperatorSuccess() {
        TokenGetter tokenGetter = new TokenGetter("TernaryOperatorSuccess.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.INT, symbols.lookup("x"));
        assertEquals(TypeCheck.INT, symbols.lookup("y"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("condition"));
        assertEquals(TypeCheck.INT, symbols.lookup("z"));
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("d"));
        assertEquals(TypeCheck.VOID, result, "Computing a ternary operator with compatible types should not give an error, and return void");
    }

    @Test
    public void testTernaryOperatorFail() {
        TokenGetter tokenGetter = new TokenGetter("TernaryOperatorFail.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Computing a ternary operator with incompatible types should result in an error");
    }

    // ===== Type Conversion =====
    @Test
    public void testTypeConversionSuccess() {
        TokenGetter tokenGetter = new TokenGetter("TypeConversionSuccess.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);

        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.INT, symbols.lookup("x"));
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("d"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("b"));
        assertEquals(TypeCheck.STRING, symbols.lookup("s"));
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("d1"));
        assertEquals(TypeCheck.INT, symbols.lookup("x1"));
        assertEquals(TypeCheck.STRING, symbols.lookup("s1"));

        assertEquals(TypeCheck.VOID, result, "Converting a double to an int should not give an error, and return void");

    }


    @Test
    public void testTypeConversionErrorBoolToInt() {
        TokenGetter tokenGetter = new TokenGetter("testTypeConversionErrorBoolToInt.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Converting a bool to an int should result in an error");
    }

    @Test
    public void testTypeConversionErrorBoolToDouble() {
        TokenGetter tokenGetter = new TokenGetter("testTypeConversionErrorBoolToDouble.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Converting a bool to a double should result in an error");
    }

    @Test
    public void testTypeConversionErrorStringToInt() {
        TokenGetter tokenGetter = new TokenGetter("testTypeConversionErrorStringToInt.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Converting a string to an int should result in an error");
    }

    @Test
    public void testTypeConversionErrorStringToDouble() {
        TokenGetter tokenGetter = new TokenGetter("testTypeConversionErrorStringToDouble.txt", testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        TypeCheck result = slist.accept(typeVisitor);
        assertEquals(TypeCheck.ERROR, result, "Converting a string to a double should result in an error");
    }

}