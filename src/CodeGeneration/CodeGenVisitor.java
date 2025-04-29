package CodeGeneration;

import Ast.*;

import java.util.*;

public class CodeGenVisitor implements AstVisitor<Void> {
    private int scopeSize = 0;
    private StringBuilder output = new StringBuilder();

    private Deque<Set<String>> scopeStack = new ArrayDeque<>();

    //Constructor
    public CodeGenVisitor() {}


    //Funktion som starter hele generation fra den første ast node
    public String generate(Statement ASTRoot) {
        applyBaseCode();

        scopeStack.push(new HashSet<>());
        ASTRoot.accept(this);
        scopeStack.pop(); //tror ikke den her er nødvendig men den er her just in case

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

    //ScopeSize tilføjelser

    private void enterScope(){
        scopeStack.push(new HashSet<>());
        scopeSize++;
    }

    private void exitScope(){
        scopeStack.pop();
        scopeSize--;
    }

    private void addVarToStack (String varName){

        //Checks for duplicates throught all recorded scopes and if there are, we ignore the newest duplicated variable
        for (Set<String> scope : scopeStack) {
            if (scope.contains(varName)){
                System.out.println("Duplicate " + varName + " detected in " + scopeSize);
                return;
            }
        }

        //Peek is used to get the most recent recorded scope set, so we can add the string to the scope we're currently in
        scopeStack.peek().add(varName);
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
        for (int i = 0; i < e.elements().size(); i++) {
            e.elements().get(i).accept(this);
            if (i < e.elements().size() - 1) {
                output.append(", ");
            }
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


    @Override
    public Void visitSif(Sif s) {
        output.append("if ");
        s.condition().accept(this);
        output.append(":\n");

        enterScope();
        s.thenBranch().accept(this);
        exitScope();

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

                enterScope();
                nestedIf.thenBranch().accept(this);
                exitScope();

                // going deeper with elif in the while loop
                elseBranch = nestedIf.elseBranch();

            } else {
                output.append(indent()).append("else:\n");

                enterScope();
                elseBranch.accept(this);
                exitScope();
                break;
            }
        }

        return null;
    }

    @Override
    public Void visitSassign(Sassign s) {
        output.append(s.var().getId()).append(" ").
                append(s.assignmentOperator().toSymbol()).append(" ");

        addVarToStack(s.var().getId());
        System.out.println("scopeStack : " + scopeStack);

        s.expr().accept(this);

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
            output.append(indent());
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

    @Override
    public Void visitSFunction(SFunction sFunction) {
        output.append("def ");
        sFunction.functionIdentifier().accept(this);
        output.append(": \n");

        enterScope();
        sFunction.body().accept(this);
        exitScope();

        output.append("\n");
        return null;
    }

    @Override
    public Void visitFunctionIdentifier(FunctionIdentifier functionIdentifier) {
        output.append(functionIdentifier.name().getId());
        output.append("(");
        functionIdentifier.params().accept(this);
        output.append(")");
        return null;
    }

}