package Compiler.Lexer;

//define the token class
public class Token {
    private final TokenType type;
    private final String value;

    public Token(TokenType type){
        this.type = type;
        this.value = null;
    }

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
  
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Token)) return false;
        if (!super.equals(object)) return false;
        Token token = (Token) object;
        return java.util.Objects.equals(type, token.type) && java.util.Objects.equals(value, token.value);
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), type, value);
    }
}




