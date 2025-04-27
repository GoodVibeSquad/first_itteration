import Ast.*;
import CodeGeneration.CodeGenVisitor;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);

        // Ask the user for the file name
//        System.out.print("Enter the file name: ");
//        String filePathName = scanner.nextLine();  // Get the file name from the user
//        TokenGetter tokenGetter = new TokenGetter(filePathName);
//        tokenGetter.initialize();
//        System.out.println("Tokens" + tokenGetter.getTokens());

//        Grammar grammar = GrammarBuilder.createGrammar();
//        Parser parser = new Parser("myFile.txt", grammar);
//        parser.run();


        TokenGetter tokenGetter = new TokenGetter("myFile.txt");
        tokenGetter.initialize();

//        List<String> input = new ArrayList<>();
//
//        for(Token token : tokenGetter.getTokens()) {
//            input.add(token.getType().toString());
//        }

        Grammar grammar = GrammarBuilder.createGrammar();
        new TableGenerator(grammar);


        List<Token> tokens = tokenGetter.getTokens();
//        for (Token token : tokens) {
//            System.out.println(token);
//        }

//        for (int i = 0; i < 1;i++){
//            Parser.parse(tokens);
//            System.out.println("Parsing iteration: " + i);
//        }

        Object astRoot = Parser.parse(tokens);

        CodeGenVisitor codeGenVisitor = new CodeGenVisitor();
        String parsedCode = codeGenVisitor.generate((Statement) astRoot);

        //System.out.println(parsedCode);

        try (FileWriter writer = new FileWriter("pythonKode.py")) {
            writer.write(parsedCode);
            System.out.println("Output skrevet til " + "pythonKode.py");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Get the filename of the file
            String pythonScriptPath = "pythonKode.py";

            // Construct the command for the terminal
            String command = "python " + pythonScriptPath;

            // Execute the command
            Process p = Runtime.getRuntime().exec(command);

            // Print stack trace from python using a reader
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);  // Print each line of the Python script's output to the terminal
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


