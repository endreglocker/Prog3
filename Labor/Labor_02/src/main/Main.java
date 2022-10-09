package main;

import players.*;
import table.Asztal;
import table.WeakAsztal;

public class Main {
    public static void main(String[] args) {

         Asztal base = new Asztal();

         base.ujJatek();

        /**base.kor();*/


        base.addJatekos(new Kezdo());
        base.addJatekos(new Robot());
        base.addJatekos(new Kezdo());



         base.addJatekos(new Nyuszi("Keksz"));
         base.addJatekos(new Mester(3));


        //base.addJatekos(new Ember());
        for (int i = 0; i < 3; i++) {
            base.kor();
        }

        /*
        WeakAsztal t[] = new WeakAsztal[100000000];
        for (int i = 0; i < 100000000; i++) {
            WeakAsztal temp = new WeakAsztal();
            temp.ujJatek();
            for(int j = 0; j < 10; j++){
                temp.addJatekos(new Kezdo());
            }
            t[i] = temp;
        }
        */
        //base = null;

        System.gc();
    }
}
