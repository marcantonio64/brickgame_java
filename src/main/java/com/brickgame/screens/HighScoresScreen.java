package com.brickgame.screens;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.google.gson.*;
import static com.brickgame.Constants.*;
import com.brickgame.BaseClient;

/** Reads, formats, and shows all entries from {@literal HighScores.json}. */
public class HighScoresScreen extends JPanel {
    private Map<String, Integer> scores = new LinkedHashMap<String, Integer>();
    /**
     * A constructor for a {@link JPanel} with all the necessary
     * information, which is then shown at the main
     * {@link BaseClient#canvas}.
     */
    public HighScoresScreen() {
        super();
        setLayout(null);
        setSize(WINDOW_DIMENSIONS);
        setBorder(new LineBorder(LINE_COLOR, 3));
        setBackground(BACK_COLOR);
        readHighScores();
        drawText();
        BaseClient.canvas.add(this);
        BaseClient.canvas.setComponentZOrder(this, 0);
    }

    /** Shows the {@link HighScoresScreen} on the {@link BaseClient#canvas}. */
    @Override
    public void show() {
        BaseClient.canvas.setComponentZOrder(this, 0);
    }

    /**
     * Parses through {@literal HighScores.json} to collect all the
     * necessary data into the {@link #scores scores} field. */
    private void readHighScores() {
        try (FileReader reader = new FileReader(HIGH_SCORES_DIR)) {
            JsonObject highScores = JsonParser.parseReader(reader).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : highScores.entrySet()) {
                String game = entry.getKey();
                int gameScore = entry.getValue().getAsInt();
                scores.put(game, gameScore);
                //System.out.println(game+": "+gameScore);
            }
        } catch (IOException e) {
            System.out.println("Failed to read 'HighScores.json'.");
            e.printStackTrace();
            return;  // This interrupts the scoring system without stopping the game.
        }
    }

    /**
     * Creates a {@link JLabel} with formatted data to be shown on
     * the screen.
     */
    private void drawText() {
        int vPad = 2*DIST_BLOCKS;
        // Writting a title.
        JLabel title = new JLabel("HIGH SCORES");
        title.setForeground(LINE_COLOR);
        title.setFont(new Font("Impact", Font.PLAIN, 10*PIXEL_SIDE));
        // Placing at top center.
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0,
                        DIST_BLOCKS,
                        WINDOW_WIDTH,
                        DIST_BLOCKS);
        // Writting the data.
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String game = entry.getKey();
            int gameScore = entry.getValue();
            // System.out.println(game +": "+gameScore);
            vPad += 2*DIST_BLOCKS;
            // Left collumn: game names.
            JLabel gameLabel = new JLabel(game);
            gameLabel.setForeground(LINE_COLOR);
            gameLabel.setFont(new Font("Impact", Font.PLAIN, 10*PIXEL_SIDE));
            gameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            gameLabel.setBounds(BORDER_WIDTH + DIST_BLOCKS/2,
                                BORDER_WIDTH + vPad,
                                WINDOW_WIDTH/2 - DIST_BLOCKS,
                                DIST_BLOCKS);
            // Second collumn: game scores.
            JLabel scoreLabel = new JLabel(String.format("%07d", gameScore));
            scoreLabel.setForeground(LINE_COLOR);
            scoreLabel.setFont(new Font("Inconsolata", Font.BOLD, 10*PIXEL_SIDE));
            scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            scoreLabel.setBounds(WINDOW_WIDTH/2 + DIST_BLOCKS/2 - BORDER_WIDTH,
                                 BORDER_WIDTH + vPad,
                                 WINDOW_WIDTH/2 - DIST_BLOCKS,
                                 DIST_BLOCKS);
            // Adding to the screen.
            add(gameLabel);
            add(scoreLabel);
            setComponentZOrder(gameLabel, 0);
            setComponentZOrder(scoreLabel, 0);
        add(title);
        setComponentZOrder(title, 0);
        }
    }

    public static void main(String[] args) {
        new BaseClient();
        new HighScoresScreen();
        BaseClient.window.setTitle("High Scores");
        BaseClient.canvas.repaint();
    }
}
