package com.brickgame.games;

import java.util.*;
import java.awt.event.*;
import static com.brickgame.Constants.*;
import static com.brickgame.screens.Sprite.createPosition;
import com.brickgame.Client;
import com.brickgame.Constants.Direction;
import com.brickgame.block.Block;

/**
 * A Tetris game.
 * <p>
 * Read the <a href="{@docRoot}/docs/GameManuals.md">Game Manuals</a>.
 * 
 * @author  <a href="https://github.com/marcantonio64/">marcantonio64</a>
 * @version "%I%, %G%"
 * @since   1.8
 */
public class Tetris extends GameEngine {
    private static Random random = new Random();
    private static double startSpeed = 1.0;
    private static char storedShape;
    /** Allows movement when a piece touches the floor. */
    private double floatSpeed;
    private Piece piece;
    private FallenBlocks fallen;

    /** Constructor for {@link Tetris}. */
    public Tetris() {
        super();
        // Setting containers for the entities.
        setEntities("piece", "fallen");
    }

    /** Defines game objects. */
    @Override
    public void start() {
        super.start();
        this.floatSpeed = startSpeed;
        this.speed = (int)startSpeed;
        // Spawning the entities.
        this.piece = new Piece();  // Tetrominoes
        this.fallen = new FallenBlocks();  // Fallen remains
        this.piece.printPreview();
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
                // Setting Piece's movement.
                if (key == KeyEvent.VK_UP) {
                    piece.rotate();
                } else if (key == KeyEvent.VK_DOWN) {
                    piece.setDirection(Direction.DOWN);
                } else if (key == KeyEvent.VK_LEFT) {
                    piece.setDirection(Direction.LEFT);
                } else if (key == KeyEvent.VK_RIGHT) {
                    piece.setDirection(Direction.RIGHT);
                } else if (key == KeyEvent.VK_SPACE) {
                    piece.drop();
                    spawnNext();
                } else if (key == KeyEvent.VK_SHIFT) {
                    if (!piece.switchLocked) {
                        piece.switchShapes();  // Only once for every new piece.
                    }
                }
            } else {  // Key released.
                if (key == KeyEvent.VK_DOWN) {
                    piece.setDirection(null);
                } else if (key == KeyEvent.VK_LEFT) {
                    piece.setDirection(null);
                } else if (key == KeyEvent.VK_RIGHT) {
                    piece.setDirection(null);
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
            // Setting the action rate at speed Blocks per second.
            if (t % (FPS/speed) == 0) {
                piece.move(Direction.DOWN);  // Slow fall
                trySpawnNext();
            }
            // speed scales over time, every 30 seconds.
            if (speed <= 10) {
                if (t % (30*FPS) == 0) {
                    floatSpeed *= Math.pow(10.0, 0.05);
                    if ((int)floatSpeed > speed) {
                        speed = (int)floatSpeed;
                    }
                }
                
            }
            // Adjusting for movement proportional to the scaling speed.
            if (t % (FPS/(7 + 3*speed)) == 0) {
                // Horizontal movement and downwards acceleration.
                piece.move();
            }
        }
        // Managing endgame.
        super.manage(t);
    }

    /** Scoring mechanics. */
    void updateScore(int fullLines) {
        // More points for more lines at once.
        switch (fullLines) {
            case 1:
                score += (2 + speed*fallen.height)*15;
                break;
            case 2:
                score += (6 + speed*fallen.height)*15;
                break;
            case 3:
                score += (12 + speed*fallen.height)*15;
                break;
            case 4:
                score += (20 + speed*fallen.height)*15;
                break;
            default:;
        }
        super.updateScore();
    }

    /** pieces will spawn when they stop falling. */
    private void trySpawnNext() {
        if (piece.height == 0) {
            spawnNext();
        }
    }

