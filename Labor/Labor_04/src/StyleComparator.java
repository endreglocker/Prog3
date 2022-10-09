import java.util.Comparator;

public class StyleComparator implements Comparator<Beer> {
    @Override
    public int compare(Beer t, Beer t1) {
        return t.getStyle().compareTo(t1.getStyle());
    }
}
