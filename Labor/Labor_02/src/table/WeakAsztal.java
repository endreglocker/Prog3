package table;

import players.Jatekos;

import java.lang.ref.WeakReference;

public class WeakAsztal extends Asztal {
    private final int max_meret = 10;
    private int jelenlegi_meret = 0;
    WeakReference<Jatekos>[] player = (WeakReference<Jatekos>[]) new WeakReference[max_meret];

    public WeakAsztal() {
        for(int i = 0; i < max_meret; i++){
            player[i] = new WeakReference<Jatekos>(null);
        }
    }

    public void addJatekos(Jatekos j) {
        if(jelenlegi_meret < max_meret){
            j.setAsztal(this);
            player[jelenlegi_meret++] = new WeakReference<Jatekos>(j);
        }
    }
}
