package game_members;

import java.awt.*;

public enum AsteroidSize {

    Small(15.0, 100),

    Medium(25.0, 50),

    Large(40.0, 20);

    private static final int NUMBER_OF_POINTS = 5;

    public final Polygon polygon;

    public final double radius;

    public final int killValue;

    AsteroidSize(double radius, int value) {
        this.polygon = generatePolygon(radius);
        this.radius = radius + 1.0;
        this.killValue = value;
    }

    private static Polygon generatePolygon(double radius) {
        //Create an array to store the coordinates.
        int[] x = new int[NUMBER_OF_POINTS];
        int[] y = new int[NUMBER_OF_POINTS];

        //Generate the points in the polygon.
        double angle = (2 * Math.PI / NUMBER_OF_POINTS);
        for (int i = 0; i < NUMBER_OF_POINTS; i++) {
            x[i] = (int) (radius * Math.sin(i * angle));
            y[i] = (int) (radius * Math.cos(i * angle));
        }

        //Create a new polygon from the generated points and return it.
        return new Polygon(x, y, NUMBER_OF_POINTS);
    }

}
