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
        if (EOF) return '\0';
        try {
            int nextCharInt = reader.read();
            if (nextCharInt == -1) {
                EOF = true;
                return '\0';
            } else {
                this.nextChar = (char) nextCharInt;
                return this.nextChar;
            }
        } catch (IOException e) {
            EOF = true;
            return '\0';
        }
    }

    public boolean isEOF() {
        return this.EOF;
    }
}