package game;

import java.awt.*;

public enum AsteroidSize {

    Small(15.0, 100),

    Medium(25.0, 50),

    Large(40.0, 20);

    private static final int polygonVertex = 8;

    public final Polygon polygon;

    public final double radius;

    public final int scoreForKilling;

    AsteroidSize(double r, int value) {
        polygon = generatePolygon(r);
        radius = r + 1.0;
        scoreForKilling = value;
    }

    private static Polygon generatePolygon(double radius) {
        //Create an array to store the coordinates.
        int[] x = new int[polygonVertex];
        int[] y = new int[polygonVertex];

        //Generate the points in the polygon.
        double angle = (2 * Math.PI / polygonVertex);
        for (int i = 0; i < polygonVertex; i++) {
            x[i] = (int) (radius * Math.sin(i * angle));
            y[i] = (int) (radius * Math.cos(i * angle));
        }

        //Create a new polygon from the generated points and return it.
        return new Polygon(x, y, polygonVertex);
    }

}
