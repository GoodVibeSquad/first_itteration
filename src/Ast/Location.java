package Ast;

// Each part of the actual source code split into columns and lines
public class Location {
    int line, column;

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }
}
