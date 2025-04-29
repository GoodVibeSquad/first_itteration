package CodeGeneration;

import Ast.*;

public class CodeGenVisitor implements AstVisitor<Void> {

    private int scopeSize = 0;
    private StringBuilder output = new StringBuilder();


    //Constructor
    public CodeGenVisitor() {}


    //Funktion som starter hele generation fra den første ast node
    public String generate(Statement ASTRoot) {
        applyBaseCode();

        ASTRoot.accept(this);
        return output.toString();
    }

    //  Base code, libraries, classes needed for our python code to run
    private void applyBaseCode() {
        output.append("import math\n");

        // Run with python pythonKode.py
        // Test print functions since they don't work yet:
//        print(piNum + eulerNum)
//        print(myBool)
//        print(y)
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

        // the value of c contains an actual boolean variable which prints to a true or false
        // with un-capitalized first letter. Python requires capitalization (True, False)
        // Therefore we capitalize the first letter before appending it.
        String javaBool = String.valueOf(c.value());
        String pythonBool = javaBool.substring(0, 1).toUpperCase() + javaBool.substring(1);
        output.append(pythonBool);
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
        output.append(indent() + e.name().getId());
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
        for (Expression expression : e.elements()) {
            expression.accept(this);
        }

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
    public Void visitSDeclaration(SDeclaration s) {
        output.append(indent()).append(s.var().getId()).append(" ").
                append(s.assignmentOperator().toSymbol()).append(" ");

        if (s.expr() != null) {
            s.expr().accept(this);
        }
        else {
            output.append("0");
        }

        output.append("\n");

        return null;
    }


    @Override
    public Void visitSprint(Sprint s) {
        output.append("print(");
        s.expr().accept(this);
        output.append(")\n");
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
        s.value().accept(this);

        // Handles variable declaration with initialized value of 0
        if(s.value() instanceof Eidentifier){
            output.append("=0\n");
        }

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

}