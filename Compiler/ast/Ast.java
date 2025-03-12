/*

HOLA SOZ
TODO:
- Lav alt
- Lav alle klasser(enums og records også) til egne filer
- Tilføj alle binære operatorer
- Alt der giver en værdi er en expression (F.eks. PLUS, MINUS, BINÆROPERATIONER)
- Her er et eksempel på en af funktioner
record Econstant(Literals  value) implements Expression {}

F.eks. hvis du vil lave en ny en til ternary operator

record ETernary(Expression e, Expression e, Expression e) implements Expression {}

-Lav noget en Visitor klasse

-Lav en accept funktion i alle records

-Eksempel af visitor interface

package ast;

public interface AstVisitor<R> {
    // Constants
    R visitCNone(CNone c);

}

Eksempel af implementation af en AstVisitor

record CNone() implements Constant {
    public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCNone(this); }
}




*/
package Compiler.ast;
import java.util.List;
import Compiler.Lexer.*;

public class Ast {
}



// Each part of the actual source code split into columns and lines
class Location {
    int line, column;
    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }
}


class Identifier {
    Location loc;
    String id;

    public Identifier(Location loc, String id) {
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
sealed interface Literals  permits CNone, CBool, CString, CInt, CDouble {}

record CNone() implements Literals  {}
record CBool(boolean value) implements Literals  {}
record CString(String value) implements Literals  {}
record CInt(long value) implements Literals  {}
record CDouble(double value) implements Literals  {}

// Expressions
sealed interface Expression permits Econstant, Eident, Ebinaryoperators, Eunaryoperators, Ecall, Elist, Eget {}

record Econstant(Literals  value) implements Expression {}
record Eident(Identifier ident) implements Expression {}
record Ebinaryoperators(BinaryOperators op, Expression left, Expression right) implements Expression {}
record Eunaryoperators(UnaryOperators op, Expression expr) implements Expression {}
record Ecall(Identifier func, List<Expression> args) implements Expression {}
record Elist(List<Expression> elements) implements Expression {}
record Eget(Expression list, Expression index) implements Expression {}

// Statements
sealed interface Statement permits Sif, Sreturn, Sassign, Sprint, Sblock, Sfor, Seval, Sset {}

record Sif(Expression condition, Statement thenBranch, Statement elseBranch) implements Statement {}
record Sreturn(Expression expr) implements Statement {}
record Sassign(Identifier var, Expression expr) implements Statement {}
record Sprint(Expression expr) implements Statement {}
record Sblock(List<Statement> stmts) implements Statement {}
record Sfor(Identifier var, Expression iterable, Statement body) implements Statement {}
record Seval(Expression expr) implements Statement {}
record Sset(Expression list, Expression index, Expression value) implements Statement {}

// Function definition
record Def(Identifier name, List<Identifier> params, Statement body) {}

record File(List<Def> functions, Statement mainBlock) {}