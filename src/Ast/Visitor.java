package Ast;

class Visitor implements AstVisitor<String> {

    // Constants
    @Override
    public String visitCNone(CNone c) {
        return "";
    }

    @Override
    public String visitCBool(CBool c) {
        return "";
    }

    @Override
    public String visitCString(CString c) {
        return "";
    }

    @Override
    public String visitCInt(CInt c) {
        return "";
    }

    @Override
    public String visitCDouble(CDouble c) {
        return "";
    }

    @Override
    public String visitCEuler(CEuler c) {
        return "";
    }

    @Override
    public String visitCPi(CPi c) {
        return "";
    }

    // Expressions
    @Override
    public String visitEconstant(Econstant e) {
        return "";
    }

    @Override
    public String visitEidentifier(Eidentifier e) {
        return "";
    }

    @Override
    public String visitEbinaryoperators(Ebinaryoperators e) {
        return "";
    }

    @Override
    public String visitEunaryoperators(Eunaryoperators e) {
        return "";
    }

    @Override
    public String visitEcall(EFuncCall e) {
        return "";
    }

    @Override
    public String visitElist(Elist e) {
        return "";
    }

    @Override
    public String visitEternary(Eternary e) {
        return "";
    }

    @Override
    public String visitESum(ESum e) {
        return "";
    }

    @Override
    public String visitEMax(EMax e) {
        return "";
    }

    @Override
    public String visitESqrt(ESqrt e) {
        return "";
    }

    @Override
    public String visitETypeconversion(ETypeconversion e) {
        return "";
    }

    @Override
    public String visitENewFunc(ENewFunc e) {
        return "";
    }

    @Override
    public String visitEContainsExpression(EContainsExpression e) {
        return "";
    }

    @Override
    public String visitEMethodCall(EMethodCall e) {
        return "";
    }

    // Statements
    @Override
    public String visitSif(Sif s) {
        return "";
    }

    @Override
    public String visitSassign(Sassign s) {
        return "";
    }

    @Override
    public String visitSprint(Sprint s) {
        return "";
    }

    @Override
    public String visitSblock(Sblock s) {
        return "";
    }

    @Override
    public String visitSfor(Sfor s) {
        return "";
    }

    @Override
    public String visitSExpression(SExpression s) {
        return "";
    }

    @Override
    public String visitSWhile(SWhile s) {
        return "";
    }

    @Override
    public String visitSBreak(SBreak s) {
        return "";
    }

    @Override
    public String visitSContinue(SContinue s) {
        return "";
    }

    // Other
    @Override
    public String visitDef(Def d) {
        return "";
    }

    @Override
    public String visitFile(File f) {
        return "";
    }
}
