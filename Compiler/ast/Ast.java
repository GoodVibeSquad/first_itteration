package ast;
import java.util.List;
import Lexer.*;



public class Ast {

}


class Location {
    int line, column;
    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }
}


class Ident {
    Location loc;
    String id;

    public Ident(Location loc, String id) {
        this.loc = loc;
        this.id = id;
    }
}

// Unary operators (from TokenType)
enum UnaryOperators {
    NEG(TokenType.MINUS),  // -e
    NOT(TokenType.NEGATION); // not e

    private final TokenType token;
    UnaryOperators(TokenType token) { this.token = token; }
}

// Binary operators (from TokenType)
enum BinaryOperators {
    PLUS(TokenType.PLUS), MINUS(TokenType.MINUS), MULTIPLY(TokenType.MULTIPLY), DIVISION(TokenType.DIVISION), MODULUS(TokenType.MODULUS),
    COMPARISON(TokenType.COMPARISON), NOT_EQUALS(TokenType.NOT_EQUALS), LESS_THAN(TokenType.LESS_THAN), LESS_OR_EQUALS(TokenType.LESS_OR_EQUALS),
    GREATER_THAN(TokenType.GREATER_THAN), GREATER_OR_EQUALS(TokenType.GREATER_OR_EQUALS),
    AND(TokenType.AND), OR(TokenType.OR);

    private final TokenType token;
    BinaryOperators(TokenType token) { this.token = token; }
}

// Constants
sealed interface Constant permits CNone, CBool, CString, CInt, CDouble {}

record CNone() implements Constant {}
record CBool(boolean value) implements Constant {}
record CString(String value) implements Constant {}
record CInt(long value) implements Constant {}
record CDouble(double value) implements Constant {}

// Expressions
sealed interface Expr permits Econstant, Eident, Ebinaryoperators, Eunaryoperators, Ecall, Elist, Eget {}

record Econstant(Constant value) implements Expr {}
record Eident(Ident ident) implements Expr {}
record Ebinaryoperators(BinaryOperators op, Expr left, Expr right) implements Expr {}
record Eunaryoperators(UnaryOperators op, Expr expr) implements Expr {}
record Ecall(Ident func, List<Expr> args) implements Expr {}
record Elist(List<Expr> elements) implements Expr {}
record Eget(Expr list, Expr index) implements Expr {}

// Statements
sealed interface Stmt permits Sif, Sreturn, Sassign, Sprint, Sblock, Sfor, Seval, Sset {}

record Sif(Expr condition, Stmt thenBranch, Stmt elseBranch) implements Stmt {}
record Sreturn(Expr expr) implements Stmt {}
record Sassign(Ident var, Expr expr) implements Stmt {}
record Sprint(Expr expr) implements Stmt {}
record Sblock(List<Stmt> stmts) implements Stmt {}
record Sfor(Ident var, Expr iterable, Stmt body) implements Stmt {}
record Seval(Expr expr) implements Stmt {}
record Sset(Expr list, Expr index, Expr value) implements Stmt {}

// Function definition
record Def(Ident name, List<Ident> params, Stmt body) {}

// Represents a Mini-Python file (list of function definitions and a main block)
record File(List<Def> functions, Stmt mainBlock) {}