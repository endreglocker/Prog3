package game;

import game_members.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class GameFrame extends JFrame {

    private static final int FRAMES_PER_SECOND = 60;

    private static final long FRAME_TIME = (long) (1000000000.0 / FRAMES_PER_SECOND);

    private static final int DISPLAY_LEVEL_LIMIT = 60;

    private static final int DEATH_COOLDOWN_LIMIT = 200;

    private static final int RESPAWN_COOLDOWN_LIMIT = 100;

    private static final int INVULN_COOLDOWN_LIMIT = 0;

    private static final int RESET_COOLDOWN_LIMIT = 120;

    private final PlayPanel world;

    private Time logicTimer;

    private Random random;

    private List<Entity> entities;

    private List<Entity> pendingEntities;

    private Player player;

    private int deathCooldown;

    private int showLevelCooldown;

    private int restartCooldown;

    private int score;

    private int lives;

    private int level;

    private boolean isGameOver;

    private boolean restartGame;

    public GameFrame() {
        //Initialize the window's basic properties.
        super("Asteroids");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        //Create and add the WorldPanel instance to the window.
        add(this.world = new PlayPanel(this), BorderLayout.CENTER);

        /*
         * Here we add a key listener to the window so that we can process incoming
         * user input.
         *
         * Because the player is updated every cycle, rather than when it receives
         * input (like I did for Tetris), we're only going to set a flag to indicate
         * the current input state. The actual change in the player's entity's state
         * will be handled in the game loop.
         *
         * The reason we do this is simple. Events are only fired when input is received
         * from the user. While the keyPressed event is continuously fired, it isn't
         * necessarily going to be in sync with our main thread, which would cause
         * all sorts of unpredictable behavior from our ship.
         *
         * Note that any "pressed" event will restart the game rather than change the
         * ship's state if the conditions are met.
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //Determine which key was pressed.
                switch (e.getKeyCode()) {

                    //Indicate that we want to apply thrust to our ship.
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (!checkForRestart()) {
                            player.setThrusting(true);
                        }
                        break;

                    //Indicate that we want to rotate our ship to the left.
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (!checkForRestart()) {
                            player.setRotateLeft(true);
                        }
                        break;

                    //Indicate that we want to rotate our ship to the right.
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (!checkForRestart()) {
                            player.setRotateRight(true);
                        }
                        break;

                    //Indicate that we want our ship to fire bullets.
                    case KeyEvent.VK_SPACE:
                        if (!checkForRestart()) {
                            player.setFiring(true);
                        }
                        break;

                    //Indicate that we want to pause the game.
                    case KeyEvent.VK_P:
                        if (!checkForRestart()) {
                            logicTimer.setPaused(!logicTimer.isPaused());
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
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> player.setThrusting(false);


                    //Indicate that we no longer want to rotate our ship left.
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.setRotateLeft(false);


                    //Indicate that we no longer want to rotate our ship right.
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.setRotateRight(false);


                    //Indicate that we no long want to fire bullets.
                    case KeyEvent.VK_SPACE -> player.setFiring(false);
                }
            }
        });

        //Resize the window to the correct size, position it in the center of the screen, and display it.
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
        this.random = new Random();
        this.entities = new LinkedList<>();
        this.pendingEntities = new ArrayList<>();
        this.player = new Player();

        //Set the variables to their default values.
        resetGame();

        //Create the logic timer and enter the game loop.
        this.logicTimer = new Time(FRAMES_PER_SECOND);
        while (true) {
            //Get the time that the frame started.
            long start = System.nanoTime();

            /*
             * Update the game once for every cycle that has elapsed. If the game
             * starts to fall behind, the game will update multiple times for each
             * frame that is rendered in order to catch up.
             */
            logicTimer.update();
            for (int i = 0; i < 5 && logicTimer.hasElapsedCycle(); i++) {
                updateGame();
            }

            //Repaint the window.
            world.repaint();

            /*
             * Determine how many nanoseconds we have left during this cycle,
             * and sleep until it is time for the next frame to start.
             */
            long delta = FRAME_TIME - (System.nanoTime() - start);
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
        entities.addAll(pendingEntities);
        pendingEntities.clear();

        /*
         * Decrement the restart cooldown.
         */
        if (restartCooldown > 0) {
            this.restartCooldown--;
        }

        /*
         * Decrement the show level cooldown.
         */
        if (showLevelCooldown > 0) {
            this.showLevelCooldown--;
        }

        /*
         * Restart the game if needed.
         */
        if (isGameOver && restartGame) {
            resetGame();
        }

        /*
         * If the game is currently in progress, and there are no enemies left alive,
         * we prepare the next level.
         */
        if (!isGameOver && areEnemiesDead()) {
            //Increment the current level, and set the show level cooldown.
            this.level++;
            this.showLevelCooldown = DISPLAY_LEVEL_LIMIT;

            //Reset the entity lists (to remove bullets).
            resetEntityLists();

            //Reset the player's entity to it is default state, and re-enable firing.
            player.reset();
            player.setFiringEnabled(true);

            //Add the asteroids to the world.
            for (int i = 0; i < level + 2; i++) {
                registerEntity(new Asteroid(random));
            }
        }

        /*
         * If the player has recently died, decrement the cooldown and handle any
         * special cases when they occur.
         */
        if (deathCooldown > 0) {
            this.deathCooldown--;
            switch (deathCooldown) {

                //Reset the entity to it is default spawn state, and disable firing.
                case RESPAWN_COOLDOWN_LIMIT -> {
                    player.reset();
                    player.setFiringEnabled(false);
                }

                //Re-enable the ability to fire, as we're no longer invulnerable.
                case INVULN_COOLDOWN_LIMIT -> player.setFiringEnabled(true);
            }
        }

        /*
         * Only run any of the update code if we're not currently displaying the
         * level to the player.
         */
        if (showLevelCooldown == 0) {

            //Iterate through the Entities and update their states.
            for (Entity entity : entities) {
                entity.update(this);
            }

            /*
             * Handle any collisions that take place.
             *
             * The outer loop iterates through all registered entities, while the
             * inner loop only iterates through the Entities later in the list
             * than the outer Entity.
             *
             * This ensures that the same collision isn't handled multiple times,
             * which allows us to make changes to an entity without it interfering
             * with other collision results.
             */
            for (int i = 0; i < entities.size(); i++) {
                Entity a = entities.get(i);
                for (int j = i + 1; j < entities.size(); j++) {
                    Entity b = entities.get(j);
                    if (i != j && a.checkCollision(b) && ((a != player && b != player) || deathCooldown <= INVULN_COOLDOWN_LIMIT)) {
                        a.handleCollision(this, b);
                        b.handleCollision(this, a);
                    }
                }
            }

            //Loop through and remove "dead" entities.
            entities.removeIf(Entity::needsRemoval);
        }
    }

    private void resetGame() {
        this.score = 0;
        this.level = 0;
        this.lives = 3;
        this.deathCooldown = 0;
        this.isGameOver = false;
        this.restartGame = false;
        resetEntityLists();
    }

    private void resetEntityLists() {
        pendingEntities.clear();
        entities.clear();
        entities.add(player);
    }

    private boolean areEnemiesDead() {
        for (Entity e : entities) {
            if (e.getClass() == Asteroid.class) {
                return false;
            }
        }
        return true;
    }


    public void killPlayer() {
        //Decrement the number of lives that we still have.
        this.lives--;

        /*
         * If there are no lives remaining, prepare the game over state variables,
         * otherwise prepare the death cooldown.
         *
         * Note that death cooldown is set to Integer.MAX_VALUE in the event of a
         * game over. While finite, the amount of time it would take for it to
         * reach zero is far longer than anyone would care to run the program
         * for.
         */
        if (lives == 0) {
            this.isGameOver = true;
            this.restartCooldown = RESET_COOLDOWN_LIMIT;
            this.deathCooldown = Integer.MAX_VALUE;
        } else {
            this.deathCooldown = DEATH_COOLDOWN_LIMIT;
        }

        //Disable the ability to fire.
        player.setFiringEnabled(false);
    }


    public void addScore(int score) {
        this.score += score;
    }

    public void registerEntity(Entity entity) {
        pendingEntities.add(entity);
    }


    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isPlayerInvulnerable() {
        return (deathCooldown > INVULN_COOLDOWN_LIMIT);
    }

    public boolean canDrawPlayer() {
        return (deathCooldown <= 100/*RESPAWN_COOLDOWN_LIMIT*/);
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getLevel() {
        return level;
    }

    public boolean isPaused() {
        return logicTimer.isPaused();
    }

    public boolean isShowingLevel() {
        return (showLevelCooldown > 0);
    }

    public Random getRandom() {
        return random;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }

}

