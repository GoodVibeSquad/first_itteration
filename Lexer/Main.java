import java.util.List;

//HVORFOR VIRKER DET IKKE???

public class Main {
    public static void main(String[] args) {
        // Sample input code to be tokenized
        String code = "int x = 10;\nprint(x);\n// This is a comment\n/* Multi-line \n comment */";

        String lexed="0"; 
        System.out.println(lexed);
        // Create a Lexer instance with the sample code
        Lexer lexer = new Lexer(code);

        // Tokenize the input code
        List<Token> tokens = lexer.tokenize();

        // Iterate through the tokens and print their type and value
        for (Token token : tokens) {
            lexed.concat(token.getType() + " -> " + token.getValue());
        }
        System.out.println(lexed);
        
    } 
}
