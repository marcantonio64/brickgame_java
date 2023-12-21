package com.brickgame.games;

import java.util.*;
import java.awt.event.*;
import static com.brickgame.Constants.*;
import static com.brickgame.screens.Sprite.createPosition;
import com.brickgame.Client;
import com.brickgame.block.*;

/**
 * A Breakout game.
 * <p>
 * Read the <a href="{@docRoot}/docs/GameManuals.md">Game Manuals</a>.
 * 
 * @author  <a href="https://github.com/marcantonio64/">marcantonio64</a>
 * @version "%I%, %G%"
 * @since   1.8
 */
public class Breakout extends GameEngine {
    private static int total = 0;
    /** Used to count the target {@link Block}s at the start of a loop. */
    private static int number = 0;
    private static int startSpeed = 15;
    private static int[] speedValues = new int[2];
    private int level;
    private Target target;
    private Ball ball;
    private Paddle paddle;

    /** Constructor for {@link Breakout}. */
    public Breakout() {
        super();
        // Setting containers for the entities.
        setEntities("target", "ball", "paddle");
    }

    /** Defines game objects. */
    @Override
    public void start() {
        super.start();
        speedValues[0] = startSpeed;
        speedValues[1] = 2*speedValues[0];
        this.level = 1;
        this.speed = startSpeed;
        // Spawning the entities.
        this.target = new Target(this.level);
        this.ball = new Ball(4, 18);
        this.paddle = new Paddle(this.ball);
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
                // Setting Paddle's movement.
                if (key == KeyEvent.VK_SPACE) {
                    speed = speedValues[1];
                } else if (key == KeyEvent.VK_LEFT) {
                    paddle.setDirection(Direction.LEFT);
                } else if (key == KeyEvent.VK_RIGHT) {
                    paddle.setDirection(Direction.RIGHT);
                }
            } else {  // Key released.
                if (key == KeyEvent.VK_LEFT) {
                    paddle.setDirection(null);
                } else if (key == KeyEvent.VK_RIGHT) {
                    paddle.setDirection(null);
                } else if (key == KeyEvent.VK_SPACE) {
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
            // Dealing with speed values greater than FPS.
            for (int i = 0; i <= speed/FPS; i++) {
                // Setting the action rate at speed Blocks per second.
                int q = (speed % FPS > 0) ? FPS/(speed % FPS) : 1;
                if (t % q == 0) {
                    // Moving the ball.
                    ball.move();

                    // Checking if ball hit target.
                    target.checkHit(ball);
                    updateScore();

                    /* Checking if there still are Blocks left in target,
                    * calling the next stage if not. */
                    manageLevels();

                    ball.checkBorderReflect();

                    // Dealing with immediate collision after hitting a border.
                    target.checkHit(ball);
                    updateScore();
                    manageLevels();

                    // paddle dragging ball upon contact.
                    paddle.checkPaddleDrag();
                    paddle.checkPaddleReflect();

                    /* Avoiding the ball from leaving the screen by the
                    * lateral borders. */
                    ball.checkBorderReflect();

                    // Making the paddle move.
                    paddle.move(speed);
                }
            }
        }
        // Managing endgame.
        super.manage(t);
    }

    /** Scoring mechanics. */
    @Override
    void updateScore() {
        // target Blocks left. */
        int n = entities.get("target").size();
        for (int i = number; i > n; i--) {
            switch (level) {
                case 1:
                    score += 15;
                    break;
                case 2:
                    score += 20;
                    break;
                case 3:
                    score += 30;
                    break;
                default:;
            }
        }
        number = n;  // Updating the number of target Blocks.
        super.updateScore();
    }

    /** Turns to the next stage upon clearing the current one. */
    void manageLevels() {
        if (level <= 3 && entities.get("target").isEmpty()) {
            // Toggling the next stage.
            System.out.println("Stage " + level + " cleared");
            level += 1;
            // Adding a bonus score from phase completion.
            score += 3000 + 3000*(level - 1);
            // Constructing the next target.
            target = new Target(level);
            // Deleting and respawning the ball.
            ball.hide();
            entities.get("ball").clear();
            ball = new Ball(4, 18);
            // Respawning the paddle.
            for (Block block : entities.get("paddle")) {
                block.hide();
            }
            entities.get("paddle").clear();
            paddle = new Paddle(ball);
        }
    }

    /**
     * Victory occurs when all 3 stages are cleared.
     * 
     * @return Whether the game has been beaten.
     */
    @Override
    public boolean checkVictory() {
        return level == 4;
    }

