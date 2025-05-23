import Ast.SList;
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

    public SList buildTestAst(String filename){
        TokenGetter tokenGetter = new TokenGetter(filename, testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        SList slist = Parser.parse(tokens);
        return slist;
    }

    // ===== Basic Variable Declaration and Usage =====
    @Test
    public void testBasicVariableDeclaration() {
        SList slist = buildTestAst("BasicVariableDeclarationAndUsage.txt");
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
        SList slist = buildTestAst("TypeMismatchIntBool.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning a bool to an int should result in an error");
    }

    @Test
    public void testTypeMismatchBoolToInt() {
        SList slist = buildTestAst("TypeMismatchBoolInt.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning an int to a bool should result in an error");
    }

    @Test
    public void testTypeMismatchDoubleString() {
        SList slist = buildTestAst("TypeMismatchDoubleString.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning a String to a Double should result in an error");
    }

    // ===== Undeclared Variable Usage =====
    @Test
    public void testAssigningToUndeclaredVariable() {
        SList slist = buildTestAst("AssigningToUndeclaredVariable.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Assigning to an undeclared variable should result in an error");

    }

    @Test
    public void testAssigningUndeclaredVariableToDeclaredVariable() {
        SList slist = buildTestAst("AssigningUndeclaredVariableToDeclaredVariable.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using an undeclared variable in assignment should result in an error");
    }
    // ===== If-Else Statements and Conditions =====
    @Test
    public void testIfElseStatementsNonBooleanCondition() {
        SList slist = buildTestAst("If-ElseStatementsNon-BooleanCondition.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using a non-Boolean condition should result in an error");

    }

    @Test
    public void testIfElseStatementsBooleanCondition() {
        SList slist = buildTestAst("If-ElseStatementBoolCondition.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a boolean condition should result in no error");


    }

    @Test
    public void testIfElseStatementsNumericComparison() {
        SList slist = buildTestAst("If-ElseStatementsNumericComparison.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using a numeric comparison should result in no error");
    }

    // ===== Loops and Break/Continue =====
    @Test
    public void testContinueBreakInLoop() {
        SList slist = buildTestAst("continueBreak-InLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using continue or break in a loop should not give an error and return void");
    }

    @Test
    public void testBreakOutsideLoop() {
        SList slist = buildTestAst("breakOutsideLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using break outside of a loop should give an error");
    }

    @Test
    public void testContinueOutsideLoop() {
        SList slist = buildTestAst("continueOutsideLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using continue outside of a loop should give an error");
    }

    @Test
    public void testForLoop() {
        SList slist = buildTestAst("ForLoop.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Using continue outside of a loop should give an error");
    }

    // ===== Nested Scopes and Variable Shadowing =====
    @Test
    public void testInnerDeclarationShadowsOuterDeclaration() {
        SList slist = buildTestAst("InnerDeclarationShadowsOuterDeclaration.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }

    @Test
    public void testInnerDeclarationNotVisibleOutsideScope() {
        SList slist = buildTestAst("InnerDeclarationNotVisibleOutsideScope.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Declaring a variable with the same name as an outer declaration should give an error");
    }

    // ===== Operators and Expressions =====
    @Test
    public void testOperatorsAndExpressionsValidComputations() {
        SList slist = buildTestAst("OperatorsAndExpressionsValidComputations.txt");
        TypeCheck result = slist.accept(typeVisitor);


        assertEquals(TypeCheck.INT, symbols.lookup("z"));
        assertEquals(TypeCheck.DOUBLE, symbols.lookup("e"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("c"));
        assertEquals(TypeCheck.BOOL, symbols.lookup("f"));

        assertEquals(TypeCheck.VOID, result);

    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationIntBool() {
        SList slist = buildTestAst("testOperatorsAndExpressionsInvalidComputationIntBool.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Computing an int with a bool should result in an error");
    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationLogicOp() {
        SList slist = buildTestAst("testOperatorsAndExpressionsInvalidComputationLogicOp.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Using logical operators without booleans should result in an error");
    }

    @Test
    public void testOperatorsAndExpressionsInvalidComputationComparison() {
        SList slist = buildTestAst("testOperatorsAndExpressionsInvalidComputationComparison.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Comparing different types should result in an error");
    }

    // ===== Complex Nested Structures =====
    @Test
    public void testComplexNestedStructure() {
        SList slist = buildTestAst("ComplexNestedStructure.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.VOID, result, "Testing a complex nested structure should not give an error");
    }

    // ===== Ternary Operator =====
    @Test
    public void testTernaryOperatorSuccess() {
        SList slist = buildTestAst("TernaryOperatorSuccess.txt");
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
        SList slist = buildTestAst("TernaryOperatorFail.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Computing a ternary operator with incompatible types should result in an error");
    }

    // ===== Type Conversion =====
    @Test
    public void testTypeConversionSuccess() {
        SList slist = buildTestAst("TypeConversionSuccess.txt");
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
        SList slist = buildTestAst("testTypeConversionErrorBoolToInt.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a bool to an int should result in an error");
    }

    @Test
    public void testTypeConversionErrorBoolToDouble() {
        SList slist = buildTestAst("testTypeConversionErrorBoolToDouble.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a bool to a double should result in an error");
    }

    @Test
    public void testTypeConversionErrorStringToInt() {
        SList slist = buildTestAst("testTypeConversionErrorStringToInt.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a string to an int should result in an error");
    }

    @Test
    public void testTypeConversionErrorStringToDouble() {
        SList slist = buildTestAst("testTypeConversionErrorStringToDouble.txt");
        TypeCheck result = slist.accept(typeVisitor);

        assertEquals(TypeCheck.ERROR, result, "Converting a string to a double should result in an error");
    }

}