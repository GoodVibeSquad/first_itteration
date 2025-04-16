package Ast;

import Tokens.TokenType;

import java.util.HashMap;
import java.util.Map;

public enum AssignmentOperator {
    ASSIGN(TokenType.ASSIGN),
    ADD_ASSIGN(TokenType.ADD_ASSIGN),
    SUB_ASSIGN(TokenType.SUB_ASSIGN),
    DIV_ASSIGN(TokenType.DIV_ASSIGN),
    MUL_ASSIGN(TokenType.MUL_ASSIGN),
    MOD_ASSIGN(TokenType.MOD_ASSIGN);

    private final TokenType token;

    AssignmentOperator(TokenType token) {
        this.token = token;
    }

    private static final Map<TokenType, AssignmentOperator> tokenToOperatorMap = new HashMap<>();

    static {
        for (AssignmentOperator op : values()) {
            tokenToOperatorMap.put(op.token, op);
        }
    }

    public static AssignmentOperator fromTokenType(TokenType type) {
        return tokenToOperatorMap.get(type);
    }
}
