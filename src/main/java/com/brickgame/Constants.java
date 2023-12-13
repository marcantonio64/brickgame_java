package com.brickgame;

import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.*;
import java.net.*;
import java.awt.*;

/** Contants used throughout the package. */
public interface Constants {
    // Colors.
    Color BACK_COLOR = new Color(109, 120, 92);  // Light green
    Color SHADE_COLOR = new Color(97, 112, 91);  // Dark green
    Color LINE_COLOR = new Color(0, 0, 0);       // Black
    
    // Window dimensions.
    enum Size {GIGA, HUGE, LARGE, BIG, MEDIUM, SMALL};
    Size SIZE = Size.BIG;
    int[] RESOLUTION = H.chooseResolution(SIZE);
    int WINDOW_WIDTH = RESOLUTION[0];
    int WINDOW_HEIGHT = RESOLUTION[1];
    Dimension WINDOW_DIMENSIONS = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    
    // Block dimensions.
    int BORDER_WIDTH = WINDOW_WIDTH/111;
    int PIXEL_SIDE = WINDOW_WIDTH/111;
    int BLOCK_SIDE = PIXEL_SIDE*10;
    int DIST_BLOCKS = BLOCK_SIDE+PIXEL_SIDE;
    Dimension BLOCK_DIMENSIONS = new Dimension(BLOCK_SIDE, BLOCK_SIDE);
    
    // Handling movement.
    static enum Direction {LEFT, RIGHT, UP, DOWN}
    Map<Direction, int[]> CONVERT = H.convert();
    
    // Handling time.
    int TICK_RATE = 23;  // Minimum time interval, in milliseconds.
    int FPS = 1000/TICK_RATE;  // 62 when TICK_RATE is 16.

    // Tracking directories.
    String HIGH_SCORES_DIR = H.getDir("HighScores.json");
    
    /** A Helper for constructing the contants. */
    class H {
        /**
         * Manages screen dimensions.
         * 
         * @param size The desired size.
         */ 
        static int[] chooseResolution(Size size) {
            switch (size) {
                case GIGA:
                    return new int[]{666, 1326};
                case HUGE:
                    return new int[]{555, 1105};
                case LARGE:
                    return new int[]{444, 884};
                case BIG:
                    return new int[]{333, 663};
                case MEDIUM:
                    return new int[]{222, 442};
                case SMALL:
                    return new int[]{111, 221};
                default:
                    return new int[]{333, 663};
            }
        }

        /**
         * Manages direction vectors.
         * 
         * @return A mapping corresponding directions to 2D-arrays.
         */ 
        static Map<Direction, int[]> convert() {
            Map<Direction, int[]> dict = new HashMap<>();
            dict.put(Direction.LEFT, new int[]{-1, 0});
            dict.put(Direction.RIGHT, new int[]{1, 0});
            dict.put(Direction.UP, new int[]{0, -1});
            dict.put(Direction.DOWN, new int[]{0, 1});
            dict.put(null, new int[]{0, 0});
            return dict;
        }

        /** 
         * Tracks the location of a file in the {@literal resources/} folder.
         * 
         * @param fileName The desired file.
         * @return         The full path of {@code fileName}.
         */ 
        static String getDir(String fileName) {
            String innerPath = "brickgame_java/src/main/resources/" + fileName;
            try {
                String currClassDir = Constants.class.getProtectionDomain()
                                                     .getCodeSource()
                                                     .getLocation()
                                                     .toURI()
                                                     .getPath();
                int dirIndex = currClassDir.indexOf("brickgame_java");
                String outerPath = currClassDir.substring(0, dirIndex);
                // Remove the leading '/' for Windows paths.
                if (System.getProperty("os.name").startsWith("Windows")) {
                    if (outerPath.startsWith("/")) {
                        outerPath = outerPath.substring(1);
                    }
                }
                String dir = Paths.get(outerPath, innerPath).toString();
                String decodedDir = URLDecoder.decode(dir, "UTF-8");
                return decodedDir;
            } catch (UnsupportedEncodingException | URISyntaxException e) {
                e.printStackTrace();
                return "";
            }
        }
    }
    
    // Testing.
    public static void main(String[] args) {
        System.out.println(Arrays.toString(RESOLUTION));
        System.out.println(FPS);
        System.out.println(HIGH_SCORES_DIR);
    }
}
