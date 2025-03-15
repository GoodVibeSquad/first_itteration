import Lexer.Token;
import Lexer.Lexer;
import Lexer.SourceCodeReader2;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import Lexer.TokenType;

public class Main {
    public static void main(String[] args) throws IOException {
        File currentDir = new File ("src/CodeFiles");
        String basePath = currentDir.getCanonicalPath();
        System.out.println(basePath);

        Scanner scanner = new Scanner(System.in);

        // Ask the user for the file name
        System.out.print("Enter the file name: ");
        String filePathName = scanner.nextLine();  // Get the file name from the user

        SourceCodeReader2 reader = new SourceCodeReader2(basePath + "/" + filePathName);


        Lexer lexer = new Lexer(reader);
        List<Token> tokens = new ArrayList<>();

        Token token;
        while ((token = lexer.tokenize()).getType() != TokenType.EOF) {
            tokens.add(token);
            System.out.println(token);
        }



    }

}
