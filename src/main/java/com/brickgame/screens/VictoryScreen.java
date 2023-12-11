package com.brickgame.screens;

import java.util.HashMap;
import java.util.Map;
import com.brickgame.BaseClient;

/** A {@literal You Win} message. */
public class VictoryScreen extends Background {
    private HashMap<Integer, int[]> sketch;
    /** Draws the pixel message with {@link Sprite}s in a 20x10 grid. */
    @Override
    void draw() {
        super.draw();
        sketch = new HashMap<Integer, int[]>();
        sketch.put(1,  new int[] {0,    2,       5,          9});
        sketch.put(2,  new int[] {0,    2,       5,          9});
        sketch.put(3,  new int[] {0, 1, 2,       5,    7,    9});
        sketch.put(4,  new int[] {      2,       5, 6, 7, 8, 9});
        sketch.put(5,  new int[] {0, 1, 2,       5, 6,    8, 9});
        
        sketch.put(5,  new int[] {0, 1, 2,       5, 6,    8, 9});
        sketch.put(7,  new int[] {0, 1, 2,          6, 7, 8   });
        sketch.put(8,  new int[] {0,    2,             7      });
        sketch.put(9,  new int[] {0,    2,             7      });
        sketch.put(10, new int[] {0, 1, 2,          6, 7, 8   });
        
        sketch.put(12, new int[] {0,    2,       5, 6,       9});
        sketch.put(13, new int[] {0,    2,       5, 6, 7,    9});
        sketch.put(14, new int[] {0,    2,       5,    7, 8, 9});
        sketch.put(15, new int[] {0, 1, 2,       5,       8, 9});

        for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
            int j = entry.getKey();
            for (int i : entry.getValue()) {
                new Sprite(i, j, this);
            }
        }
    }

    public static void main(String[] args) {
        new BaseClient();
        new VictoryScreen();
        BaseClient.canvas.repaint();
        BaseClient.window.setTitle("Victory Screen");
    }
}
