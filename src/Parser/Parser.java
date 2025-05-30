package Parser;

import Ast.SList;
import Parser.TableGenerator.TableGenerator;
import Tokens.Token;
import Tokens.TokenType;

import java.util.*;

public class Parser {

    public static SList parse(List<Token> input) {
        Stack<Integer> stateStack = new Stack<>();
        stateStack.push(0); // Start state
        Stack<Object> objectStack = new Stack<>();
        ASTBuilder astBuilder = new ASTBuilder();
        Queue<Token> tokenStream = new LinkedList<>(input);
        tokenStream.add(new Token(TokenType.EOF)); // End marker
        Token token = tokenStream.poll();
        String nextSymbol = token.getType().toString();


        while (true) {
            int currentState = stateStack.peek();
            String action = TableGenerator.actionTable
                    .getOrDefault(currentState, Map.of())
                    .getOrDefault(nextSymbol, null);

            if (action == null) {
                throw new RuntimeException("Syntax error at symbol: " + nextSymbol
                        + " in state: " + currentState +
                        "\nAvailable actions: " + TableGenerator.actionTable.getOrDefault(currentState, Map.of()) );
            }


            if (action.startsWith("S")) {
                int state = Integer.parseInt(action.substring(1));

                stateStack.push(state);
               System.out.println("Shifted " + stateStack.peek());
                objectStack.push(token);
                token = tokenStream.poll();
                nextSymbol = token.getType().toString();// move to next input symbol

            } else if (action.startsWith("R")) {
                List<Object> children = new ArrayList<>();
                String prodStr = action.substring(1); // format: "A -> β"
                String[] parts = prodStr.split("->");
                String lhs = parts[0].trim();
                String[] rhs = parts[1].trim().split("\\s+");
                List<String> rhs1 = List.of(parts[1].trim().split("\\s+"));
                Production prod = new Production(lhs,rhs1);
                int popCount = rhs[0].equals("") ? 0 : rhs.length;
                for (int i = 0; i < popCount; i++) {
                    stateStack.pop();
                    children.add(objectStack.pop());
                }


                 Object node = astBuilder.buildAst(prod,children.reversed());
                 objectStack.push(node);

                currentState = stateStack.peek();
                Integer nextState = TableGenerator.gotoTable
                        .getOrDefault(currentState, Map.of())
                        .get(lhs);

                if (nextState == null) {
                    throw new RuntimeException("Goto error for non-terminal: " + lhs);
                }

                stateStack.push(nextState);
                System.out.println("Reduced using: " + lhs + " -> " + String.join(" ", rhs));
            } else if (action.equals("ACC")) {
                System.out.println("Input successfully parsed!");
                break;
            } else {
                throw new RuntimeException("Invalid action: " + action);
            }

        }

        return (SList) objectStack.pop();
    }
}
