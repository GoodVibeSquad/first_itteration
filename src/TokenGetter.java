import Lexer.Lexer;
import Lexer.SourceCodeReader2;
import Lexer.Token;
import Lexer.TokenType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokenGetter {
    private SourceCodeReader2 reader;
    private List<Token> tokens;
    private Lexer lexer;
    private File sourceCodeDir;
    private String basePath;

    TokenGetter(String filePathName){
        this.tokens = new ArrayList<Token>();
        this.reader = reader;
        this.sourceCodeDir = new File ("src/CodeFiles");
        try {
            this.basePath = sourceCodeDir.getCanonicalPath();
            this.reader = new SourceCodeReader2(basePath + "/" + filePathName);
            this.lexer = new Lexer(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize(){
        Token token;
        while ((token = this.lexer.tokenize()).getType() != TokenType.EOF) {
            tokens.add(token);
        }
    }

    public List<Token> getTokens(){
        return tokens;
    }
}
