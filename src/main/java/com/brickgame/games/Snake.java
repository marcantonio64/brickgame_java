package com.brickgame.games;

import java.util.*;
import java.awt.event.*;
import static com.brickgame.Constants.*;
import com.brickgame.screens.Sprite;
import static com.brickgame.screens.Sprite.createPosition;
import com.brickgame.Client;
import com.brickgame.Constants.Direction;
import com.brickgame.block.*;

/**
 * A Snake game.
 * <p>
 * Read the <a href="{@docRoot}/docs/GameManuals.md">Game Manuals</a>.
 * 
 * @author  <a href="https://github.com/marcantonio64/">marcantonio64</a>
 * @version "%I%, %G%"
 * @since   1.8
 */
public class Snake extends GameEngine {
    //private static Map<String, List<Block>> entities = new HashMap<>();
    private static Random random = new Random();
    private static Direction direction = Direction.DOWN;
    private static boolean growing = false;
    private static int startSpeed = 10;
    private static int[] speedValues = new int[2];
    /** Used to allow only one directional movement at a time. */
    private boolean keyEnabled;
    private Body snake;
    private Food food;

    /** Constructor for {@link Snake}. */
    public Snake() {
        super();
        // Setting containers for the entities.
        setEntities("body", "food");
    }

    /** Defines game objects. */
    @Override
    public void start() {
        super.start();
        direction = Direction.DOWN;
        growing = false;
        speedValues[0] = startSpeed;
        speedValues[1] = 2*speedValues[0];
        this.speed = speedValues[0];
        this.keyEnabled = false;
        // Spawning the entities.
        this.snake = new Body();
        this.food = new Food();
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
                if (key == KeyEvent.VK_SPACE) {
                    speed = speedValues[1];
                } else if (keyEnabled) {
                    /* Locking direction changes after the first one,
                     * until the next iteration. */
                    keyEnabled = false;
                    /* Direction changes, making sure the snake's head
                     * will not collide with itself. */
                    if (key == KeyEvent.VK_UP && direction != Direction.DOWN) {
                        direction = Direction.UP;
                    } else if (key == KeyEvent.VK_DOWN && direction != Direction.UP) {
                        direction = Direction.DOWN;
                    } else if (key == KeyEvent.VK_LEFT && direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    } else if (key == KeyEvent.VK_RIGHT && direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                }
            } else {  // Key released.
                if (key == KeyEvent.VK_SPACE) {
                    speed = speedValues[0];
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
            checkEat();
            // Setting the action rate at speed Blocks per second.
            if (t % (FPS/(speed % FPS)) == 0) {
                updateScore();
                snake.move();
                keyEnabled = true;

                // Dealing with speed values greater than FPS.
                for (int i = 0; i < speed/FPS; i++) {
                    manage(t);
                }
            }
        }
        // Managing endgame.
        super.manage(t);
    }

    /** The snake grows when it reaches the {@link #food food}. */
    private void checkEat() {
        if (snake.head.getPosition().equals(food.getPosition())) {
            growing = true;
            food.respawn();
            // Avoids the food spawning inside the snake.
            while (snake.coords.stream().anyMatch(coordinates ->
                    coordinates.equals(food.getPosition()))) {
                food.respawn();
            }
        }
    }

    /** Scoring mechanics. */
    @Override
    void updateScore() {
        int n = entities.get("body").size();
        if (growing) {
            if (3 < n && n <= 25) {
                score += 15;
            } else if (25 < n && n <= 50) {
                score += 45;
            } else if (50 < n && n <= 100) {
                score += 100;
            } else if (100 < n && n < 200) {
                score += 250;
            }
        }
        super.updateScore();
    }

    /**
     * Victory occurs when the snake's size becomes that of the whole
     * grid.
     * 
     * @return Whether the game was beaten.
     */
    @Override
    protected boolean checkVictory() {
        return entities.get("body").size() == 200;
    }

    /**
     * Defeat happens if the snake's {@link Body#head head} hits the
     * rest of its own body or the borders.
     * 
     * @return Whether the game was lost.
     */
    @Override
    protected boolean checkDefeat() {
        int i = snake.head.getPosition().get(0);
        int j = snake.head.getPosition().get(1);
        int n = snake.coords.size();
        if (i < 0 || i >= 10 || j < 0 || j >= 20) {
            return true;
        }
        if (snake.coords.subList(0, n-1).stream().anyMatch(coordinates ->
                coordinates.equals(snake.head.getPosition()))) {
            return true;
        }
        return false;
    }

    /** Organizes the snake's drawing, movement, and growth. */
    private class Body {
        List<ArrayList<Integer>> coords;
        Block head;
        Block tail;
        Body() {
            // Setting the snake's initial position.
            this.coords = new ArrayList<>(3);
            this.coords.add(createPosition(4, 3));
            this.coords.add(createPosition(4, 4));
            this.coords.add(createPosition(4, 5));
            // Adding Blocks to the container for drawing.
            for (ArrayList<Integer> coordinates : this.coords) {
                entities.get("body").add(new Block(coordinates));
            }
            // Identifying the head and the tail.
            this.head = entities.get("body").get(coords.size()-1);
            this.tail = entities.get("body").get(0);
        }

        /** Handles the snake's movement and growth mechanics. */
        void move() {
            int i = CONVERT.get(direction)[0];
            int j = CONVERT.get(direction)[1];
            int a = head.getPosition().get(0);
            int b = head.getPosition().get(1);
            /* Movement is achieved by creating a new Block object in
             * the head's next position, ... */
            head = new Block(i+a, j+b);
            entities.get("body").add(head);
            coords.add(createPosition(i+a, j+b));
            /* ...keeping the tail in the same place if the head does
             * not hit the food, ... */
            if (growing) {
                growing = !growing;
            } else {
                /* ... or deleting it (references and drawing)
                 * otherwise. */
                tail.hide();
                entities.get("body").remove(0);
                coords.remove(0);
                tail = entities.get("body").get(0);
            }
        }
    }

    /** Organizes the food's spawn randomly. */
    private class Food extends BlinkingBlock {
        /**
         * Generates random coordinates to spawn a
         * {@link BlinkingBlock} at.
         */
        Food() {
            super(random.nextInt(10), random.nextInt(20));
            // Add the instance to the container for drawing.
            entities.get("food").clear();
            entities.get("food").add(this);
        }

        /**
         * Draws a {@link BlinkingBlock} at a new random position in
         * the grid.
         */ 
        void respawn() {
            Sprite sprite = getSprite();
            setPosition(random.nextInt(10), random.nextInt(20));
            sprite.show();
        }
    }

    public static void main(String[] args) {
        class SnakeClient extends Client {
            Snake game;
            SnakeClient() {
                super();  // <-- setup() and setLoop() are invoked here.
                window.setTitle("Snake");
            }
            /** Initializes the game. */
            @Override
            public void setup() {
                super.setup();
                this.game = new Snake();
                this.game.start();
            }
            /** List of scheduled events. */
            @Override
            public void setLoop() {
                super.setLoop();
                // Implementing the game mechanics and checking for endgame.
                game.manage(ticks);
                // Drawing the game's objects to the screen.
                game.drawEntities();
                // Updating BlinkingBlock's mechanics.
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
        new SnakeClient();
    }
}
