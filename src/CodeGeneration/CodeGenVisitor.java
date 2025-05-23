package CodeGeneration;

import Ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CodeGenVisitor implements AstVisitor<Void> {
    private int scopeSize = 0;
    private StringBuilder output = new StringBuilder();

    private Stack<Set<String>> scopeStack = new Stack<>();

    //Constructor
    public CodeGenVisitor() {
    }


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

        String neuralNetworkClasses = readBaseCode();
        output.append(neuralNetworkClasses+ "\n# BASECODE DONE \n\n");


    }

    public String readBaseCode(){
        String filePath = "NativeNeuralNetwork.py";
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }


    //Hjælpefunktion til indentation
    private String indent() {
        return "\t".repeat(scopeSize);
    }


    private boolean isEmptyElseBranch(Statement elseBranch) {
        if (elseBranch instanceof SExpression sExpr) {
            if (sExpr.expr() instanceof EConstant eConst) {
                if (eConst.value() instanceof CNone) {
                    return true;
                }
            }
        }
        return false;
    }

    //ScopeSize tilføjelser

    private void enterScope() {
        scopeStack.push(new HashSet<>());
        scopeSize++;
    }

    private void exitScope() {
        Set<String> currentScope = scopeStack.peek();
        for (String var : currentScope) {
            output.append(indent()).append("del ").append(var).append("\n");
            //output.append("del ").append(var).append("\n");
        }

        scopeStack.pop();
        scopeSize--;
    }

    private void addVarToStack(String varName) {

        //Checks for duplicates throught all recorded scopes and if there are, we ignore the newest duplicated variable
        for (Set<String> scope : scopeStack) {
            if (scope.contains(varName)) {
                System.out.println("Duplicate " + varName + " detected in the " + scopeSize + "'st scope");
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

        // the expr of c contains an actual boolean variable which prints to a true or false
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
    public Void visitEconstant(EConstant e) {
        e.value().accept(this);
        return null;
    }

    @Override
    public Void visitEidentifier(EIdentifier e) {
        output.append(e.name().getId());
        return null;
    }

    @Override
    public Void visitEbinaryoperators(EBinaryoperators e) {
        e.left().accept(this);
        output.append(" ").append(e.op().toSymbol()).append(" ");
        e.right().accept(this);
        return null;

    }

    @Override
    public Void visitEunaryoperators(EUnaryoperators e) {
        //output.append(e.op().toSymbol()); ! istedet for not????
        //e.expr().accept(this); ! istedet for not????

        output.append("not ");
        e.expr().accept(this);

        return null;
    }

    @Override
    public Void visitEcall(EFuncCall e) {

        output.append("def ").append(e.func().getId()).append("(");
        e.args().accept(this);
        output.append(")\n");
        return null;
    }

    @Override
    public Void visitElist(EList e) {
        for (int i = 0; i < e.elements().size(); i++) {
            e.elements().get(i).accept(this);
            if (i < e.elements().size() - 1) {
                output.append(", ");
            }
        }
        return null;
    }


    @Override
    public Void visitEternary(ETernary e) {
        output.append("(");
        e.thenBranch().accept(this);
        output.append(" if ");
        e.condition().accept(this);
        output.append(" else ");
        e.elseBranch().accept(this);
        output.append(")");
        return null;
    }


    @Override
    public Void visitESum(ESum e) {

        output.append("sum(");
        e.body().accept(this);
        output.append(" for ").append(e.index().getId());
        output.append(" in range( ");
        e.topExpression().accept(this);
        output.append(", ");
        e.bottomExpression().accept(this);
        output.append(" + 1 ))"); //Skal vi litterally plus med 1?
        return null;
    }

    @Override
    public Void visitEMax(EMax e) {
        output.append("max(");
        e.args().accept(this);
        output.append(")");
        return null;
    }

    @Override
    public Void visitESqrt(ESqrt e) {
        output.append("math.sqrt(");
        e.expression().accept(this);
        output.append(")");
        return null;
    }

    //math.sqrt(x)

    @Override
    public Void visitETypeconversion(ETypeconversion e) {
        output.append(e.targetType().getValue()).append("(");
        e.expression().accept(this);
        output.append(")\n");

        //targetType(x)
        return null;
    }

    @Override
    public Void visitENewFunc(ENewFunc e) {

        String type = e.type().getTypeName();

        output.append(type).append("(");
        e.e().accept(this);
        output.append(")");

        return null;
    }

    @Override
    public Void visitEContainsExpression(EContainsExpression e) {
        e.expression().accept(this);
        return null;
    }

    @Override
    public Void visitEMethodCall(EMethodCall e) {
        output.append(e.receiver().getId()).append(".").append(e.method().getId()).append("(");
        e.args().accept(this);
        output.append(")");

        return null;

    }


    @Override
    public Void visitSif(SIf s) {
        output.append("if ");
        s.condition().accept(this);
        output.append(":\n");

        enterScope();
        s.thenBranch().accept(this);

        System.out.println(scopeStack);

        exitScope();
        System.out.println(scopeStack);

        Statement elseBranch = s.elseBranch();
        while (!isEmptyElseBranch(elseBranch)) {

            // Keeps adding elif, because the AST contains Sif's on an else branch (which also have else branch)
            // This is a problem because we need the else branch to be a continuation of the original
            // scope. Instead of being an else of the (else if) is statement is a else to that
            // specific if statement.
            if (elseBranch instanceof SIf nestedIf) {
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
    public Void visitSassign(SAssign s) {
        output.append(s.name().getId()).append(" ").
                append(s.assignmentOperator().toSymbol()).append(" ");

        s.expr().accept(this);

        output.append("\n");

        return null;
    }


    @Override
    public Void visitSprint(SPrint s) {
        output.append("print(");
        s.expr().accept(this);
        output.append(")\n");
        return null;
    }

    @Override
    public Void visitSblock(SBlock s) {
        s.stmts().accept(this);
        return null;
    }

    @Override
    public Void visitSfor(SFor s) {

        s.init().accept(this);


        output.append(indent()).append("while ");
        s.condition().accept(this);
        output.append(":\n");

        enterScope(); //enterscope identer
        s.body().accept(this);

        output.append(indent());
        s.update().accept(this);
        output.append("\n");

        String initId = null;
        if (s.init() instanceof SDeclaration decl) {
            initId = decl.var().getId();  // Får navnet først
        }
        exitScope();

        output.append(indent()).append("del ").append(initId).append("\n");

        return null;
    }

    @Override
    public Void visitSExpression(SExpression s) {


        // Handles variable declaration with initialized expr of 0
        if (s.expr() instanceof EIdentifier) {
            output.append("=0\n");
        } else{
            s.expr().accept(this);
            output.append("\n");
        }

        return null;
    }

    @Override
    public Void visitSWhile(SWhile s) {
        //while betingelse:
        //    # kodeblok

        output.append("while ");
        s.expression().accept(this);
        output.append(":\n");

        enterScope();
        s.body().accept(this);
        exitScope();

        return null;
    }

    @Override
    public Void visitSBreak(SBreak s) {
        output.append("break");
        return null;
    }

    @Override
    public Void visitSContinue(SContinue s) {
        output.append("continue");
        return null;
    }

    @Override
    public Void visitSlist(SList slist) {
        for (Statement statement : slist.elements()) {
            output.append(indent());
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visitSInDeCrement(SInDeCrement s) {
        String varName = s.identifier().getId();

        if (s.inDeCrement() == InDeCrement.INCREMENT) {
            output.append(varName).append(" += 1\n");
        } else if (s.inDeCrement() == InDeCrement.DECREMENT) {
            output.append(varName).append(" -= 1\n");
        }

        return null;
    }



    // VI BRUGER DEN IKKE (DEF)
    @Override
    public Void visitDef(Def d) {
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


    @Override
    public Void visitSDeclaration(SDeclaration s) {
        output.append(s.var().getId()).append(" ")
                .append(s.assignmentOperator().toSymbol()).append(" ");


        if (s.expr() != null) {
            s.expr().accept(this);
        } else {
            output.append("0");  //if no initialised expr.
        }

        addVarToStack(s.var().getId()); //track that the variable has been declared
        output.append("\n");
        return null;
    }

    @Override
    public Void visitSReturn(SReturn s) {

        output.append("return ");
        s.expr().accept(this);
        output.append("\n");

        return null;
    }

}

