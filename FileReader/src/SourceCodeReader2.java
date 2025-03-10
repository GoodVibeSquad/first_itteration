import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceCodeReader2 {
    private boolean EOF;
    private String filepath;
    private char nextChar;
    private String sourceCode;
    private int index;

    SourceCodeReader2(String filepath) throws IOException {
        this.filepath = filepath;
        this.index = 0;
        this.sourceCode = Files.readString(Path.of(filepath));
        this.EOF = sourceCode.isEmpty();
        this.nextChar = 0;

        // Advances reader to first char
        this.advance();
    }

    public char currentChar() {
        if(this.EOF) return '\0';
        return this.nextChar;
    }

    public char peek() {
        if (this.EOF || this.index >= sourceCode.length()) return '\0'; // Check if index is out of bounds
        return sourceCode.charAt(this.index);
    }
    public char advance() {
        if (index < sourceCode.length()) {
            this.nextChar = sourceCode.charAt(index);
        } else {
            this.EOF = true;
            this.nextChar = '\0';
            return this.nextChar;
        }
        index++;
        return nextChar;
    }

    public boolean isEOF() {
        return this.EOF;
    }
}