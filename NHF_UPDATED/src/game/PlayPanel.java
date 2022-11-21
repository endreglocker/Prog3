package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class PlayPanel extends JPanel {
    static final int worldSize = 600;

    private static final Font title = new Font("Dialog", Font.PLAIN, 25);

    private final GameFrame game;

    public PlayPanel(GameFrame g) {
        game = g;

        //Set the window's size and background color.
        setPreferredSize(new Dimension(worldSize, worldSize));
        setBackground(Color.BLACK);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //Required, otherwise rendering gets messy.

        /*
         * Cast our Graphics object to a Graphics2D object to make use of the extra capabilities
         * such as anti-aliasing, and transformations.
         */
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(Color.red); //Set the draw color to white.

        //Grab a reference to the current "identity" transformation, so we can reset for each object.
        AffineTransform identity = g2d.getTransform();

        /*
         * Loop through each entity and draw it onto the window.
         */
        for (Thing thing : game.getThingList()) {
            /*
             * We should only draw the player if it is not dead, so we need to
             * ensure that the entity can be rendered.
             */
            if (thing != game.getShip() || game.canDrawPlayer()) {
                DescartesVector pos = thing.getPosition(); //Get the position of the entity.

                //Draw the entity at it is actual position, and reset the transformation.
                drawThing(g2d, thing, pos.getX(), pos.getY());
                g2d.setTransform(identity);


                double radius = thing.getCollisionRadius();
                double x = pos.getX();
                if (pos.getX() < radius) {
                    x = pos.getX() + worldSize;
                } else if (pos.getX() > worldSize - radius) {
                    x = pos.getX() - worldSize;
                }

                double y = pos.getY();
                if (pos.getY() < radius) {
                    y = pos.getY() + worldSize;
                } else if (pos.getX() > worldSize - radius) {
                    y = pos.getY() - worldSize;
                }

                //Draw the entity at it's wrapped position, and reset the transformation.
                if (x != pos.getX() || y != pos.getY()) {
                    drawThing(g2d, thing, x, y);
                    g2d.setTransform(identity);
                }
            }
        }

        //Draw the score string in the top left corner if we are still playing.
        if (!game.isGameOver()) {
            g.drawString("Score: " + game.getScore(), 10, 15);
        }

        //Draw some overlay text depending on the game state.
        if (game.isGameOver()) {
            drawTextCentered("Game Over", g2d, -25);
            drawTextCentered("Final Score: " + game.getScore(), g2d, 10);
        } /*else if (game.isPaused()) { // NOT YET FINISHED
            drawTextCentered("Paused", g2d, -25);
        }*/
    }

    private void drawTextCentered(String text, Graphics2D g, int y) {
        g.setFont(title);
        g.drawString(text, worldSize / 2 - g.getFontMetrics().stringWidth(text) / 2, worldSize / 2 + y);
    }

    private void drawThing(Graphics2D g2d, Thing thing, double x, double y) {
        g2d.translate(x, y);
        double rotation = thing.getRotation();
        if (rotation != 0.0f) {
            g2d.rotate(thing.getRotation());
        }
        thing.draw(g2d, game);
    }

}
