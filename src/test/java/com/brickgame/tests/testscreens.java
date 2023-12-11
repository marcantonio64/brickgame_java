package com.brickgame.tests;

import static com.brickgame.Constants.*;
import com.brickgame.BaseClient;
import com.brickgame.screens.*;

/** Tests for screens display. */
class testscreens extends BaseClient {
    testscreens() {
        super();
        window.setTitle("screens test");
        new Background();
        canvas.repaint();
    }

    /** Shifts through the victory and defeat messages. */
    @Override
    public void setLoop() {
        super.setLoop();
        if (ticks % (20*FPS) == 5*FPS) {
            new DefeatScreen();
            canvas.repaint();
        } else if (ticks % (20*FPS) == 10*FPS) {
            new VictoryScreen();
            canvas.repaint();
        } else if (ticks % (20*FPS) == 0) {
            new Background();
            canvas.repaint();
        }
    }
    public static void main(String[] args) {
        new testscreens();
    }
}