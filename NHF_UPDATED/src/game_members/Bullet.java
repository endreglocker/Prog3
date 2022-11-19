package game_members;

import game.*;

import java.awt.*;

public class Bullet extends Entity {

    private static final double VELOCITY_MAGNITUDE = 6.75;

    private static final int MAX_LIFESPAN = 60;

    private int lifespan;

    public Bullet(Entity owner, double angle) {
        super(new DescartesVector(owner.position), new DescartesVector(angle).multiply_by_scalar(VELOCITY_MAGNITUDE), 2.0, 0);
        this.lifespan = MAX_LIFESPAN;
    }

    @Override
    public void update(GameFrame game) {
        super.update(game);

        //Decrement the lifespan of the bullet, and remove it if needed.
        this.lifespan--;
        if (lifespan <= 0) {
            flagForRemoval();
        }
    }

    @Override
    public void handleCollision(GameFrame game, Entity other) {
        if (other.getClass() != Player.class) {
            flagForRemoval();
        }
    }

    @Override
    public void draw(Graphics2D g, GameFrame game) {
        g.drawOval(-1, -1, 2, 2);
    }

}
