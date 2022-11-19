package game_members;

import game.*;

import java.awt.*;
import java.util.ArrayList;


public class Player extends Entity {

    private static final double DEFAULT_ROTATION = -Math.PI / 2.0;

    private static final double THRUST_MAGNITUDE = 0.0385;

    private static final double MAX_VELOCITY_MAGNITUDE = 6.5;

    private static final double ROTATION_SPEED = 0.052;

    private static final double SLOW_RATE = 0.995;

    private static final int MAX_BULLETS = 4;

    private static final int FIRE_RATE = 4;

    private static final int MAX_CONSECUTIVE_SHOTS = 8;

    private static final int MAX_OVERHEAT = 30;

    private boolean thrustPressed;

    private boolean rotateLeftPressed;

    private boolean rotateRightPressed;

    private boolean firePressed;

    private boolean firingEnabled;

    private int consecutiveShots;

    private int fireCooldown;

    private int overheatCooldown;

    private int animationFrame;

    private final ArrayList<Bullet> bullets;

    public Player() {
        super(new DescartesVector(PlayPanel.WORLD_SIZE / 2.0, PlayPanel.WORLD_SIZE / 2.0), new DescartesVector(0.0, 0.0), 10.0, 0);
        this.bullets = new ArrayList<>();
        this.rotation = DEFAULT_ROTATION;
        this.thrustPressed = false;
        this.rotateLeftPressed = false;
        this.rotateRightPressed = false;
        this.firePressed = false;
        this.firingEnabled = true;
        this.fireCooldown = 0;
        this.overheatCooldown = 0;
        this.animationFrame = 0;
    }

    public void setThrusting(boolean state) {
        this.thrustPressed = state;
    }

    public void setRotateLeft(boolean state) {
        this.rotateLeftPressed = state;
    }

    public void setRotateRight(boolean state) {
        this.rotateRightPressed = state;
    }

    public void setFiring(boolean state) {
        this.firePressed = state;
    }

    public void setFiringEnabled(boolean state) {
        this.firingEnabled = state;
    }

    public void reset() {
        this.rotation = DEFAULT_ROTATION;
        position.setXY(PlayPanel.WORLD_SIZE / 2.0, PlayPanel.WORLD_SIZE / 2.0);
        velocity.setXY(0.0, 0.0);
        bullets.clear();
    }

    @Override
    public void update(GameFrame game) {
        super.update(game);

        //Increment the animation frame.
        this.animationFrame++;

        /*
         * Rotate the ship if only one of the rotation flags are true, as doing
         * one rotation will cancel the effect of doing the other.
         *
         * The conditional statement can alternatively be written like this:
         *
         * if(rotateLeftPressed) {
         *     rotate(-ROTATION_SPEED);
         * } else {
         *     rotate(ROTATION_SPEED);
         * }
         */
        if (rotateLeftPressed != rotateRightPressed) {
            rotate(rotateLeftPressed ? -ROTATION_SPEED : ROTATION_SPEED);
        }

        /*
         * Apply thrust to our ship's velocity, and ensure that the ship is not
         * going faster than the maximum magnitude.
         */
        if (thrustPressed) {
            /*
             * Here we create a new vector based on our ship's rotation, and scale
             * it by our thrust's magnitude. Then we add that vector to our velocity.
             */
            velocity.addVectorToVector(new DescartesVector(rotation).multiply_by_scalar(THRUST_MAGNITUDE));

            /*
             * Here we determine whether our ship is going faster than is
             * allowed. Like when checking for collisions, we check the squared
             * magnitude because it is quicker to square a value than it is to
             * take the square root.
             *
             * If our velocity exceeds our maximum allowed velocity, we normalize
             * it (giving it a magnitude of 1.0), and scale it to be he maximum.
             */
            if (velocity.vectorSizeSquared() >= MAX_VELOCITY_MAGNITUDE * MAX_VELOCITY_MAGNITUDE) {
                velocity.unitVector().multiply_by_scalar(MAX_VELOCITY_MAGNITUDE);
            }
        }

        /*
         * If our ship is moving, slow it down slightly, which causes the ship
         * to some to a gradual stop.
         */
        if (velocity.vectorSizeSquared() != 0.0) {
            velocity.multiply_by_scalar(SLOW_RATE);
        }

        /*
         * Loop through each bullet and remove it from the list if necessary.
         */
        bullets.removeIf(Entity::needsRemoval);

        /*
         * Decrement the fire and overheat cool-downs, and determine if we can fire another
         * bullet.
         */
        this.fireCooldown--;
        this.overheatCooldown--;
        if (firingEnabled && firePressed && fireCooldown <= 0 && overheatCooldown <= 0) {
            /*
             * We can only create a new bullet if we haven't yet exceeded the
             * maximum number of bullets that we can have fired at once.
             *
             * If a new bullet can be fired, we reset the fire cooldown, and
             * register a new bullet to the game world.
             */
            if (bullets.size() < MAX_BULLETS) {
                this.fireCooldown = FIRE_RATE;

                Bullet bullet = new Bullet(this, rotation);
                bullets.add(bullet);
                game.registerEntity(bullet);
            }

            /*
             * Since we're attempting to fire a bullet, we increment the number
             * of consecutive shots and determine if we should set to overheat
             * flag.
             *
             * This prevents us from being able to wipe out entire groups of
             * asteroids in one burst if we're accurate enough, and will prevent
             * us from firing a continuous stream of bullets until we start missing.
             */
            this.consecutiveShots++;
            if (consecutiveShots == MAX_CONSECUTIVE_SHOTS) {
                this.consecutiveShots = 0;
                this.overheatCooldown = MAX_OVERHEAT;
            }
        } else if (consecutiveShots > 0) {
            //Decrement the number of consecutive shots, since we're not trying to fire.
            this.consecutiveShots--;
        }
    }

    @Override
    public void handleCollision(GameFrame game, Entity other) {
        //Kill the player if it collides with an Asteroid.
        if (other.getClass() == Asteroid.class) {
            game.killPlayer();
        }
    }

    @Override
    public void draw(Graphics2D g, GameFrame game) {
        /*
         * When the player recently spawned, it will flash for a few seconds to indicate
         * that it is invulnerable. The player will not flash if the game is paused.
         */
        if (!game.isPlayerInvulnerable() || game.isPaused() || animationFrame % 20 < 10) {
            /*
             * Draw the ship. The nose will face right (0.0 on the unit circle). All
             * transformations will be handled by the WorldPanel before calling the draw
             * function.
             */
            g.drawLine(-10, -8, 10, 0);
            g.drawLine(-10, 8, 10, 0);
            g.drawLine(-6, -6, -6, 6);

            //Draw the flames behind the ship if we're thrusting, and not paused.
            if (!game.isPaused() && thrustPressed && animationFrame % 6 < 3) {
                g.drawLine(-6, -6, -12, 0);
                g.drawLine(-6, 6, -12, 0);
            }
        }
    }

}
