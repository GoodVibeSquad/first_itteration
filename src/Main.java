import Ast.*;
import CodeGeneration.CodeGenerator;
import Parser.*;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenGetter;
import TypeChecking.SymbolTable;
import TypeChecking.TypeCheck;
import TypeChecking.TypeCheckerVisitor;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Input file name or path: \n");
        Scanner scanner = new Scanner(System.in);


        String filePathName = scanner.nextLine();  // Get the file name from the user



        TokenGetter tokenGetter = new TokenGetter(filePathName);
        tokenGetter.initialize();


        Grammar grammar = GrammarBuilder.createGrammar();
        new TableGenerator(grammar);


        List<Token> tokens = tokenGetter.getTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }

        SList slist = Parser.parse(tokens);

        SymbolTable symbols = new SymbolTable();

        TypeCheckerVisitor typeVisitor = new TypeCheckerVisitor(symbols);
        TypeCheck result = slist.accept(typeVisitor);
        System.out.println("targetType check result: " + result);
        if (result == TypeCheck.ERROR) {
            throw new RuntimeException("Type checking failed");
        }
        symbols.clear();

        CodeGenerator generator = new CodeGenerator(slist);

        String dirName = replaceFileExtension(filePathName,"py");
        generator.generate(dirName);
    }

    public static String replaceFileExtension(String path, String newExtension) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex == -1) {
            // No extension found, just add the new one
            return path + "." + newExtension;
        } else {
            // Replace existing extension
            return path.substring(0, dotIndex + 1) + newExtension;
        }
    }
}
