package src;

import java.util.List;

public class SplitedMat {
    int[] pos;
    List<List<Long>> mat;

    SplitedMat(int row, int col, List<List<Long>> mat) {
        pos = new int[2];
        this.pos[0] = row;
        this.pos[1] = col;
        this.mat = mat;
    }
}
