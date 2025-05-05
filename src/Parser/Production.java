package Parser;

import java.util.List;
import java.util.Objects;

public class Production {
    String lhs;
    List<String> rhs;

    public Production(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String getLhs() {
        return lhs;
    }

    public List<String> getRhs() {
        return rhs;
    }

    public String toString() {
        return lhs + " -> " + String.join(" ", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Production that)) return false;
        return Objects.equals(lhs, that.lhs) && Objects.equals(rhs, that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}