package game;

import java.awt.*;
import java.util.ArrayList;


public class SpaceShip extends Thing {

    private static final double defRotation = -Math.PI / 2.0;

    private static final double thrust = 0.025;

    private static final double acceleration = 5;

    private static final double rotationSpeed = 0.05;

    private static final double slowingForce = 0.999;

    /// CHOOSE A LOWER NUMBER !!!!
    private static final int bulletNumber = 3;

    private static final int rateOfFire = 4;

    private static final int maxShots = 8;

    //private static final int MAX_OVERHEAT = 30;

    private boolean thrustPressed;

    private boolean rotateLeftPressed;

    private boolean rotateRightPressed;

    private boolean firePressed;

    private boolean firingEnabled;

    private int consecutiveShots;

    private int fireCooldown;

    //private int overheatCooldown;

    private int animationFrame;

    private final ArrayList<Bullet> bullets;

    public SpaceShip() {
        super(new DescartesVector(PlayPanel.worldSize / 2.0, PlayPanel.worldSize / 2.0), new DescartesVector(0.0, 0.0), 10.0, 0);
        bullets = new ArrayList<>();
        rotation = defRotation;
        thrustPressed = false;
        rotateLeftPressed = false;
        rotateRightPressed = false;
        firePressed = false;
        firingEnabled = true;
        fireCooldown = 0;
        //overheatCooldown = 0;
        animationFrame = 0;
    }

    public void setThrusting(boolean state) {
        thrustPressed = state;
    }

    public void setRotateLeft(boolean state) {
        rotateLeftPressed = state;
    }

    public void setRotateRight(boolean state) {
        rotateRightPressed = state;
    }

    public void setFiring(boolean state) {
        firePressed = state;
    }

    public void setFiringEnabled(boolean state) {
        firingEnabled = state;
    }

    /**
     * LEHET MAJD MEGIS KENE
     */
    /*
    public void reset() {
        rotation = defRotation;
        position.setXY(PlayPanel.worldSize / 2.0, PlayPanel.worldSize / 2.0);
        velocity.setXY(0.0, 0.0);
        bullets.clear();
    }
    */
    @Override
    public void update(GameFrame game) {
        super.update(game);

        //Increment the animation frame.
        animationFrame++;

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
            rotate(rotateLeftPressed ? -rotationSpeed : rotationSpeed);
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
            velocity.addVectorToVector(new DescartesVector(rotation).multiply_by_scalar(thrust));

            /*
             * Here we determine whether our ship is going faster than is
             * allowed. Like when checking for collisions, we check the squared
             * magnitude because it is quicker to square a value than it is to
             * take the square root.
             *
             * If our velocity exceeds our maximum allowed velocity, we normalize
             * it (giving it a magnitude of 1.0), and scale it to be he maximum.
             */
            if (velocity.vectorSizeSquared() >= acceleration * acceleration) {
                velocity.unitVector().multiply_by_scalar(acceleration);
            }
        }

        /*
         * If our ship is moving, slow it down slightly, which causes the ship
         * to some to a gradual stop.
         */

        if (velocity.vectorSizeSquared() != 0.0) {
            velocity.multiply_by_scalar(slowingForce);
        }

        /*
         * Loop through each bullet and remove it from the list if necessary.
         */
        bullets.removeIf(Thing::needsRemoval);

        /*
         * Decrement the fire and overheat cool-downs, and determine if we can fire another
         * bullet.
         */
        fireCooldown--;
        //overheatCooldown--;
        if (firingEnabled && firePressed && fireCooldown <= 0 /*&& overheatCooldown <= 0*/) {
            /*
             * We can only create a new bullet if we haven't yet exceeded the
             * maximum number of bullets that we can have fired at once.
             *
             * If a new bullet can be fired, we reset the fire cooldown, and
             * register a new bullet to the game world.
             */
            if (bullets.size() < bulletNumber) {
                fireCooldown = rateOfFire;

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
            consecutiveShots++;
            if (consecutiveShots == maxShots) {
                consecutiveShots = 0;
                //overheatCooldown = MAX_OVERHEAT;
            }
        } else if (consecutiveShots > 0) {
            //Decrement the number of consecutive shots, since we're not trying to fire.
            consecutiveShots--;
        }
    }

    @Override
    public void handleCollision(GameFrame game, Thing other) {
        //Kill the player if it collides with an Asteroid.
        if (other.getClass() == Asteroid.class) {
            game.destroyShip();
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

            int[] x = new int[]{-10, 10, -10};
            int[] y = new int[]{-8, 0, 8};

            Polygon ship = new Polygon(x, y, 3);

            g.draw(ship);
            g.setColor(Color.RED);
            g.fill(ship);
            /*
            g.drawLine(-10, -8, 10, 0);
            g.drawLine(-10, 8, 10, 0);
            g.drawLine(-6, -6, -6, 6);
            */
            //Draw the flames behind the ship if we're thrusting, and not paused.
            /*
            if (!game.isPaused() && thrustPressed && animationFrame % 6 < 3) {
                g.drawLine(-6, -6, -12, 0);
                g.drawLine(-6, 6, -12, 0);
            }
            */
        }
    }

}
