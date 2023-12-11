package com.brickgame.block;

import java.util.ArrayList;
import static com.brickgame.Constants.*;
import static com.brickgame.screens.Sprite.createPosition;
import com.brickgame.Client;
import com.brickgame.screens.Sprite;

/** Establishes the structure, appearance, and behavior of unit cells. */
public class Block {
    // Initializing instance variables.
    ArrayList<Integer> coordinates;
    private Direction direction;

    /**
     * A constructor with raw coordinates.
     * 
     * @param i Horizontal position. Must be from {@literal 0}
     *          (inclusive) to {@literal 10} (exclusive) to
     *          show on the screen.
     * @param j Vertical position. Must be from {@literal 0}
     *          (inclusive) to {@literal 20} (exclusive) to
     *          show on the screen.
     */
    public Block(int i, int j) {
        this.coordinates = createPosition(i, j);
        show();
    }

    /**
     * A constructor with an array of coordinates.
     * 
     * @param coords A 2D-array with the coordinates. Must range from
     *               {@code [0, 0]} (inclusive) to {@code [9, 19]}
     *               (inclusive) to show on the screen.
     */
    public Block (int[] coords) {
        this(coords[0], coords[1]);
        if (coords.length != 2) {
            System.out.println("Wrong argument for Block: The coords arg must be of length 2");
            System.exit(0);
        }        
    }

    /**
     * A constructor with an ArrayList of coordinates.
     * 
     * @param list An ArrayList of size 2 with the coordinates. Must
     *             range from {@code [0, 0]} (inclusive) to
     *             {@code [9, 19]} (inclusive) to show on the screen.
     */
    public Block (ArrayList<Integer> list) {
        this(list.get(0), list.get(1));
        if (list.size() != 2) {
            System.out.println("Wrong argument for Block: The list arg must be of size 2");
            System.exit(0);
        }
    }
    private boolean isOnScreen() {
        int i = coordinates.get(0);
        int j = coordinates.get(1);
        return (0 <= i && i < 10 && 0 <= j && j < 20);
    }
    public Sprite getSprite() {
        return Client.spriteMap.get(coordinates);
    }
    public void show() {
        if (isOnScreen()) {
            getSprite().show();
        }
    }
    public void hide() {
        if (isOnScreen()) {
            getSprite().hide();
        }
    }
    public void blink(int t) {
        if (isOnScreen()) {
            getSprite().blink(t);
        }
    }

    /**
     * @return Current grid position.
     */
    public ArrayList<Integer> getPosition() {
        return coordinates;
    }
    
    /**
     * Positions the {@code Block} in the speficied position in the
     * 20x10 grid.
     * 
     * @param i Horizontal position. Must be from {@literal 0}
     *          (inclusive) to {@literal 10} (exclusive) to
     *          show on the screen.
     * @param j Vertical position. Must be from {@literal 0}
     *          (inclusive) to {@literal 20} (exclusive) to
     *          show on the screen.
     */
    public void setPosition(int i, int j) {
        hide();
        coordinates.set(0, i);
        coordinates.set(1, j);
        show();
    }
    
    /**
     * @return Current {@code direction}.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Where to move the {@code Block} within the 20x10 grid.
     * 
     * @param direction One of {@code Direction.UP}, {@code Direction.DOWN},
     *                  {@code Direction.LEFT}, {@code Direction.RIGHT}.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Changes the {@link Sprite} to the one at a new position
     * according to {@link #direction}.
     */
    public void move() {
        int[] vector = CONVERT.get(direction);
        setPosition(coordinates.get(0) + vector[0],
                    coordinates.get(1) + vector[1]);
    }

    // Testing a falling Block.
    public static void main(String[] args) {
        class BlockClient extends Client {
            Block block;
            @Override
            public void setup() {
                super.setup();
                this.block = new Block(3, 4);
                this.block.setDirection(Direction.DOWN);
            }
            @Override
            public void setLoop() {
                super.setLoop();
                if (ticks % FPS == 0) {
                    block.move();
                    canvas.repaint();
                }
            }
        }

        new BlockClient();
    }
}
