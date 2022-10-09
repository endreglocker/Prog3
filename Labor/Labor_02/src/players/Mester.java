package players;

public class Mester extends Jatekos {
    private int lvl;

    public Mester(int szint) {
        lvl = szint;
    }

    public String toString() {
        return "Mester" + lvl;
    }

    public void lep() {
        super.lep();
        System.out.println("\t\tPlayer name: " + toString());

        double percentile = lvl / 100.0;

        double emeles = asztal.getTet() * (1 + percentile);

        asztal.emel(emeles);
    }

    public void Wim() {
        System.out.println(toString() + " gyozott");
    }
}