    private void spawnNext() {
        // Transfering the piece's Blocks to the fallen structure.
        fallen.grow();

        // Accounting for a proper score according to the lines cleared.
        int fullLines = fallen.removeFullLines();
        updateScore(fullLines);

        // Spawning a new Piece object.
        piece = new Piece();
        piece.printPreview();
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
     * Occurs happens if the fallen structure reaches the top of the
     * grid.
     * 
     * @return Whether the game was lost.
     */
    @Override
    public boolean checkDefeat() {
        return fallen.height > 20;
    }

    /** Organizes the four-tiled-piece's mechanics. */
    private class Piece {
        Direction direction;
        int height;
        int[] coordinates = new int[2];
        boolean switchLocked;
        char activeShape;
        private int nextID;
        private char[] shapes = {'T', 'J', 'L', 'S', 'Z', 'I', 'O'};
        private List<Block> blocks;
        /** {@code Object[]} is a {@code [int, int[2][16]]} array. */
        private Map<Integer, Object[]> rotationMap;
        
        /** Manages spawn and preview. */
        Piece() {
            height = 19;
            switchLocked = false;
            rotationMap = new HashMap<>();
            /* If storedShape has been initialized, activeShape
             * shall obtain its value. */
            if (storedShape != '\u0000') {
                activeShape = storedShape;
            } else {
                /* Otherwise, the value of activeShape shall be
                    * chosen randomly. */
                int randomIndex = random.nextInt(7);
                activeShape = shapes[randomIndex];
            }
            // Storing a new shape.
            int randomIndex = random.nextInt(7);
            storedShape = shapes[randomIndex];
            // Spawning a Piece object with the activeShape.
            spawn();
            // Permitting one switch between activeShape and storedShape.
            switchLocked = false;
        }

        /** Draws the desired piece on the screen, at top center. */
        void spawn() {
            place(activeShape, 4, 0);
            // Identifying the next rotated position.
            nextID = 1;
            // Drawing it.
            entities.get("piece").clear();
            blocks = new ArrayList<>(4);
            for (int[] coords : (int[][])rotationMap.get(1)[1]) {
                Block block = new Block(coords);
                blocks.add(block);
                entities.get("piece").add(block);
            }
        }

        /** Showcases {@link Tetris#storedShape}. */
        void printPreview() {
            String drawing;
            switch (storedShape) {
                case 'T':
                    drawing = ""
                    + "============\n"
                    + "Next:\n"
                    + "    _\n"
                    + " _ |_| _\n"
                    + "|_||_||_|";
                    System.out.println(drawing); 
                    break;
                case 'J':
                    drawing = ""
                    + "============\n"
                    + "Next:\n"
                    + " _\n"
                    + "|_| _  _\n"
                    + "|_||_||_|";
                    System.out.println(drawing); 
                    break;
                case 'L':
                    drawing = ""
                    + "============\n"
                    + "Next:\n"
                    + "       _\n"
                    + " _  _ |_|\n"
                    + "|_||_||_|";
                    System.out.println(drawing); 
                    break;
                case 'S':
                    drawing = ""
                    + "============\n"
                    + "Next:\n"
                    + "    _  _\n"
                    + " _ |_||_|\n"
                    + "|_||_|";
                    System.out.println(drawing); 
                    break;
                case 'Z':
                    drawing = ""
                    + "============\n"
                    + "Next:\n"
                    + " _  _\n"
                    + "|_||_| _\n"
                    + "   |_||_|";
                    System.out.println(drawing); 
                    break;
                case 'I':
                    drawing = ""
                    + "============\n"
                    + "Next:\n"
                    + " _  _  _  _\n"
                    + "|_||_||_||_|";
                    System.out.println(drawing); 
                    break;
                case 'O':
                    drawing = ""
                    + "============\n"
                    + "Next:\n"
                    + " _  _\n"
                    + "|_||_|\n"
                    + "|_||_|";
                    System.out.println(drawing); 
                    break;
                default:;
            }
        }

        /**
         * Mechanics for switching {@link Piece piece}s.
         * <p>
         * Change a {@code piece} with {@link #activeShape} to one
         * with {@link Tetris#storedShape} (only once for each new
         * spawn).
         */
        void switchShapes() {
            // Clearing the current piece's drawing and references.
            entities.get("piece").clear();

            // Switching shapes and reset the height.
            char s = storedShape;
            char a = activeShape;
            storedShape = a;
            activeShape = s;
            height = 19;

            spawn();
            printPreview();
            switchLocked = true;  // Only once for every new piece.
        }

        /**
         * Positions of {@link Block}s to form each shape and track
         * rotation.
         * 
         * @param shape The desired shape.
         * @param i     Horizontal coordinate reference.
         * @param j     Vertical coordinate reference.
         */
        void place(char shape, int i, int j) {
            this.coordinates = new int[] {i, j};
            switch (shape) {
                case 'T':
                    rotationMap.put(1, new Object[] {2, new int[][] {new int[] {i-1, j}, new int[] {i, j}, new int[] {i+1, j}, new int[] {i, j-1}}});
                    rotationMap.put(2, new Object[] {3, new int[][] {new int[] {i-1, j}, new int[] {i, j}, new int[] {i, j-1}, new int[] {i, j+1}}});
                    rotationMap.put(3, new Object[] {4, new int[][] {new int[] {i-1, j}, new int[] {i, j}, new int[] {i+1, j}, new int[] {i, j+1}}});
                    rotationMap.put(4, new Object[] {1, new int[][] {new int[] {i, j-1}, new int[] {i, j}, new int[] {i+1, j}, new int[] {i, j+1}}});
                    break;
                case 'J':
                    rotationMap.put(1, new Object[] {2, new int[][] {new int[] {i-1, j-1}, new int[] {i-1, j}, new int[] {i, j}, new int[] {i+1, j}}});
                    rotationMap.put(2, new Object[] {3, new int[][] {new int[] {i, j-1}, new int[] {i, j}, new int[] {i, j+1}, new int[] {i-1, j+1}}});
                    rotationMap.put(3, new Object[] {4, new int[][] {new int[] {i-1, j-1}, new int[] {i, j-1}, new int[] {i+1, j-1}, new int[] {i+1, j}}});
                    rotationMap.put(4, new Object[] {1, new int[][] {new int[] {i-1, j-1}, new int[] {i, j-1}, new int[] {i-1, j}, new int[] {i-1, j+1}}});
                    break;
                case 'L':
                    rotationMap.put(1, new Object[] {2, new int[][] {new int[] {i-1, j}, new int[] {i, j}, new int[] {i+1, j}, new int[] {i+1, j-1}}});
                    rotationMap.put(2, new Object[] {3, new int[][] {new int[] {i-1, j-1}, new int[] {i, j-1}, new int[] {i, j}, new int[] {i, j+1}}});
                    rotationMap.put(3, new Object[] {4, new int[][] {new int[] {i-1, j}, new int[] {i-1, j-1}, new int[] {i, j-1}, new int[] {i+1, j-1}}});
                    rotationMap.put(4, new Object[] {1, new int[][] {new int[] {i-1, j-1}, new int[] {i-1, j}, new int[] {i-1, j+1}, new int[] {i, j+1}}});
                    break;
                case 'S':
                    rotationMap.put(1, new Object[] {2, new int[][] {new int[] {i-1, j}, new int[] {i, j}, new int[] {i, j-1}, new int[] {i+1, j-1}}});
                    rotationMap.put(2, new Object[] {1, new int[][] {new int[] {i, j+1}, new int[] {i, j}, new int[] {i-1, j}, new int[] {i-1, j-1}}});
                    break;
                case 'Z':
                    rotationMap.put(1, new Object[] {2, new int[][] {new int[] {i-1, j-1}, new int[] {i, j-1}, new int[] {i, j}, new int[] {i+1, j}}});
                    rotationMap.put(2, new Object[] {1, new int[][] {new int[] {i, j - 1}, new int[] {i, j}, new int[] {i-1, j}, new int[] {i-1, j+1}}});
                    break;
                case 'I':
                    rotationMap.put(1, new Object[] {2, new int[][] {new int[] {i-1, j}, new int[] {i, j}, new int[] {i+1, j}, new int[] {i+2, j}}});
                    rotationMap.put(2, new Object[] {1, new int[][] {new int[] {i, j-1}, new int[] {i, j}, new int[] {i, j+1}, new int[] {i, j+2}}});
                    break;
                case 'O':
                    rotationMap.put(1, new Object[] {1, new int[][] {new int[] {i, j}, new int[] {i, j+1}, new int[] {i+1, j}, new int[] {i+1, j+1}}});
                    break;
                default:;
            }
        }

        /**
         * Tracks the boundary dimensions of each {@link Piece} and
         * updates its {@link #height}.
         * 
         * @return Horizontal and vertical limits.
         */
        private int[] calculateDimensions() {
            int iMin; int iMax; int jMax;
            List<Integer> iList = new ArrayList<>(4);
            List<Integer> jList = new ArrayList<>(4);
            for (Block block : blocks) {
                iList.add(block.getPosition().get(0));
                jList.add(block.getPosition().get(1));
            }
            iMin = Collections.min(iList);
            iMax = Collections.max(iList);
            jMax = Collections.max(jList);
            
            // Height calculation.
            List<Integer> heights = new ArrayList<>(4);
            List<Integer> vPiece;
            List<Integer> vFall;
            for (int i = iMin; i <= iMax; i++) {
                // For each horizontal coordinate,...
                vPiece = new ArrayList<>();
                vFall = new ArrayList<>();
                vPiece.add(0);
                for (Block block : blocks) {
                    if (block.getPosition().get(0) == i) {
                        vPiece.add(block.getPosition().get(1));
                    }
                }
                // ... the piece's lowest vertical coordinate is compared to...
                int j = Collections.max(vPiece);
                // ... the fallen structure's highest vertical coordinate.
                for (Block block : entities.get("fallen")) {
                    if (block.getPosition().get(0) == i
                            && block.getPosition().get(1) > j) {
                        vFall.add(block.getPosition().get(1));
                    }
                }
                // If there's anything below the piece in this column, ...
                if (!vFall.isEmpty()) {
                    // the distance is added to the list of heights.
                    heights.add(Collections.min(vFall)-j-1);
                } else {
                    /* Otherwise, the distance to the bottom of the grid is
                     * added to the list instead. */
                    heights.add(19-j);
                }
            }
            /* The height is the shortest distance from a piece to the fallen
             * structure. */
            height = Collections.min(heights);

            return new int[] {iMin, iMax, jMax};
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

        /**
         * Checks if there are obstacles for movement, moves if not.
         * 
         * @param direction One of {@code Direction.UP}, {@code Direction.DOWN},
         *                  {@code Direction.LEFT}, {@code Direction.RIGHT}.
         */
        void move(Direction direction) {
            /* Gattering all the necessary dimensions to detect whether
             * movement is possible. */
            int a = CONVERT.get(direction)[0];
            int b = CONVERT.get(direction)[1];
            int i = coordinates[0];
            int j = coordinates[1];
            int[] dims = calculateDimensions();
            int iMin = dims[0];
            int iMax = dims[1];
            int jMax = dims[2];

            /* First, a set with the desired new positions for
             * each Block in the piece. */
            Set<ArrayList<Integer>> X = new HashSet<>();
            for (Block block : blocks) {
                X.add(createPosition(block.getPosition().get(0)+a,
                                     block.getPosition().get(1)+b));
            }
            /* Second, another set with the current positions of the
             * already formed structure. */
            Set<ArrayList<Integer>> Y = new HashSet<>();
            for (Block block : entities.get("fallen")) {
                Y.add(block.getPosition());
            }
            // Movement can happen if these sets don't intersect.
            if (Collections.disjoint(X, Y)) {
                /* Checking also if the movement won't get any Block
                 * outside the grid. */
                if (0 <= iMin+a && iMax+a < 10 && jMax+b < 20) {
                    // Updating rotatingMap.
                    place(activeShape, i+a, j+b);
                    // Making the movement.
                    for (Block block : blocks) {
                        block.setPosition(block.getPosition().get(0)+a,
                                          block.getPosition().get(1)+b);
                    }
                }
            }
        }

        /** Checks if there are obstacles for movement, moves if not. */
        void move() {
            move(direction);
        }

        /** Checks if there are obstacles for rotation, rotates if not. */
        void rotate() {
            /* First set: desired new positions of the piece's
             * Blocks if a rotation were to happen. */
            Set<ArrayList<Integer>> X = new HashSet<>();
            int nextIDCandidate = (int)rotationMap.get(nextID)[0];
            for (int[] coords : (int[][])rotationMap.get(nextIDCandidate)[1]) {
                X.add(createPosition(coords));
            }
            /* Second set: current positions of the Blocks in
             * the fallen structure. */
            Set<ArrayList<Integer>> Y = new HashSet<>();
            for (Block block : entities.get("fallen")) {
                Y.add(block.getPosition());
            }
            /* Third set: Block positions outside the borders after
             * the rotation. */
            Set<List<Integer>> Z = new HashSet<>();
            for (ArrayList<Integer> pos : X) {
                int i = pos.get(0);
                int j = pos.get(1);
                if (i < 0 || i >= 10 || j >= 20) {
                    Z.add(createPosition(i, j));
                }
            }
            /* If the rotated piece doesn't collide with the
             * structure and remains inside the grid, then movement
             * occurs. */
            if (Collections.disjoint(X, Y) && Z.isEmpty()) {
                // Erasing the current piece from the screen.
                entities.get("piece").clear();
                // Replacing it with another with the next rotated state.
                place(activeShape, coordinates[0], coordinates[1]);
                // Updating rotatingMap the rotating id and drawing the piece's Blocks.
                nextID = (Integer)rotationMap.get(nextID)[0];
                blocks = new ArrayList<>();
                for (int[] coords : (int[][])rotationMap.get(nextID)[1]) {
                    Block block = new Block(coords);
                    blocks.add(block);
                    entities.get("piece").add(block);
                }
            }
        }

        /** Hard drop. Moves down a piece by its full height. */
        void drop() {
            // Updating the height.
            calculateDimensions();
            // Dropping.
            for (int i = height; i > 0; i--) {
                move(Direction.DOWN);
            }
            // Checking if the piece actually hit the bottom.
            calculateDimensions();
            if (height > 0) {
                for (int i = height; i > 0; i--) {
                    move(Direction.DOWN);
                }
            }
        }
    }

    /** The structure formed by the fallen {@link Piece piece}'s {@link Block}s. */
    private class FallenBlocks {
        int height;  // Not the same as piece.height.
        
        FallenBlocks() {
            this.height = 0;
        }

        /** Makes {@link Piece} part of the structure. */
        void grow() {
            /* Transfering the Blocks from the "piece" group to the
             * "fallen" group. */
            entities.get("fallen").addAll(piece.blocks);
            entities.get("piece").clear();
            // Updating the structure's height.
            List<Integer> vFallen = new ArrayList<>();
            vFallen.add(20);
            for (Block block : entities.get("fallen")) {
                vFallen.add(block.getPosition().get(1));
            }
            height = 20 - Collections.min(vFallen);
        }

        /**
         * @return Number of lines removed.
         */
        int removeFullLines() {
            Map<Integer, List<Block>> fullLines = new LinkedHashMap<>();
            /* Grouping all the completed lines (with 10 aligned Blocks)
             * into fullLines. */
            for (int b = 0; b < 20; b++) {
                List<Block> line = new ArrayList<>();
                for (Block block : entities.get("fallen")) {
                    if (block.getPosition().get(1) == b) {
                        line.add(block);
                    }
                }
                if (line.size() == 10) {
                    fullLines.put(b, line);
                }
            }
            /* Removing them from the structure and lowering the Blocks
             * above it. */
            for (Map.Entry<Integer, List<Block>> entry : fullLines.entrySet()) {
                int b = entry.getKey();
                List<Block> line = entry.getValue();
                entities.get("fallen").removeAll(line);
                for (Block block : entities.get("fallen")) {
                    if (block.getPosition().get(1) < b) {
                        block.setPosition(block.getPosition().get(0), block.getPosition().get(1)+1);
                    }
                }
            }

            return fullLines.size();
        }
    }

    public static void main(String[] args) {
        class TetrisClient extends Client {
            Tetris game;
            TetrisClient() {
                super();  // setup() and setLoop() are invoked here.
                window.setTitle("Tetris");
            }
            /** Initializes the game. */
            @Override
            public void setup() {
                super.setup();
                this.game = new Tetris();
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
        new TetrisClient();
    }
}
