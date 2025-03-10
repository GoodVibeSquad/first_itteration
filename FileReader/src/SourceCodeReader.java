import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SourceCodeReader {
    private BufferedReader reader;
    private boolean EOF;
    private String filepath;
    private char nextChar;

    public SourceCodeReader(String filepath) throws Throwable {
        this.filepath = filepath;

        // We use bufferedReader for efficient reading in buffers(chunks)
        // Since a normal reader only reads one char at a time which is inefficient
        // Buffereadreader reads multiple(but we use Filereader inside to only read 1)
        this.reader = new BufferedReader(new FileReader(filepath));
        this.nextChar = 0;

        // Advances reader to first char
        this.advance();
    }


    public char peek() {
        return this.nextChar;
    }


    public char advance() {
        try {
            int nextChar = reader.read();
            if (nextChar == -1) {
                this.EOF = true;
                return '\0'; // Null character indicates EOF
            }
            this.nextChar = (char) nextChar;
            return (char) nextChar;
        } catch (IOException e) {
            this.EOF = true; // Mark as EOF to prevent further reads
            return '\0';
        }
    }

    public boolean isEOF(){
        return this.EOF;
    }
}