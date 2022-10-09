package players;

public class Nyuszi extends Jatekos {
    protected String szin;

    public Nyuszi() {
        szin = "Feher";
    }

    public Nyuszi(String s) {
        szin = s;
    }

    public String toString() {
        return szin;
    }

    public void lep() {
        if (asztal.getTet() < 50.0) {
            super.lep();
            System.out.println("\t\tRabbit color: " + toString());
            asztal.emel(5.0);
        }

        if (asztal.getTet() >= 50.0) {
            asztal.emel(0.0);
            System.out.println(asztal.getTet() + "\t\tHuha!");
        }
    }

    public void Wim() {
        System.out.println(toString() + " gyozott");
    }
}
