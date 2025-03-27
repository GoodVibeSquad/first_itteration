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
    public String visitEcall(Ecall e) {
        return "";
    }

    @Override
    public String visitElist(Elist e) {
        return "";
    }

    @Override
    public String visitEget(Eget e) {
        return "";
    }

    @Override
    public String visitEternary(Eternary e) {
        return "";
    }

    // Statements
    @Override
    public String visitSif(Sif s) {
        return "";
    }

    @Override
    public String visitSreturn(Sreturn s) {
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
    public String visitSeval(Seval s) {
        return "";
    }

    @Override
    public String visitSset(Sset s) {
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
