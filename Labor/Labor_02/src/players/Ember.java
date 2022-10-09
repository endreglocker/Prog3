package players;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ember extends Jatekos {
    public void lep() {
        super.lep();
        System.out.print("\t\tMennyivel emeljem a tet?\t");

        //TODO bekeres
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String stream_double = null;
        try {
            stream_double = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double emeles = Double.parseDouble(stream_double);

        asztal.emel(emeles);
    }

}
