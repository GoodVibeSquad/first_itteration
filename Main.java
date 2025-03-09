import Lexer.Token;
import Lexer.Lexer;
import java.util.List;
import java.util.ArrayList;
import Lexer.TokenType;

public class Main {
    public static void main(String[] args) {
        String input = "x = 10;\nprint(x);\n// This is a comment\n/* Multi-line \n comment */";

        Lexer lexer = new Lexer(input);
        List<Token> tokens = new ArrayList<>();

        Token token;
        while ((token = lexer.tokenize()).getType() != TokenType.EOF) {
            tokens.add(token);
            System.out.println(token);
        }


    }

}
