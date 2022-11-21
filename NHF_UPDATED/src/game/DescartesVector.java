package game;

public class DescartesVector {

    double x;

    double y;

    public DescartesVector(double angle) {
        x = Math.cos(angle);
        y = Math.sin(angle);
    }

    public DescartesVector(double vx, double vy) {
        x = vx;
        y = vy;
    }

    public DescartesVector(DescartesVector vec) {
        x = vec.x;
        y = vec.y;
    }

    public DescartesVector addVectorToVector(DescartesVector vec) {
        x += vec.x;
        y += vec.y;
        return this;
    }

    public DescartesVector multiply_by_scalar(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public DescartesVector unitVector() {
        double length = vectorSizeSquared();
        if (length != 0.0 && length != 1.0) {
            length = Math.sqrt(length);
            x /= length;
            y /= length;
        }
        return this;
    }

    public double vectorSizeSquared() {
        return (x * x + y * y);
    }

    public double distanceOfVectors(DescartesVector vec) {
        double dx = x - vec.x;
        double dy = y - vec.y;
        return (dx * dx + dy * dy);
    }

    public void setXY(double vx, double vy) {
        x = vx;
        y = vy;
        //return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}