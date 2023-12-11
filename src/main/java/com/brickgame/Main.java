package com.brickgame;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import com.google.gson.*;
import static com.brickgame.Constants.*;
import com.brickgame.screens.*;
import com.brickgame.games.*;

/** Entry point for the execution of the main package. */
public class Main {
    /**
     * Manages all the client and game mechanics and their interactions.
     * <p>
     * Read the <a href="{@docRoot}/docs/GameManuals.md">Game Manuals</a>
     * and the <a href="{@docRoot}/docs/UserGuide.md">User Guide</a>.
     * @author  <a href="https://github.com/marcantonio64/">marcantonio64</a>
     * @version "%I%, %G%"
     * @since   1.8
     */
    public static class BrickGame extends Client {
        private static enum E {SELECTOR, GAME}
        private static E environment = E.SELECTOR;
        private static GameEngine game;
        private Selector selector;

        /** Initializing the GUI. */
        BrickGame() {
            super();  // setup() and setLoop() are invoked here.
            window.setTitle("Game Selection");
        }

        /** Generating the HighScores.json file, if it doesn't already exist. */
        private static void createHighScores() {
            File file = new File(HIGH_SCORES_DIR);
            try {
                if (file.createNewFile()) {
                    Map<String, Integer> highScores = new LinkedHashMap<>();
                    highScores.put("Snake", 0);
                    highScores.put("Breakout", 0);
                    highScores.put("Asteroids", 0);
                    highScores.put("Tetris", 0);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonString = gson.toJson(highScores);
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(jsonString);
                        String message = ""
                            + "File HighScores.json successfully created at "
                            + HIGH_SCORES_DIR;
                        System.out.println(message);
                    } catch (IOException e) {
                        String message = ""
                            + "Failed to generate the HighScores.json file";
                        System.out.println(message);
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {}
        }

        /** Manages all the console and game mechanics and their interactions. */
        @Override
        public void setup() {
            super.setup();

            createHighScores();

            // Render the environments.
            this.selector = new Selector();

            // Covering everything with the Background.
            back.show();
        }

        /** List of scheduled events. */
        @Override
        public void setLoop() {
            super.setLoop();
            switch (environment) {
                case SELECTOR:
                    selector.animateScreen();
                    break;
                case GAME:
                    // Implementing the game mechanics and check for endgame.
                    game.manage(ticks);
                    // Drawing the game objects to the screen.
                    game.drawEntities();
                    // Updating BlinkingBlocks mechanics.
                    game.update(ticks);
                    break;
                default:
                    break;
            }
            canvas.repaint();
        }

        @Override
        public void setKeyBindings(int key, boolean pressed) {
            super.setKeyBindings(key, pressed);
            switch (environment) {
                case SELECTOR:
                    // Shifting to selector keybindings.
                    selector.setKeyBindings(key, pressed);
                    break;
                case GAME:
                    // Leaving the game instance if *Backspace* is pressed.
                    if (pressed && key == KeyEvent.VK_BACK_SPACE) {
                        environment = E.SELECTOR;
                        window.setTitle("Game Selection");
                    }
                    else {  // Shifting to the game keybindings otherwise.
                        game.setKeyBindings(key, pressed);
                    }
                    break;
                default:;
            }
        }

        /** Mechanics for game selection, previews, and high scores. */
        private class Selector {
            private HashMap<Integer, GameEngine> gameSelect = new HashMap<>();
            private HashMap<String, Background> previews = new HashMap<>();
            private int numberOfGames;
            private int stageID;
            private String gameName;

            /** Instantiates all the game classes and loads previews. */
            Selector() {
                // The game timers start only when selected.
                this.gameSelect.put(1, new Snake());
                this.gameSelect.put(2, new Breakout());
                this.gameSelect.put(3, new Asteroids());
                this.gameSelect.put(4, new Tetris());
                
                // Loading preview screens.
                this.previews.put("Snake1", (GamePreviews.Snake1)new GamePreviews.Snake1());
                this.previews.put("Snake2", (GamePreviews.Snake2)new GamePreviews.Snake2());
                this.previews.put("Snake3", (GamePreviews.Snake3)new GamePreviews.Snake3());
                this.previews.put("Breakout1", (GamePreviews.Breakout1)new GamePreviews.Breakout1());
                this.previews.put("Breakout2", (GamePreviews.Breakout2)new GamePreviews.Breakout2());
                this.previews.put("Breakout3", (GamePreviews.Breakout3)new GamePreviews.Breakout3());
                this.previews.put("Asteroids1", (GamePreviews.Asteroids1)new GamePreviews.Asteroids1());
                this.previews.put("Asteroids2", (GamePreviews.Asteroids2)new GamePreviews.Asteroids2());
                this.previews.put("Asteroids3", (GamePreviews.Asteroids3)new GamePreviews.Asteroids3());
                this.previews.put("Tetris1", (GamePreviews.Tetris1)new GamePreviews.Tetris1());
                this.previews.put("Tetris2", (GamePreviews.Tetris2)new GamePreviews.Tetris2());
                this.previews.put("Tetris3", (GamePreviews.Tetris3)new GamePreviews.Tetris3());

                // Setting instance fields.
                this.numberOfGames = gameSelect.size();
                this.stageID = 1;  // Show Snake first.
                this.gameName = this.gameSelect.get(this.stageID).name;
            }

            /**
             * Deals with user input.
             * 
             * @param key     A {@link KeyEvent} identifier for a key.
             * @param pressed Whether {@code key} was pressed or released.
             */
            void setKeyBindings(int key, boolean pressed) {
                if (pressed) {  // Key press.
                    if (key == KeyEvent.VK_ENTER) {
                        // Entering a game.
                        if (stageID <= numberOfGames) {
                            environment = E.GAME;
                            game = gameSelect.get(stageID);
                            /* Updating the window title and informing
                             * of the game change. */
                            window.setTitle(game.name);
                            System.out.println("Now playing: " + game.name);
                            /* Avoiding the need to press Enter twice
                             * after endgames. */
                            if (!game.running) {
                                game.reset();
                            }
                            if (game.isPaused()) {
                                game.drawEntities(true);
                            }
                        }
                    }  // Choosing a game.
                    else if (key == KeyEvent.VK_LEFT) {
                        if (stageID > 1) {
                            if (stageID > numberOfGames) {
                                window.setTitle("Game Selection");
                            }
                            stageID -= 1;
                        } else {
                            stageID = numberOfGames + 1;
                            new HighScoresScreen();
                            window.setTitle("High Scores");
                        }
                    }
                    else if (key == KeyEvent.VK_RIGHT) {
                        if (stageID <= numberOfGames) {
                            stageID += 1;
                            if (stageID > numberOfGames) {
                                new HighScoresScreen();
                                window.setTitle("High Scores");
                            }
                        } else {
                            stageID = 1;
                            window.setTitle("Game Selection");
                        }
                    }
                }
                canvas.repaint();
            }

            /**
             * Toggle game previews.
             * <p>
             * Iterates through each game's {@link GamePreviews preview},
             * with 3 frames per second. The previews were built as
             * pixel images.
             */
            void animateScreen() {
                if (stageID <= numberOfGames) {
                    gameName = gameSelect.get(stageID).name;
                    switch (ticks % FPS) {
                        case 0:  // 0.000s
                            previews.get(gameName + 1).show();
                            break;
                        case FPS/3:  // 0.333s
                            previews.get(gameName + 2).show();
                            break;
                        case 2*FPS/3:  // 0.666s
                            previews.get(gameName + 3).show();
                            break;
                        default:;
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Check docs/GameManuals.md for instructions.");
        new BrickGame();
    }
}