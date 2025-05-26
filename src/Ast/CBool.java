package Ast;

public record CBool(boolean value) implements Literals {
    @Override
        public <R> R accept(AstVisitor<R> visitor) { return visitor.visitCBool(this);

        }
    }

