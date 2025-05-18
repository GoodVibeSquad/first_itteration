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

        // Execute the Python script using Runtime.exec
        try {
            // Get the filename of the file
            String pythonScriptPath = "pythonKode.py";

            // Construct the command for the terminal
            String command = "python " + pythonScriptPath;

            // Execute the command to run our program, but in python.
            Process p = Runtime.getRuntime().exec(command);

            // Print stack trace from python using a reader to the java terminal
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Wait for the process to finish
            p.waitFor();
            System.out.println("Python script executed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Failed to execute the Python script.");
        }
    }
}
