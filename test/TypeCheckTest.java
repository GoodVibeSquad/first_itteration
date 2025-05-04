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

    public Slist runTestFile(String filename){
        TokenGetter tokenGetter = new TokenGetter(filename, testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        Slist slist = Parser.parse(tokens);
        return slist;
    }

    // ===== Basic Variable Declaration and Usage =====
    @Test
    public void testBasicVariableDeclaration() {
        Slist slist = runTestFile("BasicVariableDeclarationAndUsage.txt");
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
        Slist slist = runTestFile("TypeMismatchIntBool.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning a bool to an int should result in an error");
    }

    @Test
    public void testTypeMismatchBoolToInt() {
        Slist slist = runTestFile("TypeMismatchBoolInt.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning an int to a bool should result in an error");
    }

    @Test
    public void testTypeMismatchDoubleString() {
        Slist slist = runTestFile("TypeMismatchDoubleString.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning a String to a Double should result in an error");
    }

    // ===== Undeclared Variable Usage =====
    @Test
    public void testAssigningToUndeclaredVariable() {
        Slist slist = runTestFile("AssigningToUndeclaredVariable.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning to an undeclared variable should result in an error");

    }

    @Test
    public void testAssigningUndeclaredVariableToDeclaredVariable() {
        Slist slist = runTestFile("AssigningUndeclaredVariableToDeclaredVariable.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using an undeclared variable in assignment should result in an error");
    }
    // ===== If-Else Statements and Conditions =====
    @Test
    public void testIfElseStatementsNonBooleanCondition() {
        Slist slist = runTestFile("If-ElseStatementsNon-BooleanCondition.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using a non-Boolean condition should result in an error");

    }

    @Test
    public void testIfElseStatementsBooleanCondition() {
        Slist slist = runTestFile("If-ElseStatementBoolCondition.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a boolean condition should result in no error");


    }

    @Test
    public void testIfElseStatementsNumericComparison() {
        Slist slist = runTestFile("If-ElseStatementsNumericComparison.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a numeric comparison should result in no error");
    }

    // ===== Loops and Break/Continue =====
    @Test
    public void testContinueBreakInLoop() {
        Slist slist = runTestFile("continueBreak-InLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using continue or break in a loop should not give an error and return void");
    }

    @Test
    public void testBreakOutsideLoop() {
        Slist slist = runTestFile("breakOutsideLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using break outside of a loop should give an error");
    }

    @Test
    public void testContinueOutsideLoop() {
        Slist slist = runTestFile("continueOutsideLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using continue outside of a loop should give an error");
    }

    @Test
    public void testForLoop() {
        Slist slist = runTestFile("ForLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using continue outside of a loop should give an error");
    }

    // ===== Nested Scopes and Variable Shadowing =====
    @Test
    public void testInnerDeclarationShadowsOuterDeclaration() {
        Slist slist = runTestFile("InnerDeclarationShadowsOuterDeclaration.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }

    @Test
    public void testInnerDeclarationNotVisibleOutsideScope() {
        Slist slist = runTestFile("InnerDeclarationNotVisibleOutsideScope.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }

    // ===== Operators and Expressions =====
    @Test
    public void testOperatorsAndExpressionsValidComputations() {
        Slist slist = runTestFile("OperatorsAndExpressionsValidComputations.txt");
        TypeCheck result = slist.accept(typeVisitor);


        assertEquals(TypeCheck.INT, symbols.lookup("z"));
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("e"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("c"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("f"));

        assertEquals(TypeCheck.VOID, result);

    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationIntBool() {
        Slist slist = runTestFile("testOperatorsAndExpressionsInvalidComputationIntBool.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Computing an int with a bool should result in an error");
    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationLogicOp() {
        Slist slist = runTestFile("testOperatorsAndExpressionsInvalidComputationLogicOp.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using logical operators without booleans should result in an error");
    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationComparison() {
        Slist slist = runTestFile("testOperatorsAndExpressionsInvalidComputationComparison.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Comparing different types should result in an error");
    }

    // ===== Complex Nested Structures =====
    @Test
    public void testComplexNestedStructure() {
        Slist slist = runTestFile("ComplexNestedStructure.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Testing a complex nested structure should not give an error");
    }

    // ===== Ternary Operator =====
    @Test
    public void testTernaryOperatorSuccess() {
        Slist slist = runTestFile("TernaryOperatorSuccess.txt");
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
        Slist slist = runTestFile("TernaryOperatorFail.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Computing a ternary operator with incompatible types should result in an error");
    }

    // ===== Type Conversion =====
    @Test
    public void testTypeConversionSuccess() {
        Slist slist = runTestFile("TypeConversionSuccess.txt");
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
        Slist slist = runTestFile("testTypeConversionErrorBoolToInt.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a bool to an int should result in an error");
    }

    @Test
    public void testTypeConversionErrorBoolToDouble() {
        Slist slist = runTestFile("testTypeConversionErrorBoolToDouble.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a bool to a double should result in an error");
    }

    @Test
    public void testTypeConversionErrorStringToInt() {
        Slist slist = runTestFile("testTypeConversionErrorStringToInt.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a string to an int should result in an error");
    }

    @Test
    public void testTypeConversionErrorStringToDouble() {
        Slist slist = runTestFile("testTypeConversionErrorStringToDouble.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a string to a double should result in an error");
    }

}