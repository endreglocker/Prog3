package table;

import players.Jatekos;
import players.NincsJatekos;

import java.util.Random;

public class Asztal {
    private int win_bit = 0;
    private Jatekos[] player = new Jatekos[10];
    private int meret = 0;
    private double tet;
    private int kor;
    private double goal;

    public void ujJatek() {
        Random random = new Random();
        win_bit = 0;
        kor = 0;
        tet = 0;
        goal = random.nextDouble(100.0);
        meret = 0;
        //player = new Jatekos[10];
    }

    public void addJatekos(Jatekos j) {
        if (meret < 10) {
            j.setAsztal(this);
            player[meret++] = j;
        } else {
            System.out.println("Az asztal megtelt!");
        }
    }

    public int getKor() {
        return kor;
    }

    public void emel(double d) {
        tet += d;
    }

    public void kor() {
        if (meret == 0) throw (new NincsJatekos("Nincs jatekos az asztalnal!"));
        if(win_bit == 1) return;
            for (int i = 0; i < meret; i++) {
                player[i].lep();

                if (tet > goal*1.1) {
                    System.out.println("Az " + i + ". jatekos vesztett!");
                    player[i].Wim();
                    win_bit = 1;
                    return;
                }
                if (tet < goal * 1.1 && tet >= goal){
                    //System.out.println("Az " + i + ". jatekos vesztett!");
                    player[i].Wim();
                    win_bit = 1;
                    return;
                }
            }
            kor++;
    }

    public double getTet() {
        return tet;
    }

    public double getGoal() {
        return goal;
    }
}
