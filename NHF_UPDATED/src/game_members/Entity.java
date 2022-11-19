package game_members;

import game.*;

import java.awt.*;

public abstract class Entity {

    protected DescartesVector position;

    protected DescartesVector velocity;

    protected double rotation;

    protected double radius;

    private boolean needsRemoval;

    private final int killScore;

    public Entity(DescartesVector position, DescartesVector velocity, double radius, int killScore) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.rotation = 0.0f;
        this.killScore = killScore;
        this.needsRemoval = false;
    }

    public void rotate(double amount) {
        this.rotation += amount;
        this.rotation %= Math.PI * 2;
    }

    public int getKillScore() {
        return killScore;
    }

    public void flagForRemoval() {
        this.needsRemoval = true;
    }

    public DescartesVector getPosition() {
        return position;
    }

    /**
     * public DescartesVector getVelocity() {
     * return velocity;
     * }
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
            position.x += PlayPanel.WORLD_SIZE;
        }
        if (position.y < 0.0f) {
            position.y += PlayPanel.WORLD_SIZE;
        }
        position.x %= PlayPanel.WORLD_SIZE;
        position.y %= PlayPanel.WORLD_SIZE;
    }

    public boolean checkCollision(Entity entity) {
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
        double radius = entity.getCollisionRadius() + getCollisionRadius();
        return (position.distanceOfVectors(entity.position) < radius * radius);
    }

    /**
     * Handle a collision with another Entity.
     *
     * @param game  The game instance.
     * @param other The Entity that we collided with.
     */
    public abstract void handleCollision(GameFrame game, Entity other);

    /**
     * Draw this Entity onto the window.
     *
     * @param g    The Graphics instance.
     * @param game The game instance.
     */
    public abstract void draw(Graphics2D g, GameFrame game);
}
