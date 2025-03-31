import Lexer.Lexer;
import CodeReader.SourceCodeReader2;
import Tokens.Token;
import Tokens.TokenType;

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
