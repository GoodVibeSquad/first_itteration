import Ast.*;
import CodeGeneration.CodeGenVisitor;
import CodeGeneration.CodeGenerator;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;
import TypeChecking.SymbolTable;
import TypeChecking.TypeCheck;
import TypeChecking.TypeCheckerVisitor;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

        Slist slist = Parser.parse(tokens);


        // System.out.println("Total number of tokens: " + tokens.size());
        SymbolTable symbols = new SymbolTable();


        TypeCheckerVisitor typeVisitor = new TypeCheckerVisitor(symbols);
        TypeCheck result = slist.accept(typeVisitor);
        System.out.println("type check result: " + result);
        symbols.clear();

        CodeGenerator generator = new CodeGenerator(slist);
        generator.generate();
    }

}
