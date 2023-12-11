package com.brickgame.screens;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import com.brickgame.Constants;
import static com.brickgame.Constants.*;
import com.brickgame.BaseClient;

/** The background image for the Brick Game's client. */
public class Background extends JPanel {
    public Background() {
        // Creating a new canvas.
        super();
        setLayout(null);
        setSize(WINDOW_DIMENSIONS);
        setBorder(new LineBorder(LINE_COLOR, BORDER_WIDTH));
        setBackground(BACK_COLOR);
        // Adding the background canvas to the main canvas.
        BaseClient.canvas.add(this);
        show();
        draw();
    }

    /** Shows the {@link Background} on the {@link BaseClient#canvas}. */
    @Override
    public void show() {
        BaseClient.canvas.setComponentZOrder(this, 0);
    }

    /**
     * Draws a 20x10 grid of {@link Sprite}s colored
     * {@link Constants#SHADE_COLOR SHADE_COLOR}.
     */
    void draw() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                new Sprite(i, j, SHADE_COLOR, this);
            }
        }
    }

    /**
     * Testing {@link Sprite} display on top of {@link Background}.
     */
    public static void main(String[] args) {
        class BackgroundClient extends BaseClient {
            Sprite blinkingSprite = new Sprite(9, 9);
            BackgroundClient() {
                super();
                new Background();
                new Sprite(0, 0);
            }
            
            @Override
            public void setLoop() {
                super.setLoop();
                blinkingSprite.blink(ticks);
                canvas.repaint();
            }
        }
        new BackgroundClient();
    }
}
