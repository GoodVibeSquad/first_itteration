import java.util.*;
import Compiler.ast.*;

public class BottomUpParser {

    // --- Grammar setup ---
    public static class Production {
        String lhs;
        List<String> rhs;

        Production(String lhs, List<String> rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public String toString() {
            return lhs + " -> " + String.join(" ", rhs);
        }
    }

    static class Grammar {
        List<Production> productions = new ArrayList<>();
        String startSymbol;

        void add(String lhs, String... rhs) {
            productions.add(new Production(lhs, Arrays.asList(rhs)));
        }


    }

    // --- Token and lexer hookup ---
    static class Token {
        String type;
        String value;

        Token(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String toString() {
            return type;
        }
    }

    static class TokenStream {
        List<Token> tokens;
        int pos = 0;

        TokenStream(List<Token> tokens) {
            this.tokens = tokens;
        }

        Token peek() {
            if (pos < tokens.size()) return tokens.get(pos);
            return new Token("EOF", "");
        }

        Token advance() {
            return tokens.get(pos++);
        }
    }

    // --- Action enum ---
    enum ActionType { SHIFT, REDUCE, ACCEPT, ERROR }

    static class Action {
        ActionType type;
        int target;
        Production production;

        static Action shift(int state) {
            Action a = new Action();
            a.type = ActionType.SHIFT;
            a.target = state;
            return a;
        }

        static Action reduce(Production prod) {
            Action a = new Action();
            a.type = ActionType.REDUCE;
            a.production = prod;
            return a;
        }

        static Action accept() {
            Action a = new Action();
            a.type = ActionType.ACCEPT;
            return a;
        }

        static Action error() {
            Action a = new Action();
            a.type = ActionType.ERROR;
            return a;
        }

        public String toString() {
            return switch (type) {
                case SHIFT -> "SHIFT " + target;
                case REDUCE -> "REDUCE " + production;
                case ACCEPT -> "ACCEPT";
                default -> "ERROR";
            };
        }
    }

    // --- Mock parse table (example only) ---
    static Map<Integer, Map<String, Action>> actionTable = new HashMap<>();
    static Map<Integer, Map<String, Integer>> gotoTable = new HashMap<>();

    // --- Main parser ---
    public static Expression parse(List<Token> tokens, Grammar grammar) {
        Stack<Integer> stateStack = new Stack<>();
        Stack<Object> symbolStack = new Stack<>();
        TokenStream ts = new TokenStream(tokens);

        stateStack.push(0);

        while (true) {
            int currentState = stateStack.peek();
            String tokenType = ts.peek().type;
            Action action = actionTable.getOrDefault(currentState, new HashMap<>()).getOrDefault(tokenType, Action.error());

            if (action.type == ActionType.SHIFT) {
                stateStack.push(action.target);
                symbolStack.push(ts.advance());
            } else if (action.type == ActionType.REDUCE) {
                List<Object> children = new ArrayList<>();
                for (int i = 0; i < action.production.rhs.size(); i++) {
                    stateStack.pop();
                    children.add(0, symbolStack.pop());
                }
                Object node = buildAST(action.production, children);
                int nextState = gotoTable.get(stateStack.peek()).get(action.production.lhs);
                stateStack.push(nextState);
                symbolStack.push(node);
                System.out.println("Reduced: " + action.production);
            } else if (action.type == ActionType.ACCEPT) {
                System.out.println("Parsing successful!");
                return (Expression) symbolStack.pop();
            } else {
                System.err.println("Syntax error at token: " + ts.peek());
                return null;
            }
        }
    }

    private static Object buildAST(Production prod, List<Object> children) {
        return switch (prod.lhs) {
            case "E" -> {
                if (prod.rhs.size() == 3 && "+".equals(prod.rhs.get(1))) {
                    Expression left = (Expression) children.get(0);
                    Expression right = (Expression) children.get(2);
                    yield new Ebinaryoperators(BinaryOperators.PLUS, left, right);
                } else {
                    yield children.get(0);
                }
            }
            case "T" -> {
                Token token = (Token) children.get(0);
                yield new Econstant(new CInt(Long.parseLong(token.value)));
            }
            default -> throw new RuntimeException("No AST rule for production: " + prod);
        };
    }

    // --- Example usage ---
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.startSymbol = "E";
        grammar.add("E", "E", "+", "T");
        grammar.add("E", "T");
        grammar.add("T", "int");

        actionTable.put(0, Map.of("int", Action.shift(2)));
        actionTable.put(1, Map.of("+", Action.shift(3), "EOF", Action.accept()));
        actionTable.put(2, Map.of("+", Action.reduce(grammar.productions.get(2)), "EOF", Action.reduce(grammar.productions.get(2))));
        actionTable.put(3, Map.of("int", Action.shift(4)));
        actionTable.put(4, Map.of("+", Action.reduce(grammar.productions.get(0)), "EOF", Action.reduce(grammar.productions.get(0))));

        gotoTable.put(0, Map.of("E", 1, "T", 2));
        gotoTable.put(3, Map.of("T", 4));

        List<Token> tokens = List.of(
                new Token("int", "1"),
                new Token("+", "+"),
                new Token("int", "2"),
                new Token("EOF", "")
        );

        Expression ast = parse(tokens, grammar);
        System.out.println("AST: " + ast);
    }
}
