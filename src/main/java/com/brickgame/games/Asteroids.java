package com.brickgame.games;

import java.util.*;
import java.awt.event.*;
import static com.brickgame.Constants.*;
import com.brickgame.Client;
import com.brickgame.Constants.Direction;
import com.brickgame.block.*;

/**
 * An Asteroids game.
 * <p>
 * Read the <a href="{@docRoot}/docs/GameManuals.md">Game Manuals</a>.
 * 
 * @author  <a href="https://github.com/marcantonio64/">marcantonio64</a>
 * @version "%I%, %G%"
 * @since   1.8
 */
public class Asteroids extends GameEngine {
    private static final boolean USE_BOMBS = true;
    private static Random random = new Random();
    private static int shooterSpeed = 10;
    private int asteroidsSpeed;  // Falling speed.
    private int gameTimer;  // To scale the difficulty.
    private Shooter shooter;
    private Bomb bomb;

    /** Constructor for {@link Asteroids}. */
    public Asteroids() {
        super();
        // Setting containers for the entities.
        setEntities("asteroids", "bullet", "shooter", "bomb");
    }

    /** Defines game objects. */
    @Override
    public void start() {
        super.start();
        this.asteroidsSpeed = 2;
        this.speed = this.asteroidsSpeed;
        this.gameTimer = 0;
        // Spawning the entities.
        this.shooter = new Shooter();
        this.bomb = new Bomb();
    }
    
    /**
     * Deals with user input during the game.
     * 
     * @param key     A {@link KeyEvent} identifier for a key.
     * @param pressed Whether {@code key} was pressed or released.
     */
    @Override
    public void setKeyBindings(int key, boolean pressed) {
        super.setKeyBindings(key, pressed);
        if (running) {
            if (pressed) {  // Key pressed.
                // Set Shooter movement.
                if (key == KeyEvent.VK_LEFT) {
                    shooter.setDirection(Direction.LEFT);
                } else if (key == KeyEvent.VK_RIGHT) {
                    shooter.setDirection(Direction.RIGHT);
                }
            } else {  // Key released.
                if (key == KeyEvent.VK_LEFT) {
                    shooter.setDirection(null);
                } else if (key == KeyEvent.VK_RIGHT) {
                    shooter.setDirection(null);
                }
            }
        }
    }

    /**
     * Game logic implementation.
     * 
     * @param t A timer.
     */
    @Override
    public void manage(int t) {
        if (running && !paused) {
            /* Using a custom timer to allow for proper scaling of
             * difficulty. */
            gameTimer += 1;

            // Setting an action rate of FPS Blocks per second.
            for (int i = 0; i <= 60/FPS; i++) {
                shooter.moveBullets();
            }

            // Managing multiple simultaneous hits and scoring.
            int numberOfCollisions = checkHit();
            updateScore(numberOfCollisions);

            // Setting an action rate of asteroidsSpeed Blocks per second.
            if (t % (FPS/asteroidsSpeed) == 0) {
                moveAsteroids(gameTimer);
                bomb.move(Direction.UP);
                bomb.checkExplosion(entities.get("asteroids"));
            }

            // Setting an action rate of shooterSpeed Blocks per second.
            if (t % (FPS/shooterSpeed) == 0) {
                // The Bullet's movement is handled by its update method.
                shooter.shoot();
                shooter.move();
                trySpawnBomb(gameTimer);
            }
        }
        // Managing endgame.
        super.manage(t);
    }

    /**
     * Bullets disappear and destroy asteroids upon collision.
     * 
     * @return Number of asteroid Blocks destroyed.
     */
    private int checkHit() {
        Iterator<Block> bullets = entities.get("bullet").iterator();
        List<Block> asteroids = entities.get("asteroids");
        List<Block> collisions = new ArrayList<>();
        List<Block> newCollisions;
        while (bullets.hasNext()) {
            Block bullet = bullets.next();
            int i = bullet.getPosition().get(0);
            int j = bullet.getPosition().get(1);
            newCollisions = new ArrayList<>();
            for (Block block : asteroids) {
                // A collision happens if the coordinates coincide.
                if ((block.getPosition().get(0) == i)
                        && ((block.getPosition().get(1) == j
                            || block.getPosition().get(1) == j+1))) {
                    newCollisions.add(block);
                }
            }
            if (!newCollisions.isEmpty()) {
                // Destroying the bullet.
                bullet.hide();
                shooter.bullets.remove(bullet);
                bullets.remove();
            }
            collisions.addAll(newCollisions);
        }
        for (Block block : collisions) {
            // Destroying the asteroids.
            block.hide();
            entities.get("asteroids").remove(block);
        }
        return collisions.size();
    }

    /** Scoring mechanics. */
    void updateScore(int blocksHit) {
        score += 5*blocksHit;
        super.updateScore();
    }

    /**
     * Handles the {@link Bomb}'s spawn over time according to its
     * spawn rate.
     * 
     * @param t A timer.
     */
    private void trySpawnBomb(int t) {
        /* The chance of a Bomb spawning increases from 0.1% up to
         * 0.15% after 3 minutes. */
        double spawnRate = 1.0/3000;
        if (t <= 180*FPS && t % (60*FPS) == 0) {
            spawnRate += 1.0/6000;
        }
        boolean spawning = random.nextDouble() < spawnRate;
        if (USE_BOMBS && spawning) {
            int i = random.nextInt(7);
            this.bomb = new Bomb(i, 19, entities.get("bomb"));
        }
    }

