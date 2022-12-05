package src;

import java.util.ArrayList;
import java.util.List;

public class Matrix implements Runnable {
    List<List<Long>> mat1;
    List<List<Long>> mat2;
    List<List<Long>> mat3;

    SplitedMat sm;

    int[] pos;
    Matrix(SplitedMat smA, SplitedMat smB) {
        this.mat1 = smA.mat;
        this.mat2 = smB.mat;
        int[] tempPos = new int[2];
        if (smA.pos[0] == -1) {
            tempPos[0] = smB.pos[0];
        } else {
            tempPos[0] = smA.pos[0];
        }

        if (smA.pos[1] == -1) {
            tempPos[1] = smB.pos[1];
        } else {
            tempPos[1] = smA.pos[1];
        }
        this.pos = tempPos;
    }

    public SplitedMat doMatCal() {
        this.mat3 = matMuli(mat1, mat2);
        sm = new SplitedMat(pos[0], pos[1], mat3);
        return sm;
    }

    public List<List<Long>> matMuli(List<List<Long>> mat1, List<List<Long>> mat2) {
        System.out.println("Thread id " + Thread.currentThread().getName() + " Started!");
        List<List<Long>> res = new ArrayList<>();
        for (int i = 0; i < mat1.size(); i++) {
            List<Long> row = new ArrayList<>();
            for (int j = 0; j < mat2.size(); j++) {
                row.add(0l);
            }
            res.add(row);
        }
//        for (List<Long> mat1row : mat1) {
//            List<Long> newRow = new ArrayList<>();
//            for (int k = 0; k < mat2.get(0).size(); k++) {
//                long sum = 0;
//                for (int i = 0, j = 0; i < mat1row.size() && j < mat2.size(); i++, j++) {
//                    sum += mat1row.get(i) * (mat2.get(j).get(k));
//                }
//                newRow.add(sum);
//            }
//            res.add(newRow);
//        }
        for (int i = 0; i < mat1.size(); i++) {
            for (int j = 0; j < mat2.size(); j++) {
                long sum = 0;
                for (int k = 0; k < mat1.get(0).size(); k++) {
                    sum += mat1.get(i).get(k) * mat2.get(j).get(k);
                }
                res.get(i).set(j, sum);
            }
        }
        return res;
    }

    @Override
    public void run() {
        doMatCal();
    }
}
