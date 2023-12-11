package com.brickgame;

import java.util.ArrayList;
import java.util.HashMap;
import com.brickgame.screens.Sprite;
import static com.brickgame.screens.Sprite.createPosition; 
import com.brickgame.screens.Background;

/** Complete client with layered {@link Sprite}s at fixed positions. */
public class Client extends BaseClient {
    public static HashMap<ArrayList<Integer>, Sprite> spriteMap = new HashMap<>(200);  
    public static Background back;
    @Override
    public void setup() {
        // Avoiding flickering.
        canvas.setVisible(false);
        // Layering all the Sprite objects to be used.
        spriteMap.put(null, null);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                spriteMap.put(createPosition(i, j), new Sprite(i, j));
            }
        }
        // Covering with the Background.
        back = new Background();
        canvas.setVisible(true);
    }

    // Testing.
    public static void main(String[] args) {
        new Client();
        canvas.repaint();
    }
}
