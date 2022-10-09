import java.util.Comparator;

public class NameComparator implements Comparator<Beer>{
    @Override
    public int compare(Beer t, Beer t1) {
            return t.getName().compareTo(t1.getName());
    }
}
