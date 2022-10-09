package players;

import table.Asztal;

public abstract class Jatekos {
    protected Asztal asztal;

    public void lep() {
        System.out.print((asztal.getKor() + 1) + ". round (offset = 1)\t\t" + "bet:\t" + asztal.getTet() /*+ "\t\t\tgoal:\t" + asztal.getGoal()*/);
    }

    public void setAsztal(Asztal a) {
        asztal = a;
    }

    public void Wim(){

    }
/**
    @Override
    public void finalize(){
        super.finalize();
        System.out.println(this + "     " + toString());
    }
 */
}
