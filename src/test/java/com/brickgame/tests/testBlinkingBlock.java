package com.brickgame.tests;

import java.util.Random;
import static com.brickgame.Constants.*;
import com.brickgame.Client;
import com.brickgame.block.BlinkingBlock;

/**
 * Tests of spawning for the
 * {@link com.brickgame.games.Snake Snake} game.
 */
class testBlinkingBlock extends Client {
    BlinkingBlock blink;
    static int[] coordinates = new int[2];
    static Random random = new Random();
    testBlinkingBlock() {
        super();
        window.setTitle("BlinkingBlock test");
    }

    @Override
    public void setup() {
        super.setup();
        coordinates[0] = random.nextInt(10);
        coordinates[1] = random.nextInt(20);
        this.blink = new BlinkingBlock(coordinates);
    }

    /**
     * Draws the {@link BlinkingBlock} at a new random position in
     * the grid.
     */ 
    void respawn() {
        coordinates[0] = random.nextInt(10);
        coordinates[1] = random.nextInt(20);
        blink.setPosition(coordinates[0], coordinates[1]);
    }

    @Override
    public void setLoop() {
        super.setLoop();
        blink.blink(ticks);
        if (ticks % FPS == 0) {
            //System.out.println("++");
            respawn();
        }
        // Updating the canvas.
        canvas.repaint();
    }

    public static void main(String[] args) {
        new testBlinkingBlock();
    }
}
