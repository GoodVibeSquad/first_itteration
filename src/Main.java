import Ast.*;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;
import Tokens.TokenType;
import TypeChecking.SymbolTable;
import TypeChecking.TypeCheck;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


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

        CodeGen codeGen = new CodeGen();
        String parsedCode = codeGen.generate((Statement) astRoot);

        //System.out.println(parsedCode);

        try (FileWriter writer = new FileWriter("pythonKode.py")) {
            writer.write(parsedCode);
            System.out.println("Output skrevet til " + "pythonKode.py");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
        
}