    /**
     * The game is endless, except for defeat.
     * 
     * @return Whether the game has been beaten.
     */
    @Override
    public boolean checkVictory() {
        return false;
    }

    /**
     * Defeat condition.
     * <p>
     * Occurs when an asteroid {@link Block} reaches either the
     * {@link #shooter} or the lower border of the grid.
     * 
     * @return Whether the game was lost.
     */
    @Override
    public boolean checkDefeat() {
        Block shooter = entities.get("shooter").get(0);
        List<Block> asteroids = entities.get("asteroids");
        List<Integer> heights = new ArrayList<>(asteroids.size());
        heights.add(1);
        // Checking for collisions with the shooter.
        List<Block> collision = new ArrayList<>();
        for (Block asteroid : asteroids) {
            if (shooter.getPosition().get(0) == asteroid.getPosition().get(0)
                    && shooter.getPosition().get(1) == asteroid.getPosition().get(1)) {
                collision.add(asteroid);
            }
            heights.add(asteroid.getPosition().get(1));
        }
        // Tracking the asteroids' height.
        int height = Collections.max(heights);

        return !collision.isEmpty() || height >= 20;  //  Hitting the bottom.
    }

    /**
     * Organizes the asteroids' display and movement.
     * 
     * @param t A timer.
     */
    private void moveAsteroids(int t) {
        if (!entities.get("asteroids").isEmpty()) {
            for (Block asteroid : entities.get("asteroids")) {
                asteroid.setPosition(asteroid.getPosition().get(0),
                                    asteroid.getPosition().get(1)+1);
            }
        }
        /* Spawn rate starts at 0.3 per tick, increasing linearly up to
         * 0.45 per tick after 3 minutes. (>=0.5 is unbeatable.) */
        double r;
        if (t < 180*FPS) {
            r = 0.3 + t*0.15/(180*FPS);
        } else {
            r = 0.45;
        }
        for (int i = 0; i < 10; i++) {
            if (random.nextDouble() < r) {
                entities.get("asteroids").add(new Block(i, 0));
            }
        }
    }

    private class Bullet extends Block {
        /** A {@link Block} moving up. */
        Bullet(int[] coords) {
            super(coords);
            this.setDirection(Direction.UP);
            entities.get("bullet").add(this);
        }
    }

    /**
     * Manages the player-controlled shooter.
     * <p>
     * A {@link Block} moving horizontally at the bottom of the grid
     * that can shoot {@link Bullets}.
     */
    private class Shooter extends Block {
        List<Bullet> bullets;
        /** Sets {@link Shooter}'s initial position */
        Shooter() {
            super(4, 19);
            ArrayList<Block> list = new ArrayList<>();
            list.add(this);
            entities.put("shooter", list);
            this.bullets = new ArrayList<>();
        }

        /** Avoids the {@link Shooter shooter} from leaving the grid. */
        @Override
        public void move() {
            int i = getPosition().get(0);
            int a = CONVERT.get(getDirection())[0];
            if (0 <= i + a && i + a < 10) {
                super.move();
            }
        }

        /** Spawns a {@link Bullet}. */
        void shoot() {
            bullets.add(new Bullet(new int[] {getPosition().get(0),
                                              getPosition().get(1)-1}));
        }

        void moveBullets() {
            Iterator<Bullet> bulletsIterator = bullets.iterator();
            while (bulletsIterator.hasNext()) {
                Bullet bullet = bulletsIterator.next();
                bullet.move();
                // Making the bullet disappear if it hits the top border.
                if (bullet.getPosition().get(1) < 0) {
                    bullet.hide();
                    entities.get("bullet").remove(bullet);
                    bulletsIterator.remove();
                }
            }
        }
    }

    public static void main(String[] args) {
        class AsteroidsClient extends Client {
            Asteroids game;
            AsteroidsClient() {
                super();  // setup() and setLoop() are invoked here.
                window.setTitle("Asteroids");
            }
            /** Initializes the game. */
            @Override
            public void setup() {
                super.setup();
                this.game = new Asteroids();
                this.game.start();
            }
            /** List of scheduled events. */
            @Override
            public void setLoop() {
                super.setLoop();
                // Implementing the game mechanics and check for endgame.
                game.manage(ticks);
                // Drawing the game objects to the screen.
                game.drawEntities();
                // Updating BlinkingBlocks mechanics.
                game.update(ticks);

                canvas.repaint();
            }

            /**
             * Deals with user input during a game.
             * 
             * @param key     A {@link KeyEvent} identifier for a key.
             * @param pressed Whether {@code key} was pressed or released.
             */
            @Override
            public void setKeyBindings(int key, boolean pressed) {
                super.setKeyBindings(key, pressed);
                game.setKeyBindings(key, pressed);
            }
        }

        System.out.println("Check docs/GameManuals.md for instructions.");
        new AsteroidsClient();
    }
}
