package com.brickgame.screens;

import java.util.Map;
import java.util.HashMap;
import com.brickgame.BaseClient;

/** A {@literal Game Over} message. */
public class DefeatScreen extends Background {
    private HashMap<Integer, int[]> sketch;
    /** Draws the pixel message with {@link Sprite}s in a 20x10 grid. */
    @Override
    void draw() {
        super.draw();
        sketch = new HashMap<Integer, int[]>();
        sketch.put(0,  new int[] {   1, 2, 3, 4,       7, 8, 9});
        sketch.put(1,  new int[] {0,                   7,    9});
        sketch.put(2,  new int[] {0,    2, 3, 4,       7,    9});
        sketch.put(3,  new int[] {0,          4,       7, 8, 9});
        sketch.put(4,  new int[] {   1, 2, 3                  });
        sketch.put(5,  new int[] {                     7,    9});
        sketch.put(6,  new int[] {   1, 2, 3,          7,    9});
        sketch.put(7,  new int[] {0,          4,       7,    9});
        sketch.put(8,  new int[] {0, 1, 2, 3, 4,          8   });
        sketch.put(9,  new int[] {0,          4               });
        sketch.put(10, new int[] {                     7, 8, 9});
        sketch.put(11, new int[] {0,          4,       7      });
        sketch.put(12, new int[] {0, 1,    3, 4,       7, 8, 9});
        sketch.put(13, new int[] {0,    2,    4,       7      });
        sketch.put(14, new int[] {                     7, 8, 9});
        sketch.put(15, new int[] {   1, 2, 3                  });
        sketch.put(16, new int[] {   1,                7, 8   });
        sketch.put(17, new int[] {   1, 2, 3,          7,    9});
        sketch.put(18, new int[] {   1,                7, 8   });
        sketch.put(19, new int[] {   1, 2, 3,          7,    9});

        for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
            int j = entry.getKey();
            for (int i : entry.getValue()) {
                new Sprite(i, j, this);
            }
        }
    }

    public static void main(String[] args) {
        new BaseClient();
        new DefeatScreen();
        BaseClient.canvas.repaint();
        BaseClient.window.setTitle("Defeat Screen");
    }
}
