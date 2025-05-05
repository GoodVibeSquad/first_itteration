import Parser.Grammar;
import Parser.Production;
import Parser.TableGenerator.LRItem;
import Parser.TableGenerator.TableGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

public class TableGeneratorTest {
    @BeforeEach
    void setup() {
        TableGenerator.actionTable = new HashMap<>();
        TableGenerator.gotoTable = new HashMap<>();
    }

    @Test
    void testSimpleClosure() {
        Grammar grammar = Mockito.mock(Grammar.class);
        Production production = new Production("S", List.of("A"));
        Mockito.when(grammar.getProductions()).thenReturn(List.of(production));

        LRItem item = new LRItem(production, 0);
        Set<LRItem> items = new HashSet<>();
        items.add(item);

        Set<LRItem> closureSet = TableGenerator.closure(items, grammar);
        Assertions.assertEquals(1, closureSet.size());
    }

    @Test
    void testSimpleGotoSet(){
        Grammar grammar = new Grammar();
        grammar.add("S", "a", "b");

        Set<LRItem> items = Set.of(new LRItem(new Production("S", List.of("a", "b")), 0));
        Set<LRItem> nextState = TableGenerator.gotoState(items, "a", grammar);

        Assertions.assertTrue(nextState.contains(new LRItem(new Production("S", List.of("a", "b")), 1)));
    }

    @Test
    void testComputeFirst() {
        Grammar grammar = new Grammar();
        grammar.add("A", "a", "b");

        Set<String> firstSet = TableGenerator.computeFirst("A", grammar);

        Assertions.assertTrue(firstSet.contains("a"));
    }


    @Test
    void testComputeFollowSets() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");
        grammar.add("S", "A", "B");
        grammar.add("A", "a");
        grammar.add("B", "b");

        Map<String, Set<String>> followSets = TableGenerator.computeFollowSets(grammar);

        Assertions.assertTrue(followSets.get("A").contains("b")); // After A comes B -> b
        Assertions.assertTrue(followSets.get("S").contains("EOF")); // Start symbol always follows with EOF
    }

    @Test
    void testBuildSLRParserSimpleShift() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");
        grammar.add("S", "a", "S");
        grammar.add("S", "a");

        new TableGenerator(grammar);

        Assertions.assertTrue(TableGenerator.actionTable.size() > 0);
        Assertions.assertTrue(TableGenerator.gotoTable.size() > 0);

        Assertions.assertEquals("S1", TableGenerator.actionTable.get(0).get("a"));  // shift action
    }

    @Test
    void testBuildSLRParserReduceAction() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");
        grammar.add("S", "a");

        new TableGenerator(grammar);

        boolean reduceFound = TableGenerator.actionTable.values().stream()
                .flatMap(map -> map.values().stream())
                .anyMatch(action -> action.startsWith("R"));

        Assertions.assertTrue(reduceFound, "Expected a reduce action in the table.");
    }

    @Test
    void testLeftRecursionHandling() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");
        grammar.add("S", "S", "a");
        grammar.add("S", "b");

        new TableGenerator(grammar);

        // Expect at least a reduce somewhere (b -> S)
        boolean reduceFound = TableGenerator.actionTable.values().stream()
                .flatMap(map -> map.values().stream())
                .anyMatch(action -> action.startsWith("R"));

        Assertions.assertTrue(reduceFound);
    }

    @Test
    void testNullableProduction() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");
        grammar.add("S", "A");
        grammar.add("A"); // A → ε
        grammar.add("A", "a");

        new TableGenerator(grammar);

        // A's follow set should include EOF (as S → A and A → ε)
        Set<String> followA = TableGenerator.computeFollowSets(grammar).get("A");
        Assertions.assertTrue(followA.contains("EOF"));
    }

    @Test
    void testUnreachableNonTerminalIgnored() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");
        grammar.add("S", "a");
        grammar.add("X", "x"); // X is never used

        new TableGenerator(grammar);

        // No action or goto entries should involve 'X'
        boolean hasX = TableGenerator.actionTable.values().stream()
                .flatMap(m -> m.keySet().stream())
                .anyMatch(sym -> sym.equals("X"));

        Assertions.assertFalse(hasX);
    }

    @Test
    void testDanglingElsePrefersShift() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("stmt");
        grammar.add("stmt", "matched");
        grammar.add("stmt", "unmatched");

        grammar.add("matched", "IF", "cond", "matched", "ELSE", "matched");
        grammar.add("matched", "PRINT", "SEMICOLON");

        grammar.add("unmatched", "IF", "cond", "stmt");
        grammar.add("unmatched", "IF", "cond", "matched", "ELSE", "unmatched");

        new TableGenerator(grammar);

        boolean elseShiftFound = false;

        for (var entry : TableGenerator.actionTable.entrySet()) {
            Map<String, String> row = entry.getValue();
            String action = row.get("ELSE");
            if (action != null) {
                System.out.println("State " + entry.getKey() + ", ELSE => " + action);
                if (action.startsWith("S")) {
                    elseShiftFound = true;
                }
            }
        }

        Assertions.assertTrue(elseShiftFound, "Expected at least one shift action on ELSE to resolve dangling else");
    }

    @Test
    void testAmbiguousGrammarStillGeneratesTables() {
        Grammar grammar = new Grammar();
        grammar.setStartSymbol("S");
        grammar.add("S", "a", "S", "b");
        grammar.add("S");

        Assertions.assertDoesNotThrow(() -> new TableGenerator(grammar));
    }

    @Test
    void testEmptyGrammarThrows() {
        Grammar grammar = new Grammar();
        Assertions.assertThrows(NullPointerException.class, () -> new TableGenerator(grammar));
    }

}
