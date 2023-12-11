package com.brickgame.games;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import com.google.gson.*;
import static com.brickgame.Constants.*;
import com.brickgame.block.*;
import com.brickgame.screens.*;

/**
 * Engine for pixel games in a 20x10 grid.
 * 
 * @author  <a href="https://github.com/marcantonio64/">marcantonio64</a>
 * @version "%I%, %G%"
 * @since   1.8
 */
public abstract class GameEngine {
    /**
     * Used to group all {@link Block} and {@link BlinkingBlock}
     * objects to be displayed.
     */
    // A LinkedHashMap is needed to preserve the order of addition.
    private static Map<String, Integer> scores = new LinkedHashMap<>(); 
    public String name;
    public boolean running;
    protected Map<String, List<Block>> entities = new HashMap<>();
    Background back;
    VictoryScreen vs;
    DefeatScreen ds;
    int score;
    int speed;
    boolean paused;
    private int highestScore;
    
    /** Loads the game screens. */
    public GameEngine() {
        this.name = getClass().getSimpleName();
        this.vs = new VictoryScreen();
        this.ds = new DefeatScreen();
        this.back = new Background();
    }

    /**
     * Initializes the entries for the {@link #entities} field.
     */
    protected void setEntities(String... entitiesList) {
        for (String entityName : entitiesList) {
            List<Block> entity = new ArrayList<>();
            entities.put(entityName, entity);
        }
    }

    /**
     * Initializes the instance variables and shows the
     * {@link Background}.
     */
    public void start() {
        this.score = 0;
        this.speed = 1;       // Unit cells per second.
        this.paused = false;
        this.running = true;  // Whether this game is active.
        back.show();
    }

    /** Removes all elements from the screen and start again. */
    public void reset() {
        running = false;
        // Clear the groups.
        for (Map.Entry<String, List<Block>> entry : entities.entrySet()) {
            List<Block> entity = entry.getValue();
            if (entity == null) {
                continue;
            }
            for (Block block : entity) {
                if (block == null) {
                    continue;
                }
                block.hide();
            }
            entity.clear();
        }
        start();
    }

    /**
     * Deals with user input during a game.
     * 
     * @param key     A {@link KeyEvent} identifier for a key.
     * @param pressed Whether {@code key} was pressed or released.
     */
    public void setKeyBindings(int key, boolean pressed) {
        if (pressed) {
            if (key == KeyEvent.VK_P) {
                // P pauses/unpauses the game.
                paused = !paused;
                if (paused) {
                    System.out.println("Game paused");
                } else {
                    System.out.println("Game unpaused");
                }
            } else if (key == KeyEvent.VK_ENTER) {
                // Enter resets the game.
                reset();
            }
        }
    }

    /**
     * Game logic implementation for endgame.
     * 
     * @param t A timer.
     */
    public void manage(int t) {
        if (running && !paused) {
            if (checkVictory()) {
                toggleVictory();
                System.out.println("Congratulations!");
                System.out.println("Your score on " + name + ": " + score);
            }
            if (checkDefeat()) {
                toggleDefeat();
                System.out.println("Better luck next time...");
                System.out.println("Your score on " + name + ": " + score);
            }
        }
    }

    /** Communicates with the {@literal HighScores.json} file. */
    void updateScore() {
        // Reading the highest scores.
        try (FileReader reader = new FileReader(HIGH_SCORES_DIR)) {
            JsonObject highScores = JsonParser.parseReader(reader).getAsJsonObject();
            // - from all games.
            for (Map.Entry<String, JsonElement> entry : highScores.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue().getAsInt();
                scores.put(key, value);
            }
            // - from the current game.
            this.highestScore = scores.get(name);
            // Close the file.
            reader.close();
        } catch (IOException e) {
            System.out.println("Failed to read HighScores.json.");
            e.printStackTrace();
            return;  // This interrupts the scoring system without stopping the game.
        }
        // Updating the highest score to the HighScores.json file.
        if (score > highestScore) {
            if (score >= 1e8) {
                score = (int)1e8 - 1;
            } else {
                highestScore = (int)score;
            }
            scores.put(name, highestScore);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(scores);
            try (FileWriter writer = new FileWriter(HIGH_SCORES_DIR)) {
                writer.write(jsonString);
            } catch (IOException e) {
                System.out.println("Failed to update 'HighScores.json'.");
                e.printStackTrace();
            }
        }
    }

    protected abstract boolean checkVictory();
    protected abstract boolean checkDefeat();

    /**
     * Removes all elements from the screen and shows the
     * {@link VictoryScreen}.
     */
    public void toggleVictory() {
        running = false;
        // Cleaning the groups.
        for (Map.Entry<String, List<Block>> entry : entities.entrySet()) {
            entry.getValue().clear();
        }
        // Showing the victory message.
        vs.show();
    }

    /**
     * Removes all elements from the screen and shows the
     * {@link DefeatScreen}.
     */
    public void toggleDefeat() {
        running = false;
        // Cleaning the groups.
        for (Map.Entry<String, List<Block>> entry : entities.entrySet()) {
            entry.getValue().clear();
        }
        // Showing the defeat message.
        ds.show();
    }

    public boolean isPaused() {
        return paused;
    }

    /**
     * Handles the blinking of {@link BlinkingBlock} objects.
     * 
     * @param t A timer.
     */
    public void update(int t) {
        if (!paused) {
            if (entities == null) {
                return;
            }
            for (Map.Entry<String, List<Block>> entry : entities.entrySet()) {
                for (Block entity : entry.getValue()) {
                    if (entity instanceof BlinkingBlock) {
                        entity.blink(t);
                    }
                }
            }
        }
    }
    
    /** Draws all the sprites to the screen. */
    public void drawEntities(boolean forceDraw) {
        if (running && forceDraw) {
            // Showing the Background (to clear previous drawings).
            back.show();
            // Drawing the current objects to the screen.
            if (entities == null) {
                return;
            }
            for (Map.Entry<String, List<Block>> entry : entities.entrySet()) {
                for (Block entity : entry.getValue()) {
                    if (entity instanceof BlinkingBlock) {
                        entity.show();
                    } else if (!(entity instanceof HiddenBlock)) {
                        entity.show();
                    }
                }
            }
        }
    }

    /** Draws all the sprites to the screen if the game isn't paused. */
    public void drawEntities() {
        drawEntities(!paused);
    }
}
