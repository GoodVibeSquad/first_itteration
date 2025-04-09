package Parser;

import Parser.TableGenerator.TableGenerator;
import Tokens.*;

import java.util.ArrayList;
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
        ASTBuilder astBuilder = new ASTBuilder();
        stateStack.push(0);
        Token currentToken = tokens.getFirst();

        while (currentToken.getType() != TokenType.EOF){
            String tokenType = currentToken.getType().toString().toLowerCase();
            String action = actionTable.get(stateStack.peek()).get(tokenType);

            if (action.charAt(0) == 'S') {
                int trimmed = Integer.parseInt(action.substring(1));
                stateStack.push(trimmed);
                symbolStack.push(tokens.removeFirst());

            } else if (action.charAt(0) == 'R') {
                System.out.println(action);

                String productionIndexStr = action.substring(1);
                System.out.println(productionIndexStr);
                Production reductionProduction = getProductionFromAction(productionIndexStr);

                List<Token> children = new ArrayList<>();
                for(int i=0; i<reductionProduction.rhs.size();i++){
                    stateStack.pop();
                    children.add((Token) symbolStack.pop());
                }

                Object node = astBuilder.buildAst(reductionProduction, children);

                System.out.println("AST NODE MAYBE?" + node);
                // Making the AST Node one at a time
                // Build AST based on production and RHS of production (List of strings)
                // POP
                // Pop the first value on the stack

                System.out.println("Reduction used for production: " + reductionProduction);
                System.out.println("LHS THING" + reductionProduction.getLhs());
                System.out.println("RHS THING" + reductionProduction.getRhs());


            }


            currentToken = tokens.getFirst();
            System.out.println("SymbolStack: " + symbolStack);
            System.out.println("StateStack: " + stateStack);
            System.out.printf("CurrentToken: " + currentToken);

        }

        //debug loop


//        System.out.println("Tokens: " + tokenGetter.getTokens());
//        System.out.println("Action Table: " + actionTable);
//        System.out.println("Goto Table: " + gotoTable);

    }

    private Production getProductionFromAction(String action) {
        String productionString = action.substring(0);
        String[] parts = productionString.split(" -> ");
        String lhs = parts[0];
        List<String> rhs = List.of(parts[1].split(" "));

        return new Production(lhs, rhs);
    }
}
