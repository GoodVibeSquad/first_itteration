package Compiler.ast;

record CBool(boolean value) implements Literals {
    //accept metode (visitor)
    @Override
        public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCBool(this);

        }
    }

