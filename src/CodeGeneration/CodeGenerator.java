package CodeGeneration;

import Ast.Statement;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class CodeGenerator {
    private String GeneratedCode;
    private CodeGenVisitor visitor;
    private String parsedCode;

    public CodeGenerator(Statement AstRoot) {
        this.visitor = new CodeGenVisitor();
        this.parsedCode = visitor.generate(AstRoot);
    }

    // Method to generate the code file and execute the Python script
    public void generate(String directoryPath) {
        // Write parsedCode to a Python file
        try (FileWriter writer = new FileWriter(directoryPath)) {
            writer.write(this.parsedCode);
            System.out.println("Output written to "+directoryPath+" successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write to file.");
            return;
        }

    }
}
