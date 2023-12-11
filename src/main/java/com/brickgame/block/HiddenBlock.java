package com.brickgame.block;

import java.util.ArrayList;

/** A {@link Block} with a name identifier. */
public class HiddenBlock extends Block {
    public HiddenBlock(int i, int j) {
        super(i, j);
    }
    public HiddenBlock(int[] coords) {
        super(coords);
    }
    public HiddenBlock(ArrayList<Integer> list) {
        super(list);
    }
}
