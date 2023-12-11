package com.brickgame.tests;

import java.awt.*;
import javax.swing.*;
import static com.brickgame.Constants.*;
import com.brickgame.BaseClient;

/** Tests for some of {@link BaseClient} functionalities. */
class testClient {

    /** A (theoretically) functioning client: plain. */
    static class Client1 extends BaseClient {
        Client1() {
            super();
        }
    }

    /** A (theoretically) functioning client: title and background. */
    static class Client2 extends BaseClient {
        Client2() {
            super();
            window.setTitle("Title test");
            window.getContentPane().setBackground(BACK_COLOR);
        }
    } 

    /** A (theoretically) functioning client: drawing squares. */
    static class Client3 extends BaseClient {
        int j;
        Sprite sprite;

        Client3() {
            super();
            // Creating the first square.
            this.sprite = draw_square(0, 0);
            // Drawing it to the canvas.
            canvas.add(this.sprite);
            canvas.repaint();
        }
        
        @Override
        public void setLoop() {
            super.setLoop();
            if (ticks % FPS == 0) {  // Every second, ...
                // Draw another square below the previous ones.
                sprite = draw_square(0, ticks/FPS);
                canvas.add(sprite);
                canvas.repaint();
            }
        }

        /**
         * Draws one square within another and adjusts their positions
         * for a 20x10 grid.
         * 
         * @param i Horizontal position. Must be from {@literal 0}
         *          (inclusive) to {@literal 10} (exclusive) to
         *          show on the screen.
         * @param j Vertical position. Must be from {@literal 0}
         *          (inclusive) to {@literal 20} (exclusive) to
         *          show on the screen.
         * @return  An identifier for manipulation.
         */
        Sprite draw_square(int i, int j) {
            Sprite sprite = new Sprite();
            sprite.setBounds(BORDER_WIDTH + i*DIST_BLOCKS,
                             BORDER_WIDTH + j*DIST_BLOCKS,
                             BLOCK_SIDE,
                             BLOCK_SIDE);
            sprite.setBackground(LINE_COLOR);
            return sprite;
        }

        /** Draws a shape to a {@link JPanel}. */
        class Sprite extends JPanel{
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int side = 7*PIXEL_SIDE;
                // Convert Graphics to Graphics2D.
                Graphics2D g2 = (Graphics2D)g;
                // Set outline color.
                g2.setColor(BACK_COLOR);
                // Set outline width (stroke width).
                g2.setStroke(new BasicStroke(PIXEL_SIDE));
                // Draw the outline.
                g2.drawRect(PIXEL_SIDE + 1, PIXEL_SIDE + 1, side, side);
            }
        }
    }

    /** Moves a square. */
    static class Client4 extends Client3 {
        public void setLoop() {
            if (ticks % FPS == 0) {  // Every second, ...
                // Clearing the previous square.
                canvas.remove(sprite);
            }
            super.setLoop();  // Drawing a falling square.
        }
    }

    /** Loads a class's instance from its name. */
    static void dynamicLoad(String className) {
        try {
            // Dynamically load the class.
            Class<?> dynamicClass = Class.forName(className);
            // Create an instance of the dynamically loaded class.
            dynamicClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            System.out.println("Error loading or instantiating class: " + className);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            String className = testClient.class.getName() + "$Client" + args[0];
            dynamicLoad(className);
        } else {
            System.out.println("Please provide an argument to specify the class,"
                               +" for example: java com.brickgame.testClient 1");
        }
    }
}