    /**
     * Defeat happens if the {@link #ball} falls past the bottom
     * border.
     * 
     * @return Whether the game was lost.
     */
    @Override
    public boolean checkDefeat() {
        return ball.getPosition().get(1) > 19;
    }

    /**
     * Manages the {@link Block}s to be destroyed.
     * <p>
     * Organizes the drawing and breaking of the target
     * {@code Block}s at the top of the grid.
     */
    private class Target {
        /** Mapping coordinates to {@link Block}s. */
        Map<ArrayList<Integer>, Block> mapBlock = new HashMap<>();

        /**
         * Builds the {@link Target target}'s structure.
         * 
         * @param level The current stage.
         */
        Target(int level) {
            // Cleaning the target's drawing and references.
            entities.get("target").clear();
            Map<Integer, int[]> sketch;
            switch (level) {
                case 1:
                    sketch = new HashMap<>(10);
                    sketch.put(0, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(1, new int[] {0,                         9});
                    sketch.put(2, new int[] {0,                         9});
                    sketch.put(3, new int[] {0,       3, 4, 5, 6,       9});
                    sketch.put(4, new int[] {0,       3, 4, 5, 6,       9});
                    sketch.put(5, new int[] {0,       3, 4, 5, 6,       9});
                    sketch.put(6, new int[] {0,       3, 4, 5, 6,       9});
                    sketch.put(7, new int[] {0,                         9});
                    sketch.put(8, new int[] {0,                         9});
                    sketch.put(9, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});

                    this.mapBlock = new HashMap<>();
                    for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                        int j = entry.getKey();
                        for (int i : entry.getValue()) {
                            ArrayList<Integer> coordinates = createPosition(i, j);
                            Block block = new Block(coordinates);
                            mapBlock.put(coordinates, block);
                            entities.get("target").add(block);
                        }
                    }
                    break;
                case 2:
                    sketch = new HashMap<>(6);
                    sketch.put(0, new int[] {0, 1,                   8, 9});
                    sketch.put(1, new int[] {0, 1, 2,             7, 8, 9});
                    sketch.put(2, new int[] {   1, 2, 3,       6, 7, 8   });
                    sketch.put(3, new int[] {      2, 3, 4, 5, 6, 7      });
                    sketch.put(4, new int[] {   1, 2, 3,       6, 7, 8   });
                    sketch.put(5, new int[] {0, 1, 2,             7, 8, 9});
                    sketch.put(6, new int[] {0, 1,                   8, 9});

                    this.mapBlock = new HashMap<>();
                    for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                        int j = entry.getKey();
                        for (int i : entry.getValue()) {
                            ArrayList<Integer> coordinates = createPosition(i, j);
                            Block block = new Block(coordinates);
                            mapBlock.put(coordinates, block);
                            entities.get("target").add(block);
                        }
                    }
                    break;
                case 3:
                    sketch = new HashMap<>(6);
                    sketch.put(0, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(1, new int[] {0,          4, 5,          9});
                    sketch.put(2, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(3, new int[] {0,          4, 5,          9});
                    sketch.put(4, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(5, new int[] {0,          4, 5,          9});
                    sketch.put(6, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});

                    this.mapBlock = new HashMap<>();
                    for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                        int j = entry.getKey();
                        for (int i : entry.getValue()) {
                            ArrayList<Integer> coordinates = createPosition(i, j);
                            Block block = new Block(coordinates);
                            mapBlock.put(coordinates, block);
                            entities.get("target").add(block);
                        }
                    }
                    break;
                default:;

                /* Template for more stages
                case X:
                    sketch = new HashMap<>(10);
                    sketch.put(0, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(1, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(2, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(3, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(4, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(5, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(6, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(7, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(8, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    sketch.put(9, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                    
                    this.mapBlock = new HashMap<>();
                    for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                        int j = entry.getKey();
                        for (int i : entry.getValue()) {
                            ArrayList<Integer> coordinates = createPosition(i, j);
                            Block block = new Block(coordinates);
                            mapBlock.put(coordinates, block);
                            entities.get("target").add(block);
                        }
                    }
                */                
            }

            total = mapBlock.size();
            number = total;
        }

