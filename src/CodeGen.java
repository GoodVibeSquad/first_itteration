import Ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeGen implements AstVisitor<Void> {

    private int scopeSize = 0;
    private StringBuilder output = new StringBuilder();


    //Constructor
    public CodeGen() {}


    //Funktion som starter hele generation fra den første ast node
    public String generate(Statement ASTRoot) {
        ASTRoot.accept(this);
        return output.toString();
    }


    //Hjælpefunktion til indentation
    private String indent() {
        return "\t".repeat(scopeSize);
    }


    private boolean isEmptyElseBranch(Statement elseBranch) {
        if (elseBranch instanceof SExpression sExpr) {
            if (sExpr.value() instanceof Econstant eConst) {
                if (eConst.value() instanceof CNone) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Void visitCNone(CNone c) {
        return null;
    }

    @Override
    public Void visitCBool(CBool c) {
        output.append(c.value());
        return null;
    }

    // NOTE: Maybe look into this such that it appends the stirng correctly
    @Override
    public Void visitCString(CString c) {
        output.append('"' + c.value() + '"');
        return null;
    }

    @Override
    public Void visitCInt(CInt c) {
        output.append(c.value());
        return null;
    }

    @Override
    public Void visitCDouble(CDouble c) {
        output.append(c.value());
        return null;
    }

    @Override
    public Void visitCEuler(CEuler c) {
        output.append("math.e");
        return null;
    }

    @Override
    public Void visitCPi(CPi c) {
        output.append("math.pi");
        return null;
    }

    @Override
    public Void visitEconstant(Econstant e) {
        e.value().accept(this);
        return null;
    }

    @Override
    public Void visitEidentifier(Eidentifier e) {
        output.append(e.name().getId());
        return null;
    }

    @Override
    public Void visitEbinaryoperators(Ebinaryoperators e) {
        e.left().accept(this);
        output.append(" ").append(e.op().toSymbol()).append(" ");
        e.right().accept(this);
        return null;

    }

    @Override
    public Void visitEunaryoperators(Eunaryoperators e) {
        return null;
    }

    @Override
    public Void visitEcall(EFuncCall e) {
        return null;
    }

    @Override
    public Void visitElist(Elist e) {
        return null;
    }

    @Override
    public Void visitEternary(Eternary e) {
        return null;
    }

    @Override
    public Void visitESum(ESum e) {
        return null;
    }

    @Override
    public Void visitEMax(EMax e) {
        return null;
    }

    @Override
    public Void visitESqrt(ESqrt e) {
        return null;
    }

    @Override
    public Void visitETypeconversion(ETypeconversion e) {
        return null;
    }

    @Override
    public Void visitENewFunc(ENewFunc e) {
        return null;
    }

    @Override
    public Void visitEContainsExpression(EContainsExpression e) {
        e.expression().accept(this);
        return null;
    }

    @Override
    public Void visitEMethodCall(EMethodCall e) {
        return null;
    }


    /*
    * public record Sif(Expression condition, Statement thenBranch, Statement elseBranch) implements Statement {
    //accept metode (visitor)
    @Override
    * */
    @Override
    public Void visitSif(Sif s) {
        output.append(indent()).append("if ");
        s.condition().accept(this);
        output.append(":\n");

        scopeSize++;
        s.thenBranch().accept(this);
        scopeSize--;

        Statement elseBranch = s.elseBranch();
        while (!isEmptyElseBranch(elseBranch)) {

            // Keeps adding elif, because the AST contains Sif's on an else branch (which also have else branch)
            // This is a problem because we need the else branch to be a continuation of the original
            // scope. Instead of being an else of the (else if) is statement is a else to that
            // specific if statement.
            if (elseBranch instanceof Sif nestedIf) {
                output.append(indent()).append("elif ");
                nestedIf.condition().accept(this);
                output.append(":\n");

                scopeSize++;
                nestedIf.thenBranch().accept(this);
                scopeSize--;

                // going deeper with elif in the while loop
                elseBranch = nestedIf.elseBranch();

            } else {
                output.append(indent()).append("else:\n");
                scopeSize++;
                elseBranch.accept(this);
                scopeSize--;
                break;
            }
        }

        return null;
    }

    @Override
    public Void visitSassign(Sassign s) {
        output.append(indent()).append(s.var().getId()).append(" ").
                append(s.assignmentOperator().toSymbol()).append(" ");

        s.expr().accept(this);

        output.append("\n");

        return null;
    }


    @Override
    public Void visitSprint(Sprint s) {
        return null;
    }

    @Override
    public Void visitSblock(Sblock s) {
        return null;
    }

    @Override
    public Void visitSfor(Sfor s) {
        return null;
    }

    @Override
    public Void visitSExpression(SExpression s) {
        return null;
    }

    @Override
    public Void visitSWhile(SWhile s) {
        return null;
    }

    @Override
    public Void visitSBreak(SBreak s) {
        return null;
    }

    @Override
    public Void visitSContinue(SContinue s) {
        return null;
    }

    @Override
    public Void visitSlist(Slist slist) {
        for (Statement statement : slist.elements()) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visitSInDeCrement(SInDeCrement sInDeCrement) {
        return null;
    }

    @Override
    public Void visitDef(Def d) {
        return null;
    }

    @Override
    public Void visitFile(File f) {
        return null;
    }
}