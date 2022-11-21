package game;

import java.awt.*;

public class Bullet extends Thing {

    private static final double acceleration = 5;

    //private static final int fireDistance = 60;

    private int lifespan;

    public Bullet(Thing owner, double angle) {
        super(new DescartesVector(owner.position), new DescartesVector(angle).multiply_by_scalar(acceleration), 2.0, 0);
        // sets the bullets 'length'
        lifespan = 60;
    }

    @Override
    public void update(GameFrame game) {
        super.update(game);

        //Decrement the lifespan of the bullet, and remove it if needed.
        lifespan--;
        if (lifespan <= 0) {
            mustRemove();
        }
    }

    @Override
    public void handleCollision(GameFrame game, Thing other) {
        if (other.getClass() != SpaceShip.class) {
            mustRemove();
        }
    }

    @Override
    public void draw(Graphics2D g, GameFrame game) {
        g.drawOval(-1, -1, 2, 2);
    }

}