        /**
         * Collisions of {@code ball} and {@code target}.
         * 
         * @param ball The active {@code Ball()} instance.
         */
        void checkHit(Ball ball) {
            int a = ball.velocity[0];
            int b = ball.velocity[1];
            int i = ball.getPosition().get(0);
            int j = ball.getPosition().get(1);
            ArrayList<Integer> hCoords = createPosition(i+a, j);
            ArrayList<Integer> vCoords = createPosition(i, j+b);
            ArrayList<Integer> xCoords = createPosition(i+a, j+b);
            Block hBlock = mapBlock.get(hCoords);
            Block vBlock = mapBlock.get(vCoords);
            Block xBlock = mapBlock.get(xCoords);
            class del {
                /** A deletion constructor for readability. */
                del(char c){
                switch (c) {
                    case 'h':
                        hBlock.hide();
                        entities.get("target").remove(hBlock);
                        mapBlock.remove(hCoords);
                        break;
                    case 'v':
                        vBlock.hide();
                        entities.get("target").remove(vBlock);
                        mapBlock.remove(vCoords);
                        break;
                    case 'x':
                        xBlock.hide();
                        entities.get("target").remove(xBlock);
                        mapBlock.remove(xCoords);
                        break;
                    default:;}}}

            // When the ball hits a corner between two target Blocks...
            if (mapBlock.containsKey(hCoords) && mapBlock.containsKey(vCoords)) {
                // ... both directions are reversed,...
                ball.velocity = new int[] {-a, -b};
                // ... both Blocks that make the corner are destroyed,...
                new del('h');
                new del('v');
                // ... including the vertex between the targets, if it exists.
                if (mapBlock.containsKey(xCoords)) {
                    new del('x');
                }
            }
            // When the ball hits the target Blocks horizontally only...
            else if (mapBlock.containsKey(hCoords) && !mapBlock.containsKey(vCoords)) {
                // ... only its first coordinate is reversed, ...
                ball.velocity[0] = -a;
                // ... and only the Block it hit is destroyed.
                new del('h');
            }
            // When the ball hits the target Blocks vertically only...
            else if (!mapBlock.containsKey(hCoords) && mapBlock.containsKey(vCoords)) {
                // ... only its second coordinate is reversed, ...
                ball.velocity[1] = -b;
                // ... and only the Block it hit is destroyed.
                new del('v');
            }
            // When the ball hits the target Blocks at exactly a vertex...
            else if (mapBlock.containsKey(xCoords)) {
                // ... both directions are reversed, ...
                ball.velocity = new int[] {-a, -b};
                // ... and the Block at said vertex is destroyed.
                new del('x');
            }
        }
    }

    /**
     * The player-controlled paddle.
     * <p>
     * Organizes the drawing, movement, dragging, and reflection off
     * of the paddle.
     */
    private class Paddle {
        private Direction direction;
        private boolean dragging;
        private Ball ball;
        private List<Block> blocks;
        private List<ArrayList<Integer>> coords;
        private int size;
        /**
         * Builds the {@link Paddle paddle}.
         * <p>
         * It must drag the ball at the start of each phase.
         * 
         * @param ball The {@code Ball()} instance.
         */
        Paddle(Ball ball) {
            this.ball = ball;
            
            // Setting the paddle's initial position.
            this.blocks = new ArrayList<>();
            this.blocks.add(new Block(3, 19));
            this.blocks.add(new Block(4, 19));
            this.blocks.add(new Block(5, 19));
            this.size = this.blocks.size();
            
            /* Adding the ball to the paddle initially to allow for a
             * launching choice. */
            this.blocks.add(ball);
            
            /* Tracking coordinates and adding to the corresponding group
             * for drawing. */
            this.coords = new ArrayList<>();
            for (Block block : this.blocks) {
                this.coords.add(block.getPosition());
                entities.get("paddle").add(block);
            }
        }

        /**
         * Mechanics for the {@link #paddle}'s movement and the
         * {@link #ball}'s launch.
         * 
         * @param speed The {@code ball} {@link #speed} in
         *              {@link Block}s per second.
         */
        void move(int speed) {
            /* Taking a reference at the leftmost horizontal
             * coordinate of the paddle. */
            int L = coords.get(0).get(0);
            int a = CONVERT.get(direction)[0];
            for (Block block : blocks) {
                int i = block.getPosition().get(0);
                int j = block.getPosition().get(1);
                // Ensuring the paddle will remain within the screen.
                if (0 <= L+a && L+a <= 10-size) {
                    block.setPosition(i+a, j);
                }
            }
            // Updating the paddle's coordinates.
            this.coords = new ArrayList<>();
            for (Block block : this.blocks) {
                this.coords.add(block.getPosition());
            }

            /* Launch mechanics at the start of the stage (the ball is
             * released from the paddle if *Space* is pressed). */
            if (speed > startSpeed) {
                if (coords.size() > size  // Stage start conditions.
                        && number == total) {
                    entities.get("paddle").remove(ball);
                    blocks.remove(size);
                    coords.remove(size);
                    // Updating ball data.
                    ball.isMoving = true;
                    ball.velocity = new int[] {1, -1};  // First direction.
                }
            }
        }

        /**
         * Where to move the {@link Paddle}.
         * 
         * @param direction One of {@code Direction.UP}, {@code Direction.DOWN},
         *                  {@code Direction.LEFT}, {@code Direction.RIGHT}.
         */
        void setDirection(Direction direction) {
            this.direction = direction;
        }

        /** Manages {@link #ball} drag and release. */
        void checkPaddleDrag() {
            int i = ball.getPosition().get(0);
            int j = ball.getPosition().get(1);
            int b = ball.velocity[1];
            /* Allowing for dragging the ball when it hits the paddle
             * from the top. */
            if (coords.subList(0, size)
                      .contains(createPosition(i, j+b))) {
                /* The ball will become part of the paddle for exactly
                 * one iteration. */
                dragging = !dragging;
                if (dragging) {
                    ball.isMoving = false;
                    blocks.add(ball);
                    entities.get("paddle").add(ball);
                } else {
                    blocks.remove(size);
                    coords.remove(size);
                    entities.get("paddle").remove(ball);
                    ball.isMoving = true;
                }
            }
        }

        /** Hypothesis for when the {@link #ball} reflects from the paddle. */
        void checkPaddleReflect() {
            if (!dragging) {
                int i = ball.getPosition().get(0);
                int j = ball.getPosition().get(1);
                int a = ball.velocity[0];
                int b = ball.velocity[1];
                ArrayList<Integer> vCoords = createPosition(i, j+b);
                ArrayList<Integer> xCoords = createPosition(i+a, j+b);
                /* Vertical reflection occurs if the ball hits the
                 * paddle directly from above or at a vertex. */
                if (coords.subList(0, size).contains(vCoords)
                        || coords.subList(0, size).contains(xCoords)) {
                    ball.velocity[1] = -1;
                    /* Horizontal reflection happens only when the
                     * paddle is hit at a vertex. */
                    if (vCoords.equals(coords.get(0))
                            || xCoords.equals(coords.get(0))) {
                        /* The ball moves left after hitting the left
                         * corner of the paddle. */
                        ball.velocity[0] = -1;
                    } else if (vCoords.equals(coords.get(size-1))
                                || xCoords.equals(coords.get(size-1))) {
                        /* The ball moves right if it hit the right
                         * corner of the paddle. */
                        ball.velocity[0] = 1;
                    }
                }
            }
        }
    }

    /** Deals with ball's movement, spawning, and border reflection. */
    private class Ball extends Block {
        boolean isMoving;
        int[] velocity;
        Ball(int i, int j) {
            super(i, j);
            this.isMoving = false;
            this.velocity = new int[] {0, 0};
            // Enabling drawing.
            entities.get("ball").add(this);
        }

        /** Sets the diagonal movement. */
        @Override
        public void move() {
            if (isMoving) {
                setPosition(getPosition().get(0)+velocity[0],
                            getPosition().get(1)+velocity[1]);
            }
        }

        /** Hypothesis for when the ball reflects from the border. */
        void checkBorderReflect() {
            int i = getPosition().get(0);
            int j = getPosition().get(1);
            int a = velocity[0];
            int b = velocity[1];
            /* Reversing the horizontal coordinate if the ball hits a
             * vertical border. */
            if (i == 0 && a == -1 || i == 9 && a == 1) {
                velocity[0] = -a;
            }
            /* Reversing the vertical coordinate if the ball hits a
             * horizontal border. */
            if (j == 0 && b == -1) {
                velocity[1] = 1;
            }
        }
    }

    public static void main(String[] args) {
        class BreakoutClient extends Client {
            Breakout game;
            BreakoutClient() {
                super();  // setup() and setLoop() are invoked here.
                window.setTitle("Breakout");
            }
            /** Initializes the game. */
            @Override
            public void setup() {
                super.setup();
                this.game = new Breakout();
                this.game.start();
            }
            /** List of scheduled events. */
            @Override
            public void setLoop() {
                super.setLoop();
                // Implementing the game mechanics and checking for endgame.
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
        new BreakoutClient();
    }
}