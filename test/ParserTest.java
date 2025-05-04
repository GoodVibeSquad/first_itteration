import Ast.Slist;
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

    static String testDir = "test/TestFiles/Parsing/";

    @BeforeAll
    public static void setUp() {
        Grammar grammar = GrammarBuilder.createGrammar();
        new TableGenerator(grammar);
    }

    private void runTestFile(String filename, boolean expectSuccess) {
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
    @Test
    @DisplayName("01_ShiftReduce")
    public void testShiftReduce() {
        runTestFile("01_ShiftReduce.txt", true);
    }

    // === Constants ===
    @Test
    @DisplayName("02_Constants")
    public void testConstants() {
        runTestFile("02_Constants.txt", true);
    }

    // === Expressions ===
    @Test
    @DisplayName("03_Expressions")
    public void testExpressions() {
        runTestFile("03_Expressions.txt", true);
    }

    @Test
    @DisplayName("03_Expressions_Errors")
    public void testExpressionErrors() {
        runTestFile("03_Expressions_Errors.txt", false);
    }

    // === Statements ===
    @Test
    @DisplayName("04_Statements")
    public void testStatements() {
        runTestFile("04_Statements.txt", true);
    }

    @Test
    @DisplayName("04_Statements_Errors")
    public void testStatementErrors() {
        runTestFile("04_Statements_Errors.txt", false);
    }

    // === Identifiers ===
    @Test
    @DisplayName("05_Identifiers")
    public void testIdentifiers() {
        runTestFile("05_Identifiers.txt", true);
    }

    @Test
    @DisplayName("05_Identifiers_Errors")
    public void testIdentifierErrors() {
        runTestFile("05_Identifiers_Errors.txt", false);
    }

    // === Edge Cases ===
    @Test
    @DisplayName("06_EmptyInput")
    public void testEmptyInput() {
        runTestFile("06_EmptyInput.txt", false);
    }

    @Test
    @DisplayName("07_NoNewlinesMultipleStatements")
    public void testNoNewlinesMultipleStatements() {
        runTestFile("07_NoNewlinesMultipleStatements.txt", true);
    }

    @Test
    @DisplayName("08_DeeplyNestedExpressions")
    public void testDeeplyNestedExpressions() {
        runTestFile("08_DeeplyNestedExpressions.txt", true);
    }

    @Test
    @DisplayName("09_EmptyBlocks")
    public void testEmptyBlocks() {
        runTestFile("09_EmptyBlocks.txt", false);
    }

    @Test
    @DisplayName("10_MisusedKeywords")
    public void testMisusedKeywords() {
        runTestFile("10_MisusedKeywords.txt", false);
    }

    @Test
    @DisplayName("11_DanglingElse")
    public void testDanglingElse() {
        runTestFile("11_DanglingElse.txt", true);
    }

    @Test
    @DisplayName("12_ExpressionListEdgeCases")
    public void testExpressionListEdgeCases() {
        runTestFile("12_ExpressionListEdgeCases.txt", false);
    }

    @Test
    @DisplayName("13_DuplicateDeclaration")
    public void testDuplicateDeclaration() {
        runTestFile("13_DuplicateDeclaration.txt", true);
    }
}
