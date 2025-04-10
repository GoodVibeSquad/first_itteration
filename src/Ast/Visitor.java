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
    public String visitEMethodCall(EMethodCall e) {
        return "";
    }

    // Etail
    @Override
    public String visitETailNone(ETailNone e){return "";}

    @Override
    public String visitEtailBPEtail(EtailBPEtail e){return "";}

    // primaries
    @Override
    public String visitPConst(PConst e){return "";}

    @Override
    public String visitPIdentifier(PIdentifier pIdentifier){return "";}

    @Override
    public String visitPParenthesis(PParenthesis pParenthesis){return "";}

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

    @Override
    public String visitSlist(Slist slist) {
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
