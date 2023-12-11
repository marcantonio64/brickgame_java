package com.brickgame.screens;

import java.util.ArrayList;
import java.awt.*;
import javax.swing.JPanel;
import com.brickgame.BaseClient;
import com.brickgame.Constants;
import static com.brickgame.Constants.*;

/** Defines the appearance and the positioning of unit cells. */
public class Sprite extends JPanel {
    public boolean active;
    private ArrayList<Integer> coordinates;
    private JPanel canvas;

    /**
     * A constructor with all parameters.
     * 
     * @param i      Horizontal position. Must be from {@literal 0}
     *               (inclusive) to {@literal 10} (exclusive) to
     *               show on the screen.
     * @param j      Vertical position. Must be from {@literal 0}
     *               (inclusive) to {@literal 20} (exclusive) to
     *               show on the screen.
     * @param color  {@link Color} of the {@link Sprite} lines.
     * @param canvas Where to draw the {@code Sprite} on.
     */
    public Sprite(int i, int j, Color color, JPanel canvas) {
        super();
        setBackground(color);
        this.coordinates = createPosition(i, j);
        this.canvas = canvas;
        this.active = true;
        place();
    }

    /**
     * A constructor without the {@code color} and {@code sourceFile}
     * parameters.
     * <p>
     * {@code color} is standardized to the constant
     * {@link Constants#LINE_COLOR LINE_COLOR}.
     * 
     * @param i      Horizontal position. Must be from {@literal 0}
     *               (inclusive) to {@literal 10} (exclusive) to
     *               show on the screen.
     * @param j      Vertical position. Must be from {@literal 0}
     *               (inclusive) to {@literal 20} (exclusive) to
     *               show on the screen.
     * @param canvas Where to draw the {@code Sprite} on.
     */
    public Sprite(int i, int j, JPanel canvas) {
        this(i, j, LINE_COLOR, canvas);
    }

    /**
     * A constructor with only raw coordinates.
     * <p>
     * {@code color} is standardized to the constant
     * {@link Constants#LINE_COLOR}.
     * <p>
     * {@code canvas} is standardized to the static variable
     * {@link BaseClient#canvas}.
     * 
     * @param i Horizontal position. Must be from {@literal 0}
     *          (inclusive) to {@literal 10} (exclusive) to
     *          show on the screen.
     * @param j Vertical position. Must be from {@literal 0}
     *          (inclusive) to {@literal 20} (exclusive) to
     *          show on the screen.
     */
    public Sprite(int i, int j) {
        this(i, j, LINE_COLOR, BaseClient.canvas);
    }

    /** Makes creating {@link ArrayList} pairs easier. */
    public static ArrayList<Integer> createPosition(int i, int j) {
        ArrayList<Integer> pos = new ArrayList<>(2);
        pos.add(i); pos.add(j);
        return pos;
    }
    public static ArrayList<Integer> createPosition(int[] pair) {
        if (pair.length == 2) {
            return createPosition(pair[0], pair[1]);
        } else {
            return null;
        }
    }

    /** Draws a rectangle with a stroke. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int side = 7*PIXEL_SIDE;
        Graphics2D g2 = (Graphics2D)g;
        // Setting the outline color.
        g2.setColor(BACK_COLOR);
        // Setting outline width (stroke width).
        g2.setStroke(new BasicStroke(PIXEL_SIDE));
        // Drawing the outline (withhaving added a correction term).
        g2.drawRect(PIXEL_SIDE+PIXEL_SIDE/2, PIXEL_SIDE+PIXEL_SIDE/2, side, side);
        // Freeing the memory.
        g2.dispose();
    }

    /**
     * {@link Sprite} display.
     * <p>
     * Adjusts and shows the {@code Sprite} in the 20x10 grid at a
     * fixed position.
     */
    void place() {
        // Setting Sprite dimensions.
        setBounds(BORDER_WIDTH + coordinates.get(0)*DIST_BLOCKS,
                  BORDER_WIDTH + coordinates.get(1)*DIST_BLOCKS,
                  BLOCK_SIDE,
                  BLOCK_SIDE);
        // Adding the Sprite to canvas.
        canvas.add(this);
        show();
    }

    public ArrayList<Integer> getPosition() {
        return coordinates;
    }

    public void show() {
        canvas.setComponentZOrder(this, 0);
    }

    public void hide() {
        canvas.setComponentZOrder(this, canvas.getComponentCount()-1);
    }

    public void blink(int t) {
        if (t % FPS < FPS/2) {
            show();
        } else {
            hide();
        }
    }

    public static void main(String[] args) {
        new BaseClient() {
            @Override
            public void setup() {
                new Sprite(3, 3);
                canvas.repaint();
            }
        };
    }
}