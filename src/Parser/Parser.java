package Parser;

import Parser.TableGenerator.TableGenerator;
import Tokens.*;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Parser {
    TokenGetter tokenGetter;
    TableGenerator tableGenerator;
    Map<Integer, Map<String, String>> actionTable;
    Map<Integer, Map<String, Integer>> gotoTable;
    List<Token> tokens;

    public Parser(String filePathName, Grammar grammar) {
        this.tokenGetter = new TokenGetter(filePathName);
        tokenGetter.initialize();
        this.tokens = tokenGetter.getTokens();
        this.tableGenerator = new TableGenerator(grammar);
        this.actionTable = TableGenerator.actionTable;
        this.gotoTable = TableGenerator.gotoTable;
    }

    public void run() {
        Stack<Integer> stateStack = new Stack<>();
        Stack<Object> symbolStack = new Stack<>();
        stateStack.push(0);
        Token currentToken = tokens.getFirst();

        while (currentToken.getType() != TokenType.EOF){
            String tokenType = currentToken.getType().toString();
            String action = actionTable.get(stateStack.peek()).get(tokenType);

            if(action == null){
                System.out.println("Error: No action found for token " + currentToken.getType() + " in state " + stateStack.peek());
                break;
            }
            if (action.charAt(0) == 'S') {
                int trimmed = Integer.parseInt(action.substring(1));
                stateStack.push(trimmed);
                symbolStack.push(tokens.removeFirst());

            } else if (action.charAt(0) == 'R') {
<<<<<<< Updated upstream
                System.out.println(action);
=======
                String productionIndexStr = action.substring(1);
                System.out.println(productionIndexStr);

                Production reductionProduction = getProductionFromAction(productionIndexStr);
                System.out.println("LHS: " + reductionProduction.getLhs());
                System.out.println("RHS: " + reductionProduction.getRhs());

                List<Object> children = new ArrayList<>();
                for(int i=0; i<reductionProduction.rhs.size();i++){
                    stateStack.pop();
                    children.add(symbolStack.pop());
                }
                System.out.println("Minor list " + children);
                Object node = astBuilder.buildAst(reductionProduction, children);
                symbolStack.push(node);

                int newState = gotoTable.get(stateStack.peek()).get(reductionProduction.getLhs());
                stateStack.push(newState);

                System.out.println("AST NODE: " + node);
                // Making the AST Node one at a time
                // Build AST based on production and RHS of production (List of strings)
                // POP
                // Pop the first value on the stack

//                System.out.println("LHS THING" + reductionProduction.getLhs());
//                System.out.println("RHS THING" + reductionProduction.getRhs());
>>>>>>> Stashed changes


            }


            currentToken = tokens.getFirst();
            System.out.println("SymbolStack: " + symbolStack);
            System.out.println("StateStack: " + stateStack);
            System.out.printf("Next Token: " + currentToken);

        }

        //debug loop


//        System.out.println("Tokens: " + tokenGetter.getTokens());
//        System.out.println("Action Table: " + actionTable);
//        System.out.println("Goto Table: " + gotoTable);

    }

}
