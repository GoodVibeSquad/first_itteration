package Compiler.ast;


 interface AstVisitor<R> {
    // Constants
    R visitCNone(CNone c);
    R visitCBool(CBool c);
    R visitCString(CString c);
    R visitCInt(CInt c);
    R visitCDouble(CDouble c);

    // Expressions
    R visitEconstant(Econstant e);
    R visitEidentifier(Eidentifier e);
    R visitEbinaryoperators(Ebinaryoperators e);
    R visitEunaryoperators(Eunaryoperators e);
    R visitEcall(Ecall e);
    R visitElist(Elist e);
    R visitEget(Eget e);
    R visitEternary(Eternary e);

    // Statements
    R visitSif(Sif s);
    R visitSreturn(Sreturn s);
    R visitSassign(Sassign s);
    R visitSprint(Sprint s);
    R visitSblock(Sblock s);
    R visitSfor(Sfor s);
    R visitSeval(Seval s);
    R visitSset(Sset s);


    //Other
    R visitDef(Def d);
    R visitFile(File f);
}
