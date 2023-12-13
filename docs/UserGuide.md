# Project User Guide

Instructions on how to expand the project.

## Adding more games/updating

New game modules should be in `...\brickgame_java\src\main\java\com\brickgame\games`

The general rules for consistency are:

### Import statements
The general game structure is defined at 
`...\brickgame_java\src\main\java\com\brickgame\games\GameEngine.java`

For a new game, the standard heading is:

```java
package com.brickgame.games;

import java.util.*;
import java.awt.event.*;
import static com.brickgame.Constants.*;
import static com.brickgame.screens.Sprite.createPosition;
import com.brickgame.Client;
import com.brickgame.block.*;
```

You may also import any library of your preference.
  
### Class structure
The class containing the new game should implement `GameEngine` and 
be imported into `...brickgame_java\src\main\java\com\brickgame\Main.java`

The class may follow this template:

```java
/**
 * An NewGame game.
 * 
 * @author  authorName
 * @version "%I%, %G%"
 * @since   JDK1.8
 */
public class NewGame extends GameEngine {
    // TODO: Define fields.
    // ...

    /** Constructor for {@link NewGame}. */
    public NewGame() {
        super();
        // Setting containers for the entities.
        setEntities("firstEntity", "secondEntity");
        // ...
    }

    /** Defines game objects. */
    @Override
    public void start() {
        super.start();
        // TODO: Initialize fields.
        // ...
        // Spawning the entities.
        this.firstEntity = First();
        this.secondEntity = Second();
    }
    
    /**
     * Deals with user input during the game.
     * 
     * @param key     A {@link KeyEvent} identifier for a key.
     * @param pressed Whether {@code key} was pressed or released.
     */
    @Override
    public void setKeyBindings(int key, boolean pressed) {
        super.setKeyBindings(key, pressed);
        if (running) {
            if (pressed) {  // Key pressed.
                // Set Shooter movement.
                //if (key == KeyEvent.VK_...) {
                //    ...;
                //} else if (key == KeyEvent.VK_...) {
                //    ...;
                //} ...
            } else {  // Key released.
                //if (key == KeyEvent.VK_...) {
                //    ...;
                //} else if (key == KeyEvent.VK_...) {
                //    ...;
                //} ...
            }
        }
    }

    /**
     * Game logic implementation.
     * 
     * @param t A timer.
     */
    @Override
    public void manage(int t) {
        if (running && !paused) {
            // TODO: Define game logic
            // ...
        }
        // Managing endgame.
        super.manage(t);
    }

    /** Scoring mechanics. */
    void updateScore(int blocksHit) {
        // TODO: Define scoring
        super.updateScore();
    }

    /**
     * Victory occurs when ...
     * 
     * @return Whether the game has been beaten.
     */
    @Override
    public boolean checkVictory() {
        // TODO: Define victory condition.
        //if (...) {
        //    return true;
        //} else {
        //    return false;
        //}
    }

    /**
     * Defeat happens if ...
     * 
     * @return Whether the game was lost.
     */
    @Override
    public boolean checkDefeat() {
        // TODO: Define defeat condition.
        //if (...) {
        //    return true;
        //} else {
        //    return false;
        //}
    }

    // Rest of the methods.
    // ...

    /** documentation */
    private class First {
        /** documentation */
        First() {}

        // Rest of the methods.
        // ...
    }

    /** documentation */
    private class Second {
        /** documentation */
        Second() {}

        // Rest of the methods.
        // ...
    }

    public static void main(String[] args) {
        class NewGameClient extends Client {
            // TODO: Change NewGame to the name of your game.
            NewGame game;
            NewGameClient() {
                super();  // setup() and setLoop() are invoked here.
                window.setTitle("NewGame");
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
                super.setLoop();
                // Implementing the game mechanics and check for endgame.
                game.manage(ticks);
                // Drawing the game objects to the screen.
                game.drawEntities();
                // Updating BlinkingBlocks mechanics.
                game.update(ticks);

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

        System.out.println("Check docs/GameManuals.md for instructions.");
        new NewGameClient();
    }
}

```

