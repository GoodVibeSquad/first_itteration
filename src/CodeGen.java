import Ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeGen implements AstVisitor<Void> {

    private String sourceCode;

    private int scopeSize = 0;
    private StringBuilder output = new StringBuilder();


    //Constructor
    public CodeGen(){
        try {
            this.sourceCode = Files.readString(Paths.get("src/CodeFiles/myFile.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Funktion som starter hele generation fra den første ast node
    public String generate(Statement ASTRoot){
        ASTRoot.accept(this);
        return output.toString();
    }

    //Hjælpefunktion til indentation
    private String indent() {
        return "\t".repeat(scopeSize);
    }


    @Override
    public Void visitCNone(CNone c) {
        return null;
    }

    @Override
    public Void visitCBool(CBool c) {
        return null;
    }

    @Override
    public Void visitCString(CString c) {
        return null;
    }

    @Override
    public Void visitCInt(CInt c) {
        output.append(c.value());
        return null;
    }

    @Override
    public Void visitCDouble(CDouble c) {
        return null;
    }

    @Override
    public Void visitCEuler(CEuler c) {
        return null;
    }

    @Override
    public Void visitCPi(CPi c) {
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

    @Override
    public Void visitSif(Sif s) {
        output.append(indent()).append("if ");
        s.condition().accept(this);
        output.append(":\n");

        scopeSize++;
        s.thenBranch().accept(this);
        scopeSize--;

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
        for (Statement statement : slist.elements() ){
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


//    public void indent(Object node){
//
//
//        for (String line : lines) {
//
//
//            String stripped = line.trim();
//
//            //Indentation
//            if (stripped.contains("{")){
//                scopeSize++;
//            }
//
//            if (stripped.contains("}")) {
//                scopeSize--;
//            }
//
//
//            //If statement
//            if(stripped.contains("if")){
//                int conditionStart = stripped.indexOf("(");
//                int conditionEnd = stripped.indexOf(")");
//                String condition = stripped.substring(conditionStart + 1, conditionEnd);
//
//                output.append("\t".repeat(scopeSize)).append("if ").append(condition).append(":\n");
//
//            }
//
//            //Semicolon efter et statement
//            if(stripped.endsWith(";")){
//                stripped = stripped.substring(0, stripped.length() - 1);
//                output.append("\t".repeat(scopeSize)).append(stripped).append("\n");
//            }
//
//        }
//
//        System.out.println(output);
//    }

}
