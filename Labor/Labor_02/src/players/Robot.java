package players;

public class Robot extends Jatekos {
    private int id = 0;

    public Robot() {
        id++;
    }

    public String toString() {
        return "Robot" + id;
    }

    public void lep() {
        super.lep();
        System.out.println("\t\tPlayer name:\t" + toString());
        asztal.emel(0.0);
    }

    public void Wim() {
        System.out.println("Bip-Bop");
    }
}
