package game;

import java.awt.*;
import java.util.Random;

public class Asteroid extends Thing {

    //private static final double minRotation = 0.0075;

    //private static final double maxRotation = 0.0175;

    //private static final double deltaRotation = maxRotation - minRotation;

    private static final double minVelocity = 0.75;

    private static final double maxVelocity = 1.65;

    private static final double deltaVelocity = maxVelocity - minVelocity;

    private static final double minDistance = 200.0;

    private static final double maxDistance = PlayPanel.worldSize / 2.0;

    private static final double deltaDistance = maxDistance - minDistance;

    //private static final float spawn = 10;

    private final AsteroidSize asteroidSize;

    //private final double rotationSpeed;

    public Asteroid(Random random) {
        super(calculatePosition(random), calculateVelocity(random), AsteroidSize.Large.radius, AsteroidSize.Large.scoreForKilling);
        //rotationSpeed = -minRotation + (random.nextDouble() * deltaRotation);
        asteroidSize = AsteroidSize.Large;
    }

    public Asteroid(Asteroid parent, AsteroidSize size, Random random) {
        super(new DescartesVector(parent.position), calculateVelocity(random), size.radius, size.scoreForKilling);
        //rotationSpeed = minRotation + (random.nextDouble() * deltaRotation);
        asteroidSize = size;
        /// !!!!!!!!! lehet mashogy jelennek meg az asteroidak
        /*
        for (int i = 0; i < spawn; i++) {
            update(null);
        }

         */
    }

    private static DescartesVector calculatePosition(Random random) {
        DescartesVector vec = new DescartesVector(maxDistance / 2.0, maxDistance / 2.0);
        return vec.addVectorToVector(new DescartesVector(random.nextDouble() * Math.PI * 2).multiply_by_scalar(minDistance + random.nextDouble() * deltaDistance));
    }

    private static DescartesVector calculateVelocity(Random random) {
        return new DescartesVector(random.nextDouble() * Math.PI * 2).multiply_by_scalar(minVelocity + random.nextDouble() * deltaVelocity);
    }

    @Override
    public void update(GameFrame game) {
        super.update(game);
        rotate(0); //Rotate the image each frame.
    }

    @Override
    public void draw(Graphics2D g, GameFrame game) {
        g.drawPolygon(asteroidSize.polygon); //Draw the Asteroid.
        g.setColor(Color.RED);
        g.fill(asteroidSize.polygon);
    }

    @Override
    public void handleCollision(GameFrame game, Thing other) {
        //Prevent collisions with other asteroids.
        if (other.getClass() != Asteroid.class) {
            //Only spawn "children" if we're not a Small asteroid.
            if (asteroidSize != AsteroidSize.Small) {
                //Determine the Size of the children.
                AsteroidSize spawnSize = AsteroidSize.values()[asteroidSize.ordinal() - 1];

                //Create the children Asteroids.
                for (int i = 0; i < 2; i++) {
                    game.registerEntity(new Asteroid(this, spawnSize, game.getRandom()));
                }
            }

            //Delete this Asteroid from the world.
            mustRemove();

            //Award the player points for killing the Asteroid.
            game.addScore(getScoredPoints());
        }
    }

}

