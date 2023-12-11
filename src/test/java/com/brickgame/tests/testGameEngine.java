package com.brickgame.tests;

import java.util.Random;
import static com.brickgame.Constants.*;
import com.brickgame.Client;
import com.brickgame.Constants.Direction;
import com.brickgame.block.*;
import com.brickgame.games.GameEngine;

public class testGameEngine {
    static class NewGame extends GameEngine {
        NewGame() {
            super();
            // Setting containers for the entities.
            setEntities("block", "food", "bomb");
        }
        @Override
        public void start() {
            super.start();
            // Spawning a Block at {3, 3} that moves down.
            Block block = new Block(3, 3);
            block.setDirection(Direction.DOWN);
            entities.get("block").add(block);
            // Spawning a BlinkingBlock at a random position in the grid.
            Random random = new Random();
            int i = random.nextInt(10);
            int j = random.nextInt(20);
            entities.get("food").add(new BlinkingBlock(i, j));
            // Spawning a Bomb at {5, 10}.
            new Bomb(5, 10, entities.get("bomb"));
        }
        @Override
        public boolean checkVictory() {
            return false;
        }
        @Override
        public boolean checkDefeat() {
            return false;
        }
    }

    public static void main(String[] args) {
        class NewGameClient extends Client {
            NewGame game;
            NewGameClient() {
                super();  // <-- setup() and setLoop() are invoked here.
                window.setTitle("Game test");
            }
            /** Initializes the game. */
            @Override
            public void setup() {
                super.setup();
                this.game = new NewGame();
                this.game.start();
            }
            /** List of scheduled events. */
            @Override
            public void setLoop() {
                //System.out.println(game+", "+0);
                super.setLoop();
                // Implementing the game mechanics and check for endgame.
                game.manage(ticks);
                // Drawing the game's objects to the screen.
                game.drawEntities();
                // Updating BlinkingBlock'ss mechanics.
                game.update(ticks);
                // Testing the game's methods.
                if (ticks == 10*FPS) {  // 10 seconds mark.
                    game.toggleDefeat();
                } else if (ticks == 15*FPS) {  // 15 seconds mark.
                    game.toggleVictory();
                } else if (ticks == 20*FPS) {  // 20 seconds mark.
                    game.reset();
                }
                canvas.repaint();
            }

            /**
             * Deals with user input during a game.
             * 
             * @param key     A {@link KeyEvent} identifier for a key.
             * @param pressed Whether {@code key} was pressed or released.
             */
            @Override
            public void setKeyBindings(int key, boolean pressed) {
                super.setKeyBindings(key, pressed);
                game.setKeyBindings(key, pressed);
            }
        }

        new NewGameClient();
    }
}
