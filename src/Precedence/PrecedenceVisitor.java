package Precedence;

import Ast.*;
import TypeChecking.SymbolTable;

public class PrecedenceVisitor implements AstVisitor<Expression> {



    @Override
    public Expression visitEbinaryoperators(Ebinaryoperators ebin) {
        // Recursively visit children first
        Expression left = ebin.left().accept(this);
        Expression right = ebin.right().accept(this);
        BinaryOperators op = ebin.op();

        int currentPrec = op.getPrecedence();
        BinaryOperators.Associativity assoc = op.getAssociativity();

        // Check left child — rotate if lower precedence or equal and right-associative
        if (left instanceof Ebinaryoperators leftBin) {
            int leftPrec = leftBin.op().getPrecedence();
            BinaryOperators.Associativity leftAssoc = leftBin.op().getAssociativity();

            if (leftPrec < currentPrec ||
                    (leftPrec == currentPrec && assoc == BinaryOperators.Associativity.RIGHT)) {
                // Left rotate: ( (a leftOp b) op c ) → ( a leftOp (b op c) )
                Expression newRight = new Ebinaryoperators(op, leftBin.right(), right);
                return new Ebinaryoperators(leftBin.op(), leftBin.left(), newRight).accept(this);
            }
        }

        // Check right child — rotate if higher precedence or equal and left-associative
        if (right instanceof Ebinaryoperators rightBin) {
            int rightPrec = rightBin.op().getPrecedence();
            BinaryOperators.Associativity rightAssoc = rightBin.op().getAssociativity();

            if (rightPrec > currentPrec ||
                    (rightPrec == currentPrec && assoc == BinaryOperators.Associativity.LEFT)) {
                // Right rotate: ( a op (b rightOp c) ) → ( (a op b) rightOp c )
                Expression newLeft = new Ebinaryoperators(op, left, rightBin.left());
                return new Ebinaryoperators(rightBin.op(), newLeft, rightBin.right()).accept(this);
            }
        }

        return new Ebinaryoperators(op, left, right);
    }

    // === other visitor methods ===
    @Override public Expression visitEconstant(Econstant e) { return e; }
    @Override public Expression visitEidentifier(Eidentifier e) { return e; }
    @Override public Expression visitEunaryoperators(Eunaryoperators e) { return e; }
    @Override public Expression visitEcall(EFuncCall e) { return e; }
    @Override public Expression visitElist(Elist e) { return e; }
    @Override public Expression visitEternary(Eternary e) { return e; }
    @Override public Expression visitESum(ESum e) { return e; }
    @Override public Expression visitEMax(EMax e) { return e; }
    @Override public Expression visitESqrt(ESqrt e) { return e; }
    @Override public Expression visitETypeconversion(ETypeconversion e) { return e; }
    @Override public Expression visitENewFunc(ENewFunc e) { return e; }
    @Override public Expression visitEContainsExpression(EContainsExpression e) { return e; }
    @Override public Expression visitEMethodCall(EMethodCall e) { return e; }

    // === constants ===
    @Override public Expression visitCNone(CNone c) { return null; }
    @Override public Expression visitCBool(CBool c) { return null; }
    @Override public Expression visitCString(CString c) { return null; }
    @Override public Expression visitCInt(CInt c) { return null; }
    @Override public Expression visitCDouble(CDouble c) { return null; }
    @Override public Expression visitCEuler(CEuler c) { return null; }
    @Override public Expression visitCPi(CPi c) { return null; }

    // === statements — implement or skip as needed ===
    @Override public Expression visitSif(Sif s) { return null; }
    @Override public Expression visitSassign(Sassign s) { return null; }
    @Override public Expression visitSDeclaration(SDeclaration s) { return null; }
    @Override public Expression visitSprint(Sprint s) { return null; }
    @Override public Expression visitSblock(Sblock s) { return null; }
    @Override public Expression visitSfor(Sfor s) { return null; }
    @Override public Expression visitSExpression(SExpression s) { return null; }
    @Override public Expression visitSWhile(SWhile s) { return null; }
    @Override public Expression visitSBreak(SBreak s) { return null; }
    @Override public Expression visitSContinue(SContinue s) { return null; }
    @Override public Expression visitSlist(Slist s) { return null; }
    @Override public Expression visitSInDeCrement(SInDeCrement s) { return null; }

    @Override public Expression visitDef(Def d) { return null; }
}
