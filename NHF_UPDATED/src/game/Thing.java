package game;

import java.awt.*;

public abstract class Thing {

    protected DescartesVector position;

    protected DescartesVector velocity;

    protected double rotation;

    protected double radius;

    private boolean needsRemoval;

    private final int scoredPoints;

    public Thing(DescartesVector pos, DescartesVector vel, double rad, int kill) {
        position = pos;
        velocity = vel;
        radius = rad;
        rotation = 0.0;
        scoredPoints = kill;
        needsRemoval = false;
    }

    public void rotate(double amount) {
        rotation += amount;
        rotation %= Math.PI * 2;
    }

    public int getScoredPoints() {
        return scoredPoints;
    }

    public void mustRemove() {
        needsRemoval = true;
    }

    public DescartesVector getPosition() {
        return position;
    }

    /*
     public DescartesVector getVelocity() {
        return velocity;
     }
     */
    public double getRotation() {
        return rotation;
    }

    public double getCollisionRadius() {
        return radius;
    }

    public boolean needsRemoval() {
        return needsRemoval;
    }

    public void update(GameFrame game) {
        position.addVectorToVector(velocity);
        if (position.x < 0.0f) {
            position.x += PlayPanel.worldSize;
        }
        if (position.y < 0.0f) {
            position.y += PlayPanel.worldSize;
        }
        position.x %= PlayPanel.worldSize;
        position.y %= PlayPanel.worldSize;
    }

    public boolean collisionDetector(Thing thing) {
        /*
         * Here we use the Pythagorean Theorem to determine whether the two
         * Entities are close enough to collide.
         *
         * The reason we are squaring everything is that it's much, much
         * quicker to square one variable than it is to take the square root
         * of another. While this game is simple enough that such minor
         * optimizations are unnecessary, it's still a good habit to get
         * into.
         */
        double radius = thing.getCollisionRadius() + getCollisionRadius();
        return (position.distanceOfVectors(thing.position) < radius * radius);
    }

    public abstract void handleCollision(GameFrame game, Thing other);

    public abstract void draw(Graphics2D g, GameFrame game);
}
