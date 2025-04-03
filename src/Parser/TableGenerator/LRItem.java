package Parser.TableGenerator;

import Parser.Production;

import java.util.Objects;

public class LRItem {
    Production production;
    private final int dotPosition;

    public LRItem(Production production, int dotPosition) {
        this.production = production;
        this.dotPosition = dotPosition;
    }

    public Boolean isComplete() {
        return dotPosition >= production.getRhs().size();
    }

    public String nextSymbol() {

        return isComplete() ? null : production.getRhs().get(dotPosition);

    }

    public LRItem advance(){
        return isComplete() ? null : new LRItem(production, dotPosition + 1);
    }

    @Override
    public String toString() {
        return "LRItem{" +
                "productionIndex=" + production +
                ", dotPosition=" + dotPosition +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRItem lrItem = (LRItem) o;
        return dotPosition == lrItem.dotPosition && production.equals(lrItem.production);
    }

    public int hashCode() {
        return Objects.hash(production, dotPosition);
    }


}
