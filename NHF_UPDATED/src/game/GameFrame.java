package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class GameFrame extends JFrame {

    private static final int speed = 60;

    private static final long frame = (long) (1000000000.0 / speed);

    //private static final int displayLvL = 60;

    //private static final int displayCoolDown = 200;

    private static final int respawnCoolDown = 100;

    private static final int invulnerableCoolDown = 0;

    private static final int resetCoolDown = 120;

    private final PlayPanel world;

    private Time timer;

    private Random random;

    private List<Thing> thingList;

    private List<Thing> thingsInQueue;

    private SpaceShip ship;

    private int deathCooldown;

    //private int showLevelCooldown;

    private int restartCooldown;

    private int score;

    //private int lives;

    //private boolean alive;

    //private int level;

    private boolean isGameOver;

    private boolean restartGame;

    public GameFrame() {
        //Initialize the window's basic properties.
        super("Asteroids");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        //Create and add the WorldPanel instance to the window.
        add(world = new PlayPanel(this), BorderLayout.CENTER);

        controller();

        //Resize the window to the correct size, position it in the center of the screen, and display it.
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void controller() {

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //Determine which key was pressed.
                switch (e.getKeyCode()) {

                    //Indicate that we want to apply thrust to our ship.
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (!checkForRestart()) {
                            ship.setThrusting(true);
                        }
                        break;

                    //Indicate that we want to rotate our ship to the left.
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (!checkForRestart()) {
                            ship.setRotateLeft(true);
                        }
                        break;

                    //Indicate that we want to rotate our ship to the right.
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (!checkForRestart()) {
                            ship.setRotateRight(true);
                        }
                        break;

                    //Indicate that we want our ship to fire bullets.
                    case KeyEvent.VK_SPACE:
                        if (!checkForRestart()) {
                            ship.setFiring(true);
                        }
                        break;

                    //Indicate that we want to pause the game.
                    case KeyEvent.VK_P:
                        if (!checkForRestart()) {
                            timer.setPaused(!timer.isPaused());
                        }
                        break;

                    //Handle all other key presses.
                    default:
                        checkForRestart();
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {

                    //Indicate that we no long want to apply thrust to the ship.
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> ship.setThrusting(false);


                    //Indicate that we no longer want to rotate our ship left.
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> ship.setRotateLeft(false);


                    //Indicate that we no longer want to rotate our ship right.
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> ship.setRotateRight(false);


                    //Indicate that we no long want to fire bullets.
                    case KeyEvent.VK_SPACE -> ship.setFiring(false);
                }
            }
        });
    }

    private boolean checkForRestart() {
        boolean restart = (isGameOver && restartCooldown <= 0);
        if (restart) {
            restartGame = true;
        }
        return restart;
    }


    public void startGame() {
        //Initialize the engine's variables.
        random = new Random();
        thingList = new LinkedList<>();
        thingsInQueue = new ArrayList<>();
        ship = new SpaceShip();

        //Set the variables to their default values.
        resetGame();

        //Create the logic timer and enter the game loop.
        timer = new Time(speed);
        while (true) {
            //Get the time that the frame started.
            long start = System.nanoTime();

            /*
             * Update the game once for every cycle that has elapsed. If the game
             * starts to fall behind, the game will update multiple times for each
             * frame that is rendered in order to catch up.
             */
            timer.update();
            /*
            for (int i = 0; i < 5 && timer.hasElapsedCycle(); i++) {
                updateGame();
            }
            */
            updateGame();
            //Repaint the window.
            world.repaint();

            /*
             * Determine how many nanoseconds we have left during this cycle,
             * and sleep until it is time for the next frame to start.
             */
            long delta = frame - (System.nanoTime() - start);
            if (delta > 0) {
                try {
                    Thread.sleep(delta / 1000000L, (int) delta % 1000000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateGame() {
        /*
         * Here we add any pending entities to the world.
         *
         * Two lists are required because we will frequently add entities to the
         * world while we are iterating over them, which causes all sorts of
         * errors.
         */
        thingList.addAll(thingsInQueue);
        thingsInQueue.clear();

        /*
         * Decrement the restart cooldown.
         */


        if (restartCooldown > 0) {
            restartCooldown--;
        }

        /*
         * Decrement the show level cooldown.
         */
        /*
        if (showLevelCooldown > 0) {
            showLevelCooldown--;
        }
        */
        /*
         * Restart the game if needed.
         */
        if (isGameOver) {
            clearThingList();
        }

        if (isGameOver && restartGame) {
            resetGame();
        }

        /*
         * If the game is currently in progress, and there are no enemies left alive,
         * we prepare the next level.
         */
        if (!isGameOver && destroyedAsteroids()) {
            /*
            //Increment the current level, and set the show level cooldown.
            level++;
            showLevelCooldown = displayLvL;

            //Reset the entity lists (to remove bullets).
            resetThingList();
            */
            //Reset the player's entity to it is default state, and re-enable firing.
            //ship.reset();
            ship.setFiringEnabled(true);

            //Add the asteroids to the world.
            int randomnumberAsteroids = 3 + (int) (Math.random() % (10 - 3));
            for (int i = 0; i < randomnumberAsteroids; i++) {
                registerEntity(new Asteroid(random));
            }
        }
        /*
        if (deathCooldown > 0) {
            deathCooldown--;
            switch (deathCooldown) {
                case respawnCoolDown -> {
                    ship.reset();
                    ship.setFiringEnabled(false);
                }

                case invulnerableCoolDown -> ship.setFiringEnabled(true);
            }
        }*/

        /// VOLT ITT EGY IF AMI AZT CSINANLTA, HOGY CSAK AKKOR UPDATEL HA NINCS LVL UP SCREEN
        for (Thing thing : thingList) {
            thing.update(this);
        }

        for (int i = 0; i < thingList.size(); i++) {
            Thing a = thingList.get(i);
            for (int j = i + 1; j < thingList.size(); j++) {
                Thing b = thingList.get(j);
                if (i != j && a.collisionDetector(b) && ((a != ship && b != ship) || deathCooldown <= invulnerableCoolDown)) {
                    a.handleCollision(this, b);
                    b.handleCollision(this, a);
                }
            }
        }

        //Loop through and remove "dead" entities.
        thingList.removeIf(Thing::needsRemoval);
    }

    private void resetGame() {
        score = 0;
        //level = 0;
        //lives = 1;
        //alive = true;
        deathCooldown = 0;
        isGameOver = false;
        restartGame = false;
        clearThingList();
    }

    private void clearThingList() {
        thingsInQueue.clear();
        thingList.clear();
        thingList.add(ship);
    }

    private boolean destroyedAsteroids() {
        for (Thing e : thingList) {
            if (e.getClass() == Asteroid.class) {
                return false;
            }
        }
        return true;
    }

    public void destroyShip() {
        //Decrement the number of lives that we still have.
        //lives--;
        //alive = false;
        /*
         * If there are no lives remaining, prepare the game over state variables,
         * otherwise prepare the death cooldown.
         *
         * Note that death cooldown is set to Integer.MAX_VALUE in the event of a
         * game over. While finite, the amount of time it would take for it to
         * reach zero is far longer than anyone would care to run the program
         * for.
         */
        /*if (!alive) {*/
            isGameOver = true;
            restartCooldown = resetCoolDown;
            deathCooldown = Integer.MAX_VALUE;
        /*} else {
            deathCooldown = displayCoolDown;
        }*/

        //Disable the ability to fire.
        ship.setFiringEnabled(false);
    }

    public void addScore(int points) {
        score += points;
    }

    public void registerEntity(Thing thing) {
        thingsInQueue.add(thing);
    }


    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isPlayerInvulnerable() {
        return (deathCooldown > invulnerableCoolDown);
    }

    public boolean canDrawPlayer() {
        return (deathCooldown <= respawnCoolDown);
    }

    public int getScore() {
        return score;
    }

    /*
    public int getLevel() {
        return level;
    }
    */
    public boolean isPaused() {
        return timer.isPaused();
    }

    /*
    public boolean isShowingLevel() {
        return (showLevelCooldown > 0);
    }
    */
    public Random getRandom() {
        return random;
    }

    public List<Thing> getThingList() {
        return thingList;
    }

    public SpaceShip getShip() {
        return ship;
    }

}

