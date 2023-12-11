package com.brickgame.tests;

import java.util.ArrayList;
import static com.brickgame.Constants.*;
import com.brickgame.Client;
import com.brickgame.block.*;

/** Testing the explosion mechanics. */
class testBomb extends Client {
    Bomb bomb;
    Bomb bomb1;
    Bomb bomb2;
    static ArrayList<Block> group = new ArrayList<>();
    static ArrayList<Block> target = new ArrayList<>();
    
    /** Spawning the necessary entities. */
    @Override
    public void setup() {
        super.setup();
        this.bomb = new Bomb(5, 5, group);
        this.bomb1 = new Bomb(5, 20, group);  // Spawns outside the grid.
        this.bomb2 = new Bomb(0, 10, group);
        target.add(new Block(5, 15));
        target.add(new Block(2, 17));    // Should not explode. 
        target.add(new Block(5, 17));
        target.add(new BlinkingBlock(9, 17));
    }

    @Override
    public void setLoop() {
        super.setLoop();
        // Making sure the BlinkingBlocks blink properly.
        for (Block block : group) {
            if (block instanceof BlinkingBlock) {
                block.blink(ticks);
            }
        }
        for (Block block : target) {
            if (block instanceof BlinkingBlock) {
                block.blink(ticks);
            }
        }
        // Every second, move all Bombs one cell up and check for any explosions.
        if (ticks % FPS == 0) {
            bomb.move(Direction.UP);
            bomb.checkExplosion(target);  // Also checks for bomb1 and bomb2.
        }
        // Explode bomb2 after 5 seconds.
        if (ticks == 5*FPS) {
            bomb2.explode(target);
        }
        // Update the canvas.
        canvas.repaint();
    }

    public static void main(String[] args) {
        new testBomb();
    }
}