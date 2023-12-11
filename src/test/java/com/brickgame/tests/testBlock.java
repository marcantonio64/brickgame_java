package com.brickgame.tests;

import java.util.ArrayList;
import static com.brickgame.Constants.*;
import com.brickgame.Client;
import com.brickgame.block.Block;

/**
 * Tests of controlled movement for the
 * {@link com.brickgame.games.Snake Snake} game.
 */
class testBlock extends Client {
    ArrayList<int[]> bodyCoords = new ArrayList<int[]>();
    ArrayList<Block> group = new ArrayList<Block>();
    testBlock() {
        super();
        window.setTitle("Block test");
        this.bodyCoords.add(new int[] {0, 0});
        this.bodyCoords.add(new int[] {0, 1});
        for (int[] coordinates : bodyCoords) { 
            Block block = new Block(coordinates);
            this.group.add(block);
        }
    }

    @Override
    public void setLoop() {
        super.setLoop();
        int n = group.size();
        // 3 actions per second.
        if (ticks % ((int)(FPS/3)) == 0) {
            // Coordinates of the last Block in bodyCoords.
            int[] coords = bodyCoords.get(n-1);
            int[] ij = CONVERT.get(Direction.DOWN);
            int i = ij[0];
            int j = ij[1];
            coords[0] += i;
            coords[1] += j;
            // Creating a third one according to Direction.DOWN.
            bodyCoords.add(coords);
            Block block = new Block(coords);
            group.add(block);
            // Erasing the first Block, creating an illusion of movement.
            group.get(0).hide();
            bodyCoords.remove(0);
            group.remove(0);
            // Updating the canvas.
            canvas.repaint();
        }
    }

    public static void main(String[] args) {
        new testBlock();
    }
}
