package Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Parser.Production;

public class Grammar {
    List<Production> productions = new ArrayList<>();
    String startSymbol;

    void add(String lhs, String... rhs) {
        productions.add(new Production(lhs, Arrays.asList(rhs)));
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public List<Production> getProductions() {
        return productions;
    }
}