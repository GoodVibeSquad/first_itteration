import Parser.*;
import Parser.TableGenerator.TableGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Ask the user for the file name
        System.out.print("Enter the file name: ");
        String filePathName = scanner.nextLine();  // Get the file name from the user
        TokenGetter tokenGetter = new TokenGetter(filePathName);
        tokenGetter.initialize();
        System.out.println("Tokens" + tokenGetter.getTokens());

        // System.out.println("Total number of tokens: " + tokens.size());

        Grammar grandma = GrammarBuilder.createSimpleGrammar();
//        TableGenerator.buildSLRParser(grandma);

        Map<Integer, Map<String, String>> actionTable = TableGenerator.actionTable;
        Map<Integer, Map<String, Integer>> gotoTable = TableGenerator.gotoTable;
        
    }

}
