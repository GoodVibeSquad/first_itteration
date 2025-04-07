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
            String tokenType = currentToken.getType().toString().toLowerCase();
            String action = actionTable.get(stateStack.peek()).get(tokenType);

            if (action.charAt(0) == 'S') {
                int trimmed = Integer.parseInt(action.substring(1));
                stateStack.push(trimmed);
                symbolStack.push(tokens.removeFirst());

            } else if (action.charAt(0) == 'R') {
                System.out.println(action);


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

}
