package game_members;

import game.*;

import java.awt.*;
import java.util.Random;

public class Asteroid extends Entity {

    private static final double MIN_ROTATION = 0.0075;

    private static final double MAX_ROTATION = 0.0175;

    private static final double ROTATION_VARIANCE = MAX_ROTATION - MIN_ROTATION;

    private static final double MIN_VELOCITY = 0.75;

    private static final double MAX_VELOCITY = 1.65;

    private static final double VELOCITY_VARIANCE = MAX_VELOCITY - MIN_VELOCITY;

    private static final double MIN_DISTANCE = 200.0;


    private static final double MAX_DISTANCE = PlayPanel.WORLD_SIZE / 2.0;

    private static final double DISTANCE_VARIANCE = MAX_DISTANCE - MIN_DISTANCE;

    private static final float SPAWN_UPDATES = 10;

    private final AsteroidSize size;

    private final double rotationSpeed;

    public Asteroid(Random random) {
        super(calculatePosition(random), calculateVelocity(random), AsteroidSize.Large.radius, AsteroidSize.Large.killValue);
        this.rotationSpeed = -MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
        this.size = AsteroidSize.Large;
    }

    public Asteroid(Asteroid parent, AsteroidSize size, Random random) {
        super(new DescartesVector(parent.position), calculateVelocity(random), size.radius, size.killValue);
        this.rotationSpeed = MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
        this.size = size;

        /*
         * While not necessary, calling the update method here makes the asteroid
         * appear to have a different starting position than it's parent or sibling.
         */
        for (int i = 0; i < SPAWN_UPDATES; i++) {
            update(null);
        }
    }

    private static DescartesVector calculatePosition(Random random) {
        DescartesVector vec = new DescartesVector(PlayPanel.WORLD_SIZE / 2.0, PlayPanel.WORLD_SIZE / 2.0);
        return vec.addVectorToVector(new DescartesVector(random.nextDouble() * Math.PI * 2).multiply_by_scalar(MIN_DISTANCE + random.nextDouble() * DISTANCE_VARIANCE));
    }

    private static DescartesVector calculateVelocity(Random random) {
        return new DescartesVector(random.nextDouble() * Math.PI * 2).multiply_by_scalar(MIN_VELOCITY + random.nextDouble() * VELOCITY_VARIANCE);
    }

    @Override
    public void update(GameFrame game) {
        super.update(game);
        rotate(rotationSpeed); //Rotate the image each frame.
    }

    @Override
    public void draw(Graphics2D g, GameFrame game) {
        g.drawPolygon(size.polygon); //Draw the Asteroid.
    }

    @Override
    public void handleCollision(GameFrame game, Entity other) {
        //Prevent collisions with other asteroids.
        if (other.getClass() != Asteroid.class) {
            //Only spawn "children" if we're not a Small asteroid.
            if (size != AsteroidSize.Small) {
                //Determine the Size of the children.
                AsteroidSize spawnSize = AsteroidSize.values()[size.ordinal() - 1];

                //Create the children Asteroids.
                for (int i = 0; i < 2; i++) {
                    game.registerEntity(new Asteroid(this, spawnSize, game.getRandom()));
                }
            }

            //Delete this Asteroid from the world.
            flagForRemoval();

            //Award the player points for killing the Asteroid.
            game.addScore(getKillScore());
        }
    }

}

