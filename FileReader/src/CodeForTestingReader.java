import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class CodeForTestingReader {
    public CodeForTestingReader() throws FileNotFoundException {
    }

    public static void main(String[] args) throws Throwable {
        //SourceCodeReader reader = new SourceCodeReader("C:\\Users\\peter\\Desktop\\4. semester\\Project\\FileReader\\src\\myFile.txt");

        File currentDir = new File ("src/CodeFiles");
        String basePath = currentDir.getCanonicalPath();
        System.out.println(basePath);

        Scanner scanner = new Scanner(System.in);

        // Ask the user for the file name
        System.out.print("Enter the file name: ");
        String filePathName = scanner.nextLine();  // Get the file name from the user



        SourceCodeReader2 reader = new SourceCodeReader2(basePath + "/" + filePathName);
        while(!reader.isEOF()) {
            System.out.println("Current char: " + reader.currentChar());
            System.out.println("Peak value: " + reader.peek());
            reader.peek();
            reader.advance();
            System.out.println("\n");
        }

    }



}