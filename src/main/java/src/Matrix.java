package src;

import java.util.ArrayList;
import java.util.List;

public class Matrix implements Runnable {
    List<List<Long>> mat1;
    List<List<Long>> mat2;
    List<List<Long>> mat3;

    int[] pos;
    Matrix(SplitedMat smA, SplitedMat smB) {
        this.mat1 = smA.mat;
        this.mat2 = smB.mat;
        this.pos = smA.pos;
    }

    public SplitedMat doMatCal() {
        this.mat3 = matMuli(mat1, mat2);
        SplitedMat sm = new SplitedMat(pos[0], pos[1], mat3);
        return sm;
    }

    public List<List<Long>> matMuli(List<List<Long>> mat1, List<List<Long>> mat2) {
        System.out.println("Matrix multiple start!");
        List<List<Long>> res = new ArrayList<>();
        int count = 0;
        for (List<Long> mat1row : mat1) {
            List<Long> newRow = new ArrayList<>();
            for (int k = 0; k < mat2.get(0).size(); k++) {
                long sum = 0;
                for (int i = 0, j = 0; i < mat1row.size() && j < mat2.size(); i++, j++) {
                    sum += mat1row.get(i) * (mat2.get(j).get(k));
                }
                newRow.add(sum);
                count++;
                if (count % 10485 == 0) {
                    System.out.println("%" + count / 10485);
                }
            }
            res.add(newRow);
        }
        return res;
    }

    @Override
    public void run() {
        this.mat3 = matMuli(this.mat1, this.mat2);
    }
}
