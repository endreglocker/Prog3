package players;

public class Kezdo extends Jatekos {
    private String nev;

    public Kezdo() {
        nev = "Nincs nev";
    }

    public Kezdo(String a) {
        nev = a;
    }

    public void lep() {
        super.lep();
        System.out.println("\t\tPlayer name:\t" + nev);
        if (asztal.getKor() % 2 == 1) {
            asztal.emel(0.0);
        } else {
            asztal.emel(1.0);
        }
    }

    public void Wim() {
        System.out.println(toString() + " gyozott");
    }

    public String toString() {
        return nev;
    }

}
