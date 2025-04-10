package Parser;

import Parser.TableGenerator.TableGenerator;

import java.util.*;

public class Parser {

    public static void parse(List<String> input) {
        Stack<Integer> stateStack = new Stack<>();
        stateStack.push(0); // Start state

        Queue<String> tokenStream = new LinkedList<>(input);
        tokenStream.add("EOF"); // End marker

        String nextSymbol = tokenStream.poll();

        while (true) {
            int currentState = stateStack.peek();
            String action = TableGenerator.actionTable
                    .getOrDefault(currentState, Map.of())
                    .getOrDefault(nextSymbol, null);

            if (action == null) {
                System.err.println("Syntax error at symbol: " + nextSymbol);
                System.err.println("In state: " + currentState);
                System.err.println("Available actions: " + TableGenerator.actionTable.getOrDefault(currentState, Map.of()));
                return;
            }


            if (action.startsWith("S")) {
                int state = Integer.parseInt(action.substring(1));

                stateStack.push(state);
               System.out.println("Shifted " + stateStack.peek());
                nextSymbol = tokenStream.poll(); // move to next input symbol
            } else if (action.startsWith("R")) {
                String prodStr = action.substring(1); // format: "A -> β"
                String[] parts = prodStr.split("->");
                String lhs = parts[0].trim();
                String[] rhs = parts[1].trim().split("\\s+");

                int popCount = rhs[0].equals("") ? 0 : rhs.length;
                for (int i = 0; i < popCount; i++) {
                    stateStack.pop();
                }

                currentState = stateStack.peek();
                Integer nextState = TableGenerator.gotoTable
                        .getOrDefault(currentState, Map.of())
                        .get(lhs);

                if (nextState == null) {
                    System.err.println("Goto error for non-terminal: " + lhs);
                    return;
                }

                stateStack.push(nextState);
                System.out.println("Reduced using: " + lhs + " -> " + String.join(" ", rhs));
            } else if (action.equals("ACC")) {
                System.out.println("Input successfully parsed!");
                break;
            } else {
                System.err.println("Invalid action: " + action);
                return;
            }

        }
        System.out.println("\n");
    }
}
