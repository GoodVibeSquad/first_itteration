package Parser;

import java.util.List;

public class Production {
    String lhs;
    List<String> rhs;

    Production(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public List<String> getRhs() {
        return rhs;
    }

    public String toString() {
        return lhs + " -> " + String.join(" ", rhs);
    }
}