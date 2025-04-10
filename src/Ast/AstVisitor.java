package Ast;


 interface AstVisitor<R> {
    // Constants
    R visitCNone(CNone c);
    R visitCBool(CBool c);
    R visitCString(CString c);
    R visitCInt(CInt c);
    R visitCDouble(CDouble c);
    R visitCEuler(CEuler c);
    R visitCPi(CPi c);

    // Expressions
    R visitEconstant(Econstant e);
    R visitEbinaryoperators(Ebinaryoperators e);
    R visitEunaryoperators(Eunaryoperators e);
    R visitEcall(EFuncCall e);
    R visitElist(Elist e);
    R visitEternary(Eternary e);
    R visitESum(ESum e);
    R visitEMax(EMax e);
    R visitESqrt(ESqrt e);
    R visitETypeconversion(ETypeconversion e);
    R visitENewFunc(ENewFunc e);
    R visitEMethodCall(EMethodCall e);

    // Expression Tail
    R visitETailNone(ETailNone e);
    R visitEtailBPEtail(EtailBPEtail e);

    // PRimary
    R visitPConst(PConst e);
    R visitPIdentifier(PIdentifier e);
    R visitPParenthesis(PParenthesis e);

    // Statements
    R visitSif(Sif s);
    R visitSassign(Sassign s);
    R visitSprint(Sprint s);
    R visitSblock(Sblock s);
    R visitSfor(Sfor s);
    R visitSExpression(SExpression s);
    R visitSWhile(SWhile s);
    R visitSBreak(SBreak s);
    R visitSContinue(SContinue s);
    R visitSlist(Slist slist);


    //Other
    R visitDef(Def d);
    R visitFile(File f);
 }
