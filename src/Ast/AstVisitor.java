package Ast;


 public interface AstVisitor<R> {
    // Constants
    R visitCNone(CNone c);
    R visitCBool(CBool c);
    R visitCString(CString c);
    R visitCInt(CInt c);
    R visitCDouble(CDouble c);
    R visitCEuler(CEuler c);
    R visitCPi(CPi c);

    // Expressions
    R visitEconstant(EConstant e);
    R visitEidentifier(EIdentifier e);
    R visitEbinaryoperators(EBinaryoperators e);
    R visitEunaryoperators(EUnaryoperators e);
    R visitEcall(EFuncCall e);
    R visitElist(EList e);
    R visitEternary(ETernary e);
    R visitESum(ESum e);
    R visitEMax(EMax e);
    R visitESqrt(ESqrt e);
    R visitETypeconversion(ETypeconversion e);
    R visitENewFunc(ENewFunc e);
    R visitEContainsExpression(EContainsExpression e);
    R visitEMethodCall(EMethodCall e);

    // Statements
    R visitSif(SIf s);
    R visitSassign(SAssign s);
    R visitSDeclaration(SDeclaration s);
    R visitSprint(SPrint s);
    R visitSblock(SBlock s);
    R visitSfor(SFor s);
    R visitSExpression(SExpression s);
    R visitSWhile(SWhile s);
    R visitSBreak(SBreak s);
    R visitSContinue(SContinue s);
    R visitSlist(SList slist);
    R visitSInDeCrement(SInDeCrement sInDeCrement);
    R visitSReturn(SReturn s);

    //function definition
    R visitSFunction(SFunction sFunction);
    R visitFunctionIdentifier(FunctionIdentifier functionIdentifier);

    //Other
    R visitDef(Def d);
 }
