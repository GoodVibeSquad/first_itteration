import java.io.IOException;
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


    }

}
