package com.brickgame.block;

import java.util.*;
import static com.brickgame.Constants.*;
import com.brickgame.Client;
import com.brickgame.Constants.Direction;

/**
 * Used to destroy a target upon collision.
 * <p>
 * The bombs are built as an {@link List} of {@link Block} objects,
 * forming an {@literal X} shape, akin to that of a sea mine.
 */
public class Bomb {
    public static List<ArrayList<Block>> bombs = new ArrayList<ArrayList<Block>>();
    private List<Block> group;
    /**
     * A constructor with raw coordinates where the required
     * {@link Block}, {@link BlinkingBlock}, and {@link HiddenBlock}
     * objects are created and organized in the given group.
     * 
     * @param i     Horizontal position. Must be from {@literal 0}
     *              (inclusive) to {@literal 10} (exclusive) to
     *              show on the screen.
     * @param j     Vertical position. Must be from {@literal 0}
     *              (inclusive) to {@literal 20} (exclusive) to
     *              show on the screen.
     * @param group A {@link List} containing the {@link Bomb}'s elements.
     */
    public Bomb(int i, int j, List<Block> group) {
        this.group = group;
        ArrayList<Block> bomb = new ArrayList<Block>();
        bomb.add(new BlinkingBlock(i, j));    // 4 outer corners.
        bomb.add(new BlinkingBlock(i, j+3));
        bomb.add(new BlinkingBlock(i+3, j));
        bomb.add(new BlinkingBlock(i+3, j+3));
        bomb.add(new Block(i+1, j+1));        // 4 core Blocks.
        bomb.add(new Block(i+1, j+2));
        bomb.add(new Block(i+2, j+1));
        bomb.add(new Block(i+2, j+2));
        bomb.add(new HiddenBlock(i, j+1));  // Filling spaces.
        bomb.add(new HiddenBlock(i, j+2));
        bomb.add(new HiddenBlock(i+3, j+1));
        bomb.add(new HiddenBlock(i+3, j+2));
        bomb.add(new HiddenBlock(i+1, j));
        bomb.add(new HiddenBlock(i+2, j));
        bomb.add(new HiddenBlock(i+1, j+3));
        bomb.add(new HiddenBlock(i+2, j+3));
        for (Block block : bomb) {
            this.group.add(block);  // Add bomb's components to group for drawing.
            if (block instanceof HiddenBlock) {
                block.hide();
            }
        }
        bombs.add(bomb);  // Add bomb itself to bombs.
    }

    /**
     * A constructor with coordinates as a 2D-array where the required
     * {@link Block}, {@link BlinkingBlock}, and {@link HiddenBlock}
     * objects are created and organized in the given group.
     * 
     * @param coords A 2D-array with the coordinates. Must range from
     *               {@code [0, 0]} (inclusive) to {@code [9, 19]}
     *               (inclusive) to show on the screen.
     * @param group  A {@link ArrayList list} containing the
     *               {@link Bomb}'s elements.
     */
    public Bomb(int[] coords, List<Block> group) {
        this(coords[0], coords[1], group);
    }

    /** An empty {@link Bomb} object. */
    public Bomb() {}

    /**
     * Moves {@code Bomb} one unit cell to the specified direction.
     * 
     * @param direction One of {@code Direction.UP}, {@code Direction.DOWN},
     *                  {@code Direction.LEFT}, {@code Direction.RIGHT}.
     */
    public void move(Direction direction) {
        int a = CONVERT.get(direction)[0];
        int b = CONVERT.get(direction)[1];
        Iterator<ArrayList<Block>> bombsIterator = bombs.iterator();
        while (bombsIterator.hasNext()) {
            ArrayList<Block> bomb = bombsIterator.next();
            // Moving each component using its setPosition() method.
            for (Block block : bomb) {
                int i = block.getPosition().get(0);
                int j = block.getPosition().get(1);
                block.setPosition(i+a, j+b);
                if (block instanceof HiddenBlock) {
                    block.hide();
                }
            }
            for (Block block : bomb) {
                if (!(block instanceof BlinkingBlock || block instanceof HiddenBlock)) {
                    block.show();
                }
            }

            // Deleting a Bomb when it exits the grid.
            int k = bomb.get(0).getPosition().get(1);
            if (direction == Direction.UP && k < 0  // At the top.
                    || direction == Direction.DOWN && k >= 17) {  // At the bottom.
                for (Block block : bomb) {
                    // Erase the drawings.
                    block.hide();
                    group.remove(block);
                }
                // Remove references.
                bombsIterator.remove();
            }
        }
    }

    /**
     * Manages collision detection.
     * 
     * @param target {@link Block} and {@link BlinkingBlock} objects
     *               to be destroyed.
     * @return       Whether any explosion happened.
     */
    public boolean checkExplosion(List<Block> target) {
        boolean eraseAny = false;
        // Iterate through bombs.
        Iterator<ArrayList<Block>> bombsIterator = bombs.iterator();
        while (bombsIterator.hasNext()) {
            ArrayList<Block> bomb = bombsIterator.next();
            int i = bomb.get(0).getPosition().get(0);
            int j = bomb.get(0).getPosition().get(1);
            boolean erase = false;
            // Detect explosion.
            for (Block block : target) {
                int a = block.getPosition().get(0);
                int b = block.getPosition().get(1);
                if ((i <= a && a <= i+3) && (j <= b && b <= j+3)) {
                    // Stop if Bomb hits any component of target.
                    erase = true;
                    eraseAny = true;
                }
            }
            if (erase) {
                explode(bomb, target);
                bombsIterator.remove();
            }
        }
        return eraseAny;
    }

    /**
     * Destroys the bomb and the target.
     * 
     * @param bomb   An exploding {@link Bomb}.
     * @param target {@link Block} and {@link BlinkingBlock} objects
     *               to be destroyed.
     */
    public void explode(ArrayList<Block> bomb, List<Block> target) {
        int i = bomb.get(0).getPosition().get(0);
        int j = bomb.get(0).getPosition().get(1);
        // Destroying the target.
        Iterator<Block> targetIterator = target.iterator();
        while (targetIterator.hasNext()) {
            Block block = targetIterator.next();
            int a = block.getPosition().get(0);
            int b = block.getPosition().get(1);
            // Blast range of 2 cells from the Bomb's edges.
            if ((i-2 <= a && a <= i+5) && (j-2 <= b && b <= j+5)) {
                block.hide();
                targetIterator.remove();
            }
        }
        // Destroying the Bomb.
        for (Block block : bomb) {
            block.hide();
            group.remove(block);
        }
    }

    /**
     * Destroys the targets hit by the last {@link Bomb} in the
     * {@link #bombs} static list.
     * 
     * @param target {@link Block} and {@link BlinkingBlock} objects
     *               to be destroyed.
     */
    public void explode(List<Block> target) {
        ArrayList<Block> bomb = bombs.get(bombs.size()-1);
        explode(bomb, target);
        bombs.remove(bomb);
    }

    // Testing Bomb display and movement.
    public static void main(String[] args) {
        class BombClient extends Client {
            Bomb bomb;
            ArrayList<Block> bombList = new ArrayList<Block>();
            BombClient() {
                super();
                bomb = new Bomb(new int[] {0, 0}, bombList);
            }

            @Override
            public void setLoop() {
                super.setLoop();
                for (Block block : bombList) {
                    if (block instanceof BlinkingBlock) {
                        block.getSprite().blink(ticks);
                    }
                }
                if (ticks % FPS == 0) {
                    bomb.move(Direction.DOWN);
                }
                canvas.repaint();
            }
        }

        new BombClient();
    }
}
