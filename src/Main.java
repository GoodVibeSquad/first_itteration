import Ast.*;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;
import Tokens.TokenType;
import TypeChecking.SymbolTable;
import TypeChecking.TypeCheck;

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
        for (Token token : tokens) {
            System.out.println(token);
        }

        List<String> tokenStream = new ArrayList<>();
        for (Token token : tokens) {
            tokenStream.add(token.getType().toString());
        }

         // your tokenized input

        for (int i = 0; i < 1000;i++){
        Parser.parse(tokenStream);
        System.out.println("Parsing iteration: " + i);
    }

        // System.out.println("Total number of tokens: " + tokens.size());
        SymbolTable symbols = new SymbolTable();
        symbols.declareVariable("x", TypeCheck.BOOL);
        symbols.declareVariable("y", TypeCheck.INT);

//        TypeCheckerVisitor typeVisitor = new TypeCheckerVisitor(symbols);
//        TypeCheck result = op.accept(typeVisitor);
//        System.out.println("type check result: " + result);
//        symbols.clear();

        
    }

}
