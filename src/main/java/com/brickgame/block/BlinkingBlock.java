package com.brickgame.block;

import java.util.ArrayList;
import static com.brickgame.Constants.*;
import com.brickgame.Client;

/** A {@link Block} with a name identifier. */
public class BlinkingBlock extends Block {
    public BlinkingBlock(int i, int j) {
        super(i, j);
    }
    public BlinkingBlock(int[] coords) {
        super(coords);
    }
    public BlinkingBlock(ArrayList<Integer> list) {
        super(list);
    }

    // Testing.
    public static void main(String[] args) {
        class BlinkClient extends Client {
            BlinkingBlock blink;
            BlinkingBlock blink1;
            @Override
            public void setup() {
                super.setup();
                this.blink = new BlinkingBlock(new int[] {0, 0});
                this.blink.setDirection(Direction.DOWN);
                this.blink1 = new BlinkingBlock(5, 0);
            }

            @Override
            public void setLoop() {
                super.setLoop();
                if (ticks % FPS == 0) {
                    blink.move();
                    blink1.setPosition(5, ticks/FPS);
                }
                blink.blink(ticks);
                blink1.blink(ticks);
                canvas.repaint();
            }
        }

        new BlinkClient();
    }
}
