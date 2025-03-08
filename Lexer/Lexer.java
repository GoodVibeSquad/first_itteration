import java.util.ArrayList;
import java.util.List; 
 
public class Lexer {
    private String input;
    private int position = 0; 
    private final List<Token> tokens = new ArrayList<>();
    

    public Lexer(String input){
        this.input = input;
    }

    public List<Token> tokenize() {  
        while (position < input.length()) {
            char currentChar = input.charAt(position);

            //tjekker keyword
            Token token = checkIfTokenExists();
            if (token != null) {
                tokens.add(token);
                continue;
            }

            //Whitespace
            if (Character.isWhitespace(currentChar)) {
                position++;
                continue;
            }

            //Single line multi line comment 
            if (currentChar == '/' && peekChar() == '/' ) {
                skipSingleLineComment();
                continue;
            }

            if(currentChar == '/' && peekChar() == '*') {
                skipMultiLineComment();
                continue;
            }


            //MAtch identifiers
            if(Character.isLetterOrDigit(currentChar) || currentChar == '_'){
                tokens.add(scanIdentifier());
                continue;
            }

            //Match numbers 
            if(Character.isDigit(currentChar)){
                tokens.add(scanNumber());
                continue;
            }

            //Match string literals
            if(currentChar == '"'){
                tokens.add(scanString());
                continue;
            }
            
        }
        
        //EOF anvendes som en token for at indikere at der ikke er mere i inputstringen.
        tokens.add(new Token(TokenType.EOF, "EOF"));
        return tokens; 
    }


    private Token checkIfTokenExists(){
        //Tjekker gennem enum af tokens
        for (TokenType type : TokenType.values()) {
            //Tjekker om input er inde i enum listen og returner en token hvis den er
            String symbol = type.name();
            if (symbol != null && input.startsWith(symbol, position)) {
                position += symbol.length();
                return new Token(type, symbol);
            }
        }
        return null;

    }

    private Token scanIdentifier(){
        //Vi definerer hvor vores nuværende position er i vores input string
        int start = position; 

        //Tjek om den char vi læser er en identifier
        while (position < input.length() && (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            position++;
        }

        //Definer value som den del af input der blev læst som en identifier
        String value = input.substring(start, position);

        TokenType type; 
        //tjekker om vores læste string er et keyword i vores enum
            if (TokenType.tokenTypeMap.containsKey(value)) {
                type = TokenType.tokenTypeMap.get(value); // Hvis ja, brug den matchende TokenType
            } else {
                type = TokenType.STRING; // Gør standard til STRING, hvis ikke fundet
            }

        return new Token(type, value);
    }

    private Token scanNumber(){
        int start = position;
        boolean isDecimal = false; //Boolean til at tjekke om vi spotter et dot i vores læste input

        //Tjekker om den læste string er et tal eller har en dot
        while (position < input.length() && Character.isDigit(input.charAt(position)) || (input.charAt(position) == '.')) {

            if((input.charAt(position) == '.')) {
                isDecimal = true;
            } 
            position++;
        } 

        if(isDecimal) {
            return new Token(TokenType.DOUBLE, input.substring(start, position));
        } else { 
            return new Token(TokenType.INT, input.substring(start, position));
        }     
    }

    private Token scanString() {
    int start = position + 1; //for at skippe opening quote.
   
        while (position < input.length() && input.charAt(position) != '"') {
            position ++;
        }
        
        String value = input.substring(start, position);

        if (position < input.length() && input.charAt(position) == '"') { 
            position++; //For at skippe lukke quotes
        }

     return new Token(TokenType.STRING, value);
    }

    private void skipSingleLineComment(){
        while(position < input.length() && input.charAt(position) != '\n') {
            position++;
        }
        position++; //Skipper \n så den kan læse stringen til næste function

    }

    private void skipMultiLineComment(){
        position += 2;
        //-1 sikre vi ikke går forbi slutningen af stringen fordi vi læser to chars på én gang.
        while (position < input.length() - 1 && !(input.charAt(position) == '*' && input.charAt(position+1)== '/')) {
            position++;
        }
        position += 2;

    }

    private char peekChar() {
        if(position + 1 < input.length()){
            return input.charAt(position + 1);
        } else {
        return '\0';
        }
    }


    
    
}

