import Ast.SList;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;
import TypeChecking.*;
import CodeGeneration.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

public class CodeGeneratorTest {

    static String testDir = "test/TestFiles/CodeGeneration/";

    @BeforeEach
    public void setUp() {
        Grammar grammar = GrammarBuilder.createGrammar();
        new TableGenerator(grammar);
    }

    public SList buildTestAst(String filename) {
        TokenGetter tokenGetter = new TokenGetter(filename, testDir);
        tokenGetter.initialize();
        List<Token> tokens = tokenGetter.getTokens();
        return Parser.parse(tokens);
    }

    public String runGeneratedPythonCode(String filename) {
        SList slist = buildTestAst(filename);
        SymbolTable symbols = new SymbolTable();
        TypeCheckerVisitor typeVisitor = new TypeCheckerVisitor(symbols);
        TypeCheck result = slist.accept(typeVisitor);
        if (result == TypeCheck.ERROR) {
            throw new RuntimeException("Type checking failed");
        }
        System.out.println(result);
        CodeGenerator generator = new CodeGenerator(slist);
        generator.generate("testPythonCode.py");

        // Capture the output from executing the Python file
        StringBuilder output = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec("python testPythonCode.py");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString().trim();
    }

    @Test
    @DisplayName("01_testEmptyProgram")
    public void testEmptyProgram() {
        assertThrows(RuntimeException.class, () -> {
            SList slist = buildTestAst("EmptyProgram.txt");
            CodeGenerator generator = new CodeGenerator(slist);
            generator.generate("testPythonCode.py");
        });

    }

    @Test
    @DisplayName("02_testPrintNone")
    public void testPrintNone() {
        assertThrows(RuntimeException.class, () -> {
            SList slist = buildTestAst("PrintNone.txt");
            CodeGenerator generator = new CodeGenerator(slist);
            generator.generate("testPythonCode.py");  // or runGeneratedPythonCode("PrintNone.txt");
        });
    }

    @Test
    @DisplayName("03_testForLoopZeroIterations")
    public void testForLoopZeroIterations() {
        String output = runGeneratedPythonCode("ForLoopZeroIterations.txt");
        assertEquals("", output);
    }

    @Test
    @DisplayName("04_testWhileTrueInfinite")
    public void testWhileTrueInfinite() {
        String output = runGeneratedPythonCode("WhileTrueInfinite.txt");
        assertEquals("0", output);
    }


    @Test
    @DisplayName("05_testConstructorInvalidArgs")
    public void testConstructorInvalidArgs() {
        assertThrows(RuntimeException.class, () -> {
            // Attempt to parse, generate, and execute code for invalid constructor
            runGeneratedPythonCode("ConstructorInvalidArgs.txt");
        }, "Expected a RuntimeException due to invalid constructor arguments");
    }

    @Test
    @DisplayName("06_testVariableShadowing")
    public void testVariableShadowing() {
        assertThrows(RuntimeException.class, () -> {runGeneratedPythonCode("VariableShadowing.txt");
            },"Expected a RuntimeException due to variable shadowing");

    }

    @Test
    @DisplayName("07_testEmptyIfElse")
    public void testEmptyIfElse() {
        assertThrows(RuntimeException.class, () -> {runGeneratedPythonCode("EmptyIfElse.txt");
        },"Expected a RuntimeException due to empty if-else statement");

    }

    @Test
    @DisplayName("08_TernaryMixedTypes")
    public void testTernaryMixedTypes() {
        assertThrows(RuntimeException.class, () -> {runGeneratedPythonCode("TernaryMixedTypes.txt");
        },"Expected a RuntimeException due to invalid ternary operator");
    }

    @Test
    @DisplayName("09_UnsupportedCastBoolToInt")
    public void testUnsupportedCastBoolToInt() {
        assertThrows(RuntimeException.class, () -> {runGeneratedPythonCode("UnsupportedCastBoolToInt.txt");
        }, "Expected a RuntimeException due to unsupported targetType conversion");
    }

    @Test
    @DisplayName("10_IncompleteAssignment")
    public void testIncompleteAssignment() {
        assertThrows(RuntimeException.class, () -> {runGeneratedPythonCode("IncompleteAssignment.txt");
        }, "Expected a RuntimeException due to incomplete assignment");
    }

    @Test
    @DisplayName("11_testValidBasicPrint")
    public void testValidBasicPrint() {
        String output = runGeneratedPythonCode("ValidBasicPrint.txt");
        assertEquals("5", output);
    }

    @Test
    @DisplayName("12_testValidConstructorUsage")
    public void testValidConstructorUsage() {
        String output = runGeneratedPythonCode("ValidConstructorUsage.txt");
        assertTrue(output.contains("constructed"));
    }

    @Test
    @DisplayName("13_testValidTernaryUse")
    public void testValidTernaryUse() {
        String output = runGeneratedPythonCode("ValidTernaryUse.txt");
        assertEquals("1", output.trim(), "Ternary should assign 1 when condition is true");
    }

    @Test
    @DisplayName("14_testLoopIteration")
    public void testLoopIteration() {
        String output = runGeneratedPythonCode("LoopIterationTest.txt").trim();
        assertEquals("0\n1\n2", output, "Expected loop output from 0 to 2");
    }

    @Test
    @DisplayName("15_testMathLibraryUsage")
    public void testMathLibraryUsage() {
        String output = runGeneratedPythonCode("MathLibraryUsage.txt");
        assertTrue(output.contains("3.14") || output.contains("3.141"), "Should print approximate expr of pi");
    }

}
