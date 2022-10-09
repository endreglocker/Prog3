import java.util.Comparator;

public class StrengthComparator implements Comparator<Beer> {
    @Override
    public int compare(Beer t, Beer t1) {
        if (t.getStrength() == t1.getStrength())
            return 0;
        else if (t.getStrength() < t1.getStrength())
            return -1;
        else
            return 1;
    }
}