### Updates in `...\brickgame_java\src\main\java\com\brickgame\Main.java`
Some changes need to be made in order to properly load the game 
when running the full package:
* Import the game into `...\brickgame_java\src\main\java\com\brickgame\Main.java`
  with `import com.brickgame.games.NewGame;`
* Update the file `...\brickgame_java\src\main\resources\HighScores.json` by 
  changing the `Main.createHighScores()` method, adding 
  `highScores.put("NewGame", 0);` in the `try` statement (around line 36).
  Before:
  ```java
  try {
    if (file.createNewFile()) {
        Map<String, Integer> highScores = new LinkedHashMap<>();
        highScores.put("Snake", 0);
        highScores.put("Breakout", 0);
        highScores.put("Asteroids", 0);
        highScores.put("Tetris", 0);
        // rest of the code
        // ...
    }
  } catch (IOException e) {}
  ```
  After:
  ```java
  try {
    if (file.createNewFile()) {
        Map<String, Integer> highScores = new LinkedHashMap<>();
        highScores.put("Snake", 0);
        highScores.put("Breakout", 0);
        highScores.put("Asteroids", 0);
        highScores.put("Tetris", 0);
        highScores.put("NewGame", 0);
        // rest of the code
        // ...
    }
  } catch (IOException e) {}
  ```
* Update the `Brickgame.Selector()` constructor by adding
  `n: NewGame(),` to `this.gameSelect` (around line 127) (`n` is the
  new  number of games).
  
  Before:
  ```java
  this.gameSelect.put(1, new Snake());
  this.gameSelect.put(2, new Breakout());
  this.gameSelect.put(3, new Asteroids());
  this.gameSelect.put(4, new Tetris());
  ```
  After:
  ```java
  this.gameSelect.put(1, new Snake());
  this.gameSelect.put(2, new Breakout());
  this.gameSelect.put(3, new Asteroids());
  this.gameSelect.put(4, new Tetris());
  this.gameSelect.put(5, new NewGame());
  ```
  
### Updates in `...\brickgame_java\src\main\java\com\brickgame\screens\GamePreviews.java`
The image previews for each game are built and drawn when running 
the game using the `GamePreviews.java` file. To create the 
previews for the new game, you will need to add three new nested 
classes in the `GamePreview` class with this format:
```java
/** Organizes the game preview screens. */
public class GamePreviews {

    // ...

    /** First preview for {@link com.brickgame.games.NewGame NewGame}. */
    public static class NewGame1 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            // TODO: Define sketch
            //sketch.put(0,  new int[] {...});
            //sketch.put(1,  new int[] {...});
            // ...
            //sketch.put(19,  new int[] {...});

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }

    /** Second preview for {@link com.brickgame.games.NewGame NewGame}. */
    public static class NewGame2 extends Background {
        // ...
    }
    /** Third preview for {@link com.brickgame.games.NewGame NewGame}. */
    public static class NewGame3 extends Background {
        // ...
    }

    // ...

}
```

An example for `sketch`:

```java
sketch.put(0,  new int[] {                            });
sketch.put(1,  new int[] {                            });
sketch.put(2,  new int[] {         3, 4, 5,           });
sketch.put(3,  new int[] {            4,              });
sketch.put(4,  new int[] {                            });
sketch.put(5,  new int[] {                            });
sketch.put(6,  new int[] {                            });
sketch.put(7,  new int[] {                            });
sketch.put(8,  new int[] {                            });
sketch.put(9,  new int[] {                            });
sketch.put(10, new int[] {0,                          });
sketch.put(11, new int[] {0,                          });
sketch.put(12, new int[] {0, 1, 2,          6, 7,     });
sketch.put(13, new int[] {0, 1, 2, 3,    5, 6, 7, 8, 9});
sketch.put(14, new int[] {                            });
sketch.put(15, new int[] {         3, 4, 5,           });
sketch.put(16, new int[] {         3,       6,        });
sketch.put(17, new int[] {         3,       6,        });
sketch.put(18, new int[] {         3,       6,        });
sketch.put(19, new int[] {         3, 4, 5,           });
```

> Remember to add the next unused letter of the alphabet for identification.