import java.util.List;

//HVORFOR VIRKER DET IKKE???
//Det virker nu :=)
public class Main {
    public static void main(String[] args) {
        // Sample input code to be tokenized
        String code = "x = 10;\nprint(x);\n// This is a comment\n/* Multi-line \n comment */";

        // Create a Lexer instance with the sample code
        Lexer lexer = new Lexer(code);

        // Tokenize the input code
        List<Token> tokens = lexer.tokenize();

        System.out.println(tokens.toString());

    } 
}
