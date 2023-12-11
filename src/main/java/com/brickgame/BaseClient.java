package com.brickgame;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import static com.brickgame.Constants.*;

/** Establishes the basic structure of the client. */
public class BaseClient {
    public static JFrame window;
    public static JPanel canvas;
    public int ticks = 0;
    private Timer timer;
    private int delay;
    private long lastTime;
    private long currTime;

    /**
     * A constructor that creates a GUI, reads user input, and sets a
     * loop.
     */
    public BaseClient() {
        // Creating the main window.
        window = new JFrame();
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setPreferredSize(WINDOW_DIMENSIONS);
        window.setAutoRequestFocus(true);
        
        // Creating the canvas.
        canvas = new JPanel(null);
        canvas.setSize(WINDOW_DIMENSIONS);
        canvas.setBorder(new LineBorder(LINE_COLOR, BORDER_WIDTH));
        canvas.setBackground(BACK_COLOR);

        // Adding the canvas to the window.
        window.getContentPane().add(canvas);
        window.pack();
        window.setComponentZOrder(canvas, 0);
        window.setVisible(true);
        
        setup();

        SwingUtilities.invokeLater(() -> {
            // Handling events.
            window.addKeyListener(new KeyBindings());
            // Setting the main loop.
            this.ticks = 0;
            this.delay = TICK_RATE;
                this.timer = new Timer(this.delay, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setLoop();
                    }
                });
                this.currTime = System.nanoTime();
                this.timer.start();
        });
    }

    /** Adding functionalities to {@link #canvas}. */
    public void setup(){} 

    /** Schedules looping events. */
    public void setLoop() {
        ticks++;
        lastTime = currTime;
        currTime = System.nanoTime();
        // Removing the extra time since the last loop from the reference.
        int delayError = (int)Math.round((double)(currTime - lastTime)/1e6 - TICK_RATE);
        delay = (delayError < delay) ? (delay - delayError) : (TICK_RATE - 15); 
        //System.out.println(ticks+0+"|"+(double)(currTime - lastTime)/1e6+"|"+TICK_RATE);
        timer.setDelay(delay);
    }

    /**
     * Deals with user input.
     * 
     * @param key     A {@link KeyEvent} identifier for a key.
     * @param pressed Whether {@code key} was pressed or released.
     */
    public void setKeyBindings(int key, boolean pressed) {}

    /** Sets up the structure for key events. */
    private class KeyBindings extends KeyAdapter {
        /**
         * Manages keys being pressed.
         * <p>
         * Converts a {@link KeyEvent} to an ID, binds pressing
         * {@literal ESC} to exit, and shifts to
         * {@code setKeyBindings()} upon key press.
         * 
         * @param e User input event.
         */
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            setKeyBindings(key, true);
            }

        /**
         * Manages keys being released.
         * <p>
         * Converts a {@link KeyEvent} to an ID, and shifts to
         * {@code setKeyBindings()} upon key release.
         * 
         * @param e User input.
         */
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            setKeyBindings(key, false);
            }
    }

    // Testing.
    public static void main(String[] args) {
        new BaseClient();
    }
}
