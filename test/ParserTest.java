import Ast.*;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private record ParseResult(boolean success, Slist ast, String errorMsg) {}

    private ParseResult parseFile(String filename) {
        try {
            TokenGetter tokenGetter = new TokenGetter(filename, testDir);
            tokenGetter.initialize();
            List<Token> tokens = tokenGetter.getTokens();
            Slist ast = Parser.parse(tokens);
            return new ParseResult(true, ast, null);
        } catch (Exception e) {
            return new ParseResult(false, null, e.getMessage());
        }
    }
    private void assertParseError (String filename) {
        ParseResult result = parseFile(filename);
        assertFalse(result.success(), "Parser should fail for invalid expressions");
        assertNotNull(result.errorMsg(), "Error message should not be null");
    }

    static String testDir = "test/TestFiles/Parsing/";

    @BeforeAll
    public static void setUp() {
        Grammar grammar = GrammarBuilder.createGrammar();
        new TableGenerator(grammar);
    }

    private void validateSyntax(String filename, boolean expectSuccess) {
        TokenGetter tokenGetter = new TokenGetter(filename, testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();

        try {
            Slist slist = Parser.parse(tokens);
            if (!expectSuccess) {
                fail("Expected syntax error, but parsing succeeded for " + filename);
            } else {
                assertNotNull(slist);
                System.out.println("Parsed successfully: " + filename);
            }
        } catch (RuntimeException e) {
            if (expectSuccess) {
                e.printStackTrace();
                fail("Unexpected syntax error: " + e.getMessage());
            } else {
                System.out.println("Correctly failed: " + filename);
            }
        }
    }



    // === Shift/Reduce ===
    //------------------------------------------------------------------------------------
    @Test
    @DisplayName("01 ShiftReduce")
    public void testShiftReduce() {
        ParseResult result = parseFile("01_ShiftReduce.txt");
        assertTrue(result.success, "Parser should successfully parse valid code");
        
        // Check that we get exactly one statement
        assertEquals(1, result.ast.elements().size(), "Should have exactly one statement");
        
        // Check that it's the correct type
        Statement stmt = result.ast.elements().getFirst();
        assertTrue(stmt instanceof SExpression, "Statement should be an SExpression");
        
        // Check the internal structure
        SExpression sExpr = (SExpression) stmt;
        assertTrue(sExpr.value() instanceof Econstant, "Expression should be an Econstant");
        
        // Optionally print the parse tree to verify shift/reduce actions
        System.out.println("Parse result: " + result.ast);
    }
    // ^^^^ MAYBE MAKE SOME MORE TESTS OF THIS TYPE ^^^^
    //------------------------------------------------------------------------------------

    // === Constants ===
    @Test
    @DisplayName("02 Constants")
    public void testConstants() {
        ParseResult result = parseFile("02_Constants.txt");
        assertTrue(result.success, "Parser should succeed for valid constants");
        
        for (Statement stmt : result.ast.elements()) {
            assertTrue(stmt instanceof SExpression, "Statement should be SExpression, because constants are expressions, got " + stmt.getClass().getSimpleName() + " instead.");
            SExpression sExpr = (SExpression) stmt;
            assertTrue(sExpr.value() instanceof Econstant, 
                sExpr.value() + " should be constant, got " + sExpr.value().getClass().getSimpleName());
        }
    }

    // === Expressions ===
    @Test
    @DisplayName("03 Expressions")
    public void testExpressions() {
        ParseResult result = parseFile("03_Expressions.txt");
        assertTrue(result.success, "Parser should succeed for valid expressions");
        for (Statement stmt : result.ast.elements()) {
            assertTrue(stmt instanceof SExpression, "Expected SExpression, got " + stmt.getClass().getSimpleName());
        }
    }

    // === Syntax Errors ===
    @Test
    @DisplayName("04 Missing Parentheses")
    public void testMissingParenthesis() {
        assertParseError("04_MissingParentheses.txt");
    }

    @Test
    @DisplayName("04 Incomplete Binary Operation")
    public void testIncompleteBinaryOperations() {
        assertParseError("04_IncompleteBinaryOperation.txt");
    }

    @Test
    @DisplayName("04 Missing Semicolon")
    public void testMissingSemicolon() {
        assertParseError("04_MissingSemicolon.txt");
    }

    @Test
    @DisplayName("04 Double Operators")
    public void testDoubleOperator() {
        assertParseError("04_DoubleOperators.txt");

    }

    // === Function Call Errors ===
    @Test
    @DisplayName("05 Missing Function Arguments")
    public void testMissingFunctionArguments() {
        assertParseError("05_MissingFunctionArguments.txt");
    }
    @Test
    @DisplayName("05 Extra Comma")
    public void testExtraComma() {
        assertParseError("05_ExtraComma.txt");
    }
    @Test
    @DisplayName("05 Missing Closed Parenthesis")
    public void testMissingClosedParenthesis() {
        assertParseError("05_MissingClosedParenthesis.txt");
    }

    @Test
    @DisplayName("05 Invalid Argument Lists\n")
    public void testMissingComma() {
        assertParseError("05_InvalidArgumentList.txt");
    }

    // === Type Cast Errors ===
    @Test
    @DisplayName("06 Missing Target Type")
    public void testInvalidTypeCast() {
        assertParseError("06_MissingTargetType.txt");
    }
    @Test
    @DisplayName("06 Invalid Cast Syntax")
    public void testInvalidCastSyntax() {
        assertParseError("06_InvalidCastSyntax.txt");
    }
    @Test
    @DisplayName("06 Missing Expression")
    public void testCastMissingExpression() {
        assertParseError("06_MissingExpression.txt");
    }

    // === Ternary Operator Errors ===
    @Test
    @DisplayName("07 Ternary Missing Colon")
    public void testTernaryMissingColon() {
        assertParseError("07_TernaryMissingColon.txt");
    }
    @Test
    @DisplayName("07 Ternary Missing Condition")
    public void testTernaryMissingCondition() {
        assertParseError("07_TernaryMissingCondition.txt");
    }
    @Test
    @DisplayName("07 Ternary Missing Then")
    public void testTernaryMissingThen() {
        assertParseError("07_TernaryMissingThen.txt");
    }

    // === Object Construction/Method Calls ===

   @Test
   @DisplayName("08 Invalid New Syntax")
   public void testInvalidNewSyntax() {
        assertParseError("08_InvalidNewSyntax.txt");
   }
   @Test
   @DisplayName("08 Missing Constructor Arguments")
   public void testMissingConstructorArguments() {
        assertParseError("08_MissingConstructorArguments.txt");
   }
  @Test
  @DisplayName("08 Invalid Method Call Syntax")
  public void testInvalidMethodCallSyntax() {
        assertParseError("08_InvalidMethodCallSyntax.txt");
  }
  @Test
  @DisplayName("08 Missing Method Name")
  public void testMissingMethodName() {
        assertParseError("08_MissingMethodName.txt");
  }

    // === Valid Statements ===

    @Test
    @DisplayName("09 Simple Statements ")
    public void testSimpleStatements() {
        ParseResult result = parseFile("09_SimpleStatements.txt");
        assertTrue(result.success, "Parser should succeed for valid simple statements");
    }
    @Test
    @DisplayName("10 If Statements")
    public void testIfStatements() {
        ParseResult result = parseFile("10_IfStatements.txt");
        assertTrue(result.success, "Parser should succeed for valid if statements");
    }
    @Test
    @DisplayName("11 Nested If Statements")
    public void testNestedIfStatements() {
        ParseResult result = parseFile("11_NestedIfStatements.txt");
        assertTrue(result.success, "Parser should succeed for valid nested if statements");
    }
    @Test
    @DisplayName("12 For Loops")
    public void testForLoops() {
        ParseResult result = parseFile("12_ForLoops.txt");
        assertTrue(result.success, "Parser should succeed for valid for loops");
    }

    @Test
    @DisplayName("13 While Loops")
    public void testWhileLoops() {
        ParseResult result = parseFile("13_WhileLoops.txt");
        assertTrue(result.success, "Parser should succeed for valid while loops");
    }

    @Test
    @DisplayName("14 Break/Continue")
    public void testBreakContinue() {
        ParseResult result = parseFile("14_BreakContinue.txt");
        assertTrue(result.success, "Parser should succeed for valid break/continue statements");
    }

    @Test
    @DisplayName("15 Nested Loops")
    public void testNestedLoops() {
        ParseResult result = parseFile("15_NestedLoops.txt");
        assertTrue(result.success, "Parser should succeed for valid nested loops");
    }

    @Test
    @DisplayName("16 Block Statements")
    public void testBlockStatements() {
        ParseResult result = parseFile("16_BlockStatements.txt");
        assertTrue(result.success, "Parser should succeed for valid block statements");
    }

    @Test
    @DisplayName("17 Mixed Complex Statements")
    public void testMixedComplexStatements() {
        ParseResult result = parseFile("17_MixedComplexStatements.txt");
        assertTrue(result.success, "Parser should succeed for valid complex mixed statements");
    }

    // === If Statement Errors ===


    @Test
    @DisplayName("18 Missing parentheses around condition\n")
    public void testMissingParenthesesAroundIfCondition() {
        assertParseError("18_MissingParenthesesAroundIfCondition.txt");
    }

    @Test
    @DisplayName("18 Empty IF Condition")
    public void testEmptyIfCondition() {
        assertParseError("18_EmptyIfCondition.txt");
    }

    @Test
    @DisplayName("18 Empty IF Body")
    public void testEmptyIfBlock() {
        assertParseError("18_EmptyIfBody.txt");
    }

    @Test
    @DisplayName("18 Missing then block before else")
    public void testMissingThenBlockBeforeElse() {
        assertParseError("18_MissingThenBlockBeforeElse.txt");
    }

    @Test
    @DisplayName("18 Else Without If")
    public void testElseWithoutIf() {
        assertParseError("18_ElseWithoutIf.txt");
    }


    // === Block Statement Errors ===

   @Test
   @DisplayName("19 Unclosed Block")
   public void testUnclosedBlock() {
        assertParseError("19_UnclosedBlock.txt");
   }

    @Test
    @DisplayName("19 Block without opening brace")
    public void testBlockWithoutOpeningBrace() {
        assertParseError("19_BlockWithoutOpeningBrace.txt");
    }

    // === For Loop Errors ===
    @Test
    @DisplayName("20 For Loop Missing Initializer")
    public void testForLoopMissingInitializer() {
        assertParseError("20_ForLoopMissingInitializer.txt");
    }

    @Test
    @DisplayName("20 For Loop Missing Condition")
    public void testForLoopMissingCondition() {
        assertParseError("20_ForLoopMissingCondition.txt");
    }
    @Test
    @DisplayName("20 For Loop Missing Increment")
    public void testForLoopMissingIncrement() {
        assertParseError("20_ForLoopMissingIncrement.txt");
    }
    @Test
    @DisplayName("20 For Loop Missing Block")
    public void testForLoopEmptyBody() {
        assertParseError("20_ForLoopEmptyBody.txt");
    }

    // === While Loop Errors ===
    @Test
    @DisplayName("21 While Loop Empty Condition")
    public void testWhileLoopMissingCondition() {
        assertParseError("21_WhileLoopEmptyCondition.txt");
    }
    @Test
    @DisplayName("21 While Loop Missing Body")
    public void testWhileLoopEmptyBody() {
        assertParseError("21_WhileLoopMissingBody.txt");
    }
    @Test
    @DisplayName("21 While Loop Missing Everything")
    public void testWhileLoopMissingEverything() {
        assertParseError("21_WhileLoopMissingEverything.txt");
    }

    // === Break/Continue Errors ===
    @Test
    @DisplayName("22 Break With Expression")
    public void testBreakWithExpression() {
        assertParseError("22_BreakWithExpression.txt");
    }
    @Test
    @DisplayName("22 Continue With Expression")
    public void testContinueWithExpression() {
        assertParseError("22_ContinueWithExpression.txt");
    }

    // === Variable Declaration Errors ===
    @Test
    @DisplayName("23 Variable Declaration Without Identifier")
    public void testVariableDeclarationWithoutInitializer() {
        assertParseError("23_VariableDeclarationWithoutIdentifier.txt");
    }
    // -------------------------
//    @Test
//    @DisplayName("23 Missing Comma Between Declarations")
//    public void testMissingCommaBetweenDeclarations() {
//        assertParseError("23_MissingCommaBetweenDeclarations.txt");
//    }
    // CAN AND SHOULD WE BE ABLE TO DO THIS???
    // -------------------------

    @Test
    @DisplayName("23 Missing Initialization Value")
    public void testDeclarationMissingInitializer() {
        assertParseError("23_DeclarationMissingInitializer.txt");
    }

    @Test
    @DisplayName("23 Assignment Missing Variable Name")
    public void testAssignmentMissingVariableName() {
        assertParseError("23_AssignmentMissingVariableName.txt");
    }

    @Test
    @DisplayName("23 Assignment Without Value")
    public void testAssignmentWithoutValue() {
        assertParseError("23_AssignmentWithoutValue.txt");
    }

   @Test
   @DisplayName("23 Using Keyword In Declaration")
   public void testUsingKeywordInDeclaration() {
        assertParseError("23_UsingKeywordInDeclaration.txt");
   }

    // === Edge Cases ===

    @Test
    @DisplayName("24_EmptyInput")
    public void testEmptyInput() {
        validateSyntax("24_EmptyInput.txt", false);
    }

    @Test
    @DisplayName("24_NoNewlinesMultipleStatements")
    public void testNoNewlinesMultipleStatements() {
        validateSyntax("24_NoNewlinesMultipleStatements.txt", true);
    }

    @Test
    @DisplayName("24_DeeplyNestedExpressions")
    public void testDeeplyNestedExpressions() {
        validateSyntax("24_DeeplyNestedExpressions.txt", true);
    }

    @Test
    @DisplayName("24_EmptyBlocks")
    public void testEmptyBlocks() {
        validateSyntax("24_EmptyBlocks.txt", false);
    }

    @Test
    @DisplayName("24_MisusedKeywords")
    public void testMisusedKeywords() {
        validateSyntax("24_MisusedKeywords.txt", false);
    }

    @Test
    @DisplayName("24_DanglingElse")
    public void testDanglingElse() {
        validateSyntax("24_DanglingElse.txt", true);
    }


    // THIS NOT TYPECHECK?
    @Test
    @DisplayName("24_DuplicateDeclaration")
    public void testDuplicateDeclaration() {
        validateSyntax("24_DuplicateDeclaration.txt", true);
    }
}