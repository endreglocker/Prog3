import java.io.Serializable;

public class Beer implements Serializable {
    private String name;
    private String style;
    private double strength;

    public Beer() {
        name = "";
        style = "";
        strength = 0.0;
    }

    public Beer(String n, String s, double str) {
        name = n;
        style = s;
        strength = str;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    public double getStrength() {
        return strength;
    }

    public String toString() {
        return name + " " + style + " " + strength + "%";
    }
}
