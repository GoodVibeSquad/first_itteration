import Ast.*;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.TokenGetter;
import Tokens.TokenType;


import java.io.IOException;
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
