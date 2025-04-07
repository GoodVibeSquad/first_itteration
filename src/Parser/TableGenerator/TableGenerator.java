package Parser.TableGenerator;

import Parser.Grammar;
import Parser.*;

import java.util.*;

public class TableGenerator {

    public static Map<Integer, Map<String, String>> actionTable = new HashMap<>();
    public static Map<Integer, Map<String, Integer>> gotoTable = new HashMap<>();

    public TableGenerator(Grammar grammar) {
        buildSLRParser(grammar);
    }

    //closure metode
    public static Set<LRItem> closure(Set<LRItem> items, Grammar grammar) {
        Set<LRItem> closureSet = new HashSet<>(items);
        boolean isRunning;

        do {
            isRunning = false;
            Set<LRItem> newItems = new HashSet<>();

            for (LRItem item : closureSet) {
                String next = item.nextSymbol();
                if (next != null && isNonTerminal(next, grammar)) {
                    for (Production p : grammar.getProductions()) {
                        if (p.getLhs().equals(next)) {
                            LRItem newItem = new LRItem(p, 0);
                            if (!closureSet.contains(newItem)) {
                                newItems.add(newItem);
                            }
                        }
                    }
                }
            }

            if (!newItems.isEmpty()) {
                closureSet.addAll(newItems);
                isRunning = true;
            }

        } while (isRunning);

        return closureSet;
    }

    //Goto metode
    public static Set<LRItem> gotoSet(Set<LRItem> items, String Symbol, Grammar grammar) {
        Set<LRItem> nextSet = new HashSet<>();
        for (LRItem item : items) {
            if (Symbol.equals(item.nextSymbol())) {
                nextSet.add(item.advance());
            }
        }
        return closure(nextSet, grammar);
    }

    static boolean isNonTerminal(String symbol, Grammar grammar) {
        for (Production p : grammar.getProductions()) {
            if (p.getLhs().equals(symbol)) return true;
        }
        return false;
    }

    static Set<String> first(String nonTerminal, Grammar grammar) {
        Set<String> firstSet = new HashSet<>();
        for (Production p : grammar.getProductions()) {
            if (p.getLhs().equals(nonTerminal)) {
                if (!p.getRhs().isEmpty()) {
                    firstSet.add(p.getRhs().get(0));
                }
            }
        }
        return firstSet;
    }

    static Set<String> isFirst(String symbol, Grammar grammar) {
        if (!isNonTerminal(symbol, grammar)) {
            return Set.of(symbol);
        } else {
            return first(symbol, grammar);
        }
    }

    static Map<String, Set<String>> computeFollowSets(Grammar grammar) {
        Map<String, Set<String>> follow = new HashMap<>();

        for (Production p : grammar.getProductions()) {
            follow.putIfAbsent(p.getLhs(), new HashSet<>());
        }

        follow.get(grammar.getStartSymbol()).add("EOF");

        boolean changed;
        do {
            changed = false;
            for (Production p : grammar.getProductions()) {
                List<String> rhs = p.getRhs();
                for (int i = 0; i < rhs.size(); i++) {
                    String B = rhs.get(i);
                    if (!isNonTerminal(B, grammar)) continue;

                    Set<String> followB = follow.get(B);
                    int before = followB.size();

                    if (i + 1 < rhs.size()) {
                        String next = rhs.get(i + 1);
                        followB.addAll(isFirst(next, grammar));
                    } else {
                        followB.addAll(follow.get(p.getLhs()));
                    }

                    if (followB.size() > before) {
                        changed = true;
                    }
                }
            }
        } while (changed);

        return follow;
    }

    // === Build SLR(1) Automaton with Tables ===
    public static void buildSLRParser(Grammar grammar) {
        Map<Set<LRItem>, Integer> stateNumbers = new HashMap<>();
        List<Set<LRItem>> states = new ArrayList<>();
        Map<Integer, Map<String, Integer>> transitions = new HashMap<>();

        Production startProd = new Production("S'", List.of(grammar.getStartSymbol()));
        Set<LRItem> startState = closure(Set.of(new LRItem(startProd, 0)), grammar);
        states.add(startState);
        stateNumbers.put(startState, 0);

        Queue<Set<LRItem>> queue = new LinkedList<>();
        queue.add(startState);

        while (!queue.isEmpty()) {
            Set<LRItem> state = queue.poll();
            int stateNum = stateNumbers.get(state);

            Set<String> symbols = new HashSet<>();
            for (LRItem item : state) {
                String next = item.nextSymbol();
                if (next != null) {
                    symbols.add(next);
                }
            }

            for (String symbol : symbols) {
                Set<LRItem> target = gotoSet(state, symbol, grammar);

                if (!stateNumbers.containsKey(target)) {
                    int newStateNum = states.size();
                    stateNumbers.put(target, newStateNum);
                    states.add(target);
                    queue.add(target);
                }

                int targetNum = stateNumbers.get(target);
                transitions.computeIfAbsent(stateNum, k -> new HashMap<>()).put(symbol, targetNum);
            }
        }

        Map<String, Set<String>> followSets = computeFollowSets(grammar);
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();


        for (int i = 0; i < states.size(); i++) {
            Set<LRItem> state = states.get(i);

            for (LRItem item : state) {
                if (!item.isComplete()) {
                    String symbol = item.nextSymbol();
                    int target = transitions.get(i).get(symbol);
                    if (!isNonTerminal(symbol, grammar)) {
                        actionTable.computeIfAbsent(i, k -> new HashMap<>()).put(symbol, "S" + target);
                    } else {
                        gotoTable.computeIfAbsent(i, k -> new HashMap<>()).put(symbol, target);
                    }
                } else {
                    if (item.production.getLhs().equals("S'")) {
                        actionTable.computeIfAbsent(i, k -> new HashMap<>()).put("EOF", "ACC");
                    } else {
                        for (String terminal : followSets.get(item.production.getLhs())) {
                            actionTable.computeIfAbsent(i, k -> new HashMap<>()).put(terminal, "R" + item.production);
                        }
                    }
                }
            }
        }

//        System.out.println("\n=== ACTION TABLE ===");
//        for (var entry : actionTable.entrySet()) {
//            System.out.println("State " + entry.getKey() + ": " + entry.getValue());
//        }
//
//        System.out.println("\n=== GOTO TABLE ===");
//        for (var entry : gotoTable.entrySet()) {
//            System.out.println("State " + entry.getKey() + ": " + entry.getValue());
//        }
    }
}
