package com.brickgame.screens;

import java.util.Map;
import java.util.HashMap;

/** Organizes the game preview screens. */
public class GamePreviews {
    /** First preview for {@link com.brickgame.games.Snake Snake}. */
    public static class Snake1 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {                            });
            sketch.put(1,  new int[] {                            });
            sketch.put(2,  new int[] {                            });
            sketch.put(3,  new int[] {      2, 3, 4, 5, 6, 7,     });
            sketch.put(4,  new int[] {                            });
            sketch.put(5,  new int[] {                            });
            sketch.put(6,  new int[] {                            });
            sketch.put(7,  new int[] {                     7,     });
            sketch.put(8,  new int[] {                            });
            sketch.put(9,  new int[] {                            });
            sketch.put(10, new int[] {                            });
            sketch.put(11, new int[] {                            });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {                            });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {            4, 5,           });
            sketch.put(16, new int[] {         3,       6,        });
            sketch.put(17, new int[] {         3, 4, 5, 6,        });
            sketch.put(18, new int[] {         3,       6,        });
            sketch.put(19, new int[] {         3,       6,        });

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Second preview for {@link com.brickgame.games.Snake Snake}. */
    public static class Snake2 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {                            });
            sketch.put(1,  new int[] {                            });
            sketch.put(2,  new int[] {                            });
            sketch.put(3,  new int[] {         3, 4, 5, 6, 7,     });
            sketch.put(4,  new int[] {                     7,     });
            sketch.put(5,  new int[] {                            });
            sketch.put(6,  new int[] {                            });
            sketch.put(7,  new int[] {                     7,     });
            sketch.put(8,  new int[] {                            });
            sketch.put(9,  new int[] {                            });
            sketch.put(10, new int[] {                            });
            sketch.put(11, new int[] {                            });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {                            });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {            4, 5,           });
            sketch.put(16, new int[] {         3,       6,        });
            sketch.put(17, new int[] {         3, 4, 5, 6,        });
            sketch.put(18, new int[] {         3,       6,        });
            sketch.put(19, new int[] {         3,       6,        });

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Third preview for {@link com.brickgame.games.Snake Snake}. */
    public static class Snake3 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {                            });
            sketch.put(1,  new int[] {                            });
            sketch.put(2,  new int[] {                            });
            sketch.put(3,  new int[] {            4, 5, 6, 7,     });
            sketch.put(4,  new int[] {                     7,     });
            sketch.put(5,  new int[] {                     7,     });
            sketch.put(6,  new int[] {                            });
            sketch.put(7,  new int[] {                     7,     });
            sketch.put(8,  new int[] {                            });
            sketch.put(9,  new int[] {                            });
            sketch.put(10, new int[] {                            });
            sketch.put(11, new int[] {                            });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {                            });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {            4, 5,           });
            sketch.put(16, new int[] {         3,       6,        });
            sketch.put(17, new int[] {         3, 4, 5, 6,        });
            sketch.put(18, new int[] {         3,       6,        });
            sketch.put(19, new int[] {         3,       6,        });
            
            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }

    /** First preview for {@link com.brickgame.games.Breakout Breakout}. */
    public static class Breakout1 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
            sketch.put(1,  new int[] {0,                         9});
            sketch.put(2,  new int[] {0,                         9});
            sketch.put(3,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(4,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(5,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(6,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(7,  new int[] {0,                         9});
            sketch.put(8,  new int[] {0,                         9});
            sketch.put(9,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
            sketch.put(10, new int[] {                            });
            sketch.put(11, new int[] {               5,           });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {         3, 4, 5,           });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {         3, 4, 5,           });
            sketch.put(16, new int[] {         3,       6,        });
            sketch.put(17, new int[] {         3, 4, 5,           });
            sketch.put(18, new int[] {         3,       6,        });
            sketch.put(19, new int[] {         3, 4, 5,           });

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Second preview for {@link com.brickgame.games.Breakout Breakout}. */
    public static class Breakout2 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
            sketch.put(1,  new int[] {0,                         9});
            sketch.put(2,  new int[] {0,                         9});
            sketch.put(3,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(4,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(5,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(6,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(7,  new int[] {0,                         9});
            sketch.put(8,  new int[] {0,                         9});
            sketch.put(9,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
            sketch.put(10, new int[] {                  6,        });
            sketch.put(11, new int[] {                            });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {         3, 4, 5,           });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {         3, 4, 5,           });
            sketch.put(16, new int[] {         3,       6,        });
            sketch.put(17, new int[] {         3, 4, 5,           });
            sketch.put(18, new int[] {         3,       6,        });
            sketch.put(19, new int[] {         3, 4, 5,           });

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Third preview for {@link com.brickgame.games.Breakout Breakout}. */
    public static class Breakout3 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
            sketch.put(1,  new int[] {0,                         9});
            sketch.put(2,  new int[] {0,                         9});
            sketch.put(3,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(4,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(5,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(6,  new int[] {0,       3, 4, 5, 6,       9});
            sketch.put(7,  new int[] {0,                         9});
            sketch.put(8,  new int[] {0,                         9});
            sketch.put(9,  new int[] {0, 1, 2, 3, 4, 5,       8, 9});
            sketch.put(10, new int[] {                            });
            sketch.put(11, new int[] {                     7,     });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {            4, 5, 6,        });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {         3, 4, 5,           });
            sketch.put(16, new int[] {         3,       6,        });
            sketch.put(17, new int[] {         3, 4, 5,           });
            sketch.put(18, new int[] {         3,       6,        });
            sketch.put(19, new int[] {         3, 4, 5,           });
            
            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** First preview for {@link com.brickgame.games.Asteroids Asteroids}. */
    public static class Asteroids1 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {0, 1, 2, 3,             8, 9});
            sketch.put(1,  new int[] {0,    2,    4, 5, 6, 7,    9});
            sketch.put(2,  new int[] {0,    2,                   9});
            sketch.put(3,  new int[] {0,       3, 4, 5,           });
            sketch.put(4,  new int[] {   1,                       });
            sketch.put(5,  new int[] {            4,              });
            sketch.put(6,  new int[] {                            });
            sketch.put(7,  new int[] {                            });
            sketch.put(8,  new int[] {                            });
            sketch.put(9,  new int[] {                            });
            sketch.put(10, new int[] {                            });
            sketch.put(11, new int[] {                            });
            sketch.put(12, new int[] {            4,              });
            sketch.put(13, new int[] {            4,              });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {            4, 5, 6,        });
            sketch.put(16, new int[] {         3,                 });
            sketch.put(17, new int[] {         3,                 });
            sketch.put(18, new int[] {         3,                 });
            sketch.put(19, new int[] {            4, 5, 6,        });

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Second preview for {@link com.brickgame.games.Asteroids Asteroids}. */
    public static class Asteroids2 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {0, 1, 2, 3,             8, 9});
            sketch.put(1,  new int[] {0,    2,    4, 5, 6, 7,    9});
            sketch.put(2,  new int[] {0,    2,                   9});
            sketch.put(3,  new int[] {0,       3, 4, 5,           });
            sketch.put(4,  new int[] {   1,       4,              });
            sketch.put(5,  new int[] {                            });
            sketch.put(6,  new int[] {                            });
            sketch.put(7,  new int[] {                            });
            sketch.put(8,  new int[] {                            });
            sketch.put(9,  new int[] {                            });
            sketch.put(10, new int[] {                            });
            sketch.put(11, new int[] {            4,              });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {            4,              });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {            4, 5, 6,        });
            sketch.put(16, new int[] {         3,                 });
            sketch.put(17, new int[] {         3,                 });
            sketch.put(18, new int[] {         3,                 });
            sketch.put(19, new int[] {            4, 5, 6,        });

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Third preview for {@link com.brickgame.games.Asteroids Asteroids}. */
    public static class Asteroids3 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {0, 1, 2, 3,             8, 9});
            sketch.put(1,  new int[] {0,    2,    4, 5, 6, 7,    9});
            sketch.put(2,  new int[] {0,    2,                   9});
            sketch.put(3,  new int[] {0,       3,    5,           });
            sketch.put(4,  new int[] {   1,                       });
            sketch.put(5,  new int[] {                            });
            sketch.put(6,  new int[] {                            });
            sketch.put(7,  new int[] {                            });
            sketch.put(8,  new int[] {                            });
            sketch.put(9,  new int[] {                            });
            sketch.put(10, new int[] {            4,              });
            sketch.put(11, new int[] {                            });
            sketch.put(12, new int[] {                            });
            sketch.put(13, new int[] {            4,              });
            sketch.put(14, new int[] {                            });
            sketch.put(15, new int[] {            4, 5, 6,        });
            sketch.put(16, new int[] {         3,                 });
            sketch.put(17, new int[] {         3,                 });
            sketch.put(18, new int[] {         3,                 });
            sketch.put(19, new int[] {            4, 5, 6,        });
            
            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** First preview for {@link com.brickgame.games.Tetris Tetris}. */
    public static class Tetris1 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {                            });
            sketch.put(1,  new int[] {         3, 4, 5,           });
            sketch.put(2,  new int[] {            4,              });
            sketch.put(3,  new int[] {                            });
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

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Second preview for {@link com.brickgame.games.Tetris Tetris}. */
    public static class Tetris2 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
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

            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }
    
    /** Third preview for {@link com.brickgame.games.Tetris Tetris}. */
    public static class Tetris3 extends Background {
        private Map<Integer, int[]> sketch;
        /** Draws the pixel image with {@link Sprite}s in a 20x10 grid. */
        @Override
        void draw() {
            super.draw();
            sketch = new HashMap<Integer, int[]>();
            sketch.put(0,  new int[] {                            });
            sketch.put(1,  new int[] {                            });
            sketch.put(2,  new int[] {                            });
            sketch.put(3,  new int[] {         3, 4, 5,           });
            sketch.put(4,  new int[] {            4,              });
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
            
            for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
                int j = entry.getKey();
                for (int i : entry.getValue()) {
                    new Sprite(i, j, this);
                }
            }
        }
    }

    public static void main(String[] args) {}
}

/* Template for more screens:
@Override
void draw() {
    super.draw();
    sketch = new HashMap<Integer, int[]>();
    sketch.put(0,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(1,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(2,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(3,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(4,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(5,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(6,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(7,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(8,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(9,  new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(10, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(11, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(12, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(13, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(14, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(15, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(16, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(17, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(18, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    sketch.put(19, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
                
    for (Map.Entry<Integer, int[]> entry : sketch.entrySet()) {
        int j = entry.getKey();
        for (int i : entry.getValue()) {
            new Sprite(i, j, this);
        }
    }
}
*/
