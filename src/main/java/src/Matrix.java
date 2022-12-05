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

    List<Long> calColCheckSum(List<Long> colCheckSum, List<List<Long>> mat) {
        List<Long> ans = new ArrayList<>();
        for (int i = 0; i < mat.size(); i++) {
            long sum = 0;
            for (int j = 0, k = 0; j < mat.get(0).size() && k < colCheckSum.size(); j++, k++) {
                sum += mat.get(i).get(j) * colCheckSum.get(k);
            }
            ans.add(sum);
        }
        return ans;
    }

    List<Long> calRowCheckSum(List<List<Long>> mat, List<Long> rowCheckSum) {
        List<Long> ans = new ArrayList<>();
        for (List<Long> row : mat) {
            long sum = 0;
            for (int i = 0; i < row.size(); i++) {
                sum += row.get(i) * rowCheckSum.get(i);
            }
            ans.add(sum);
        }
        return ans;
    }

    List<Long> getCheckSumMN(List<List<Long>> mat) {
        List<Long> ans = new ArrayList<>();
        for (int i = 0; i < mat.get(0).size(); i++) {
            long checkSum = 0;
            for (int j = 0; j < mat.size(); j++) {
                checkSum += mat.get(j).get(i);
            }
            ans.add(checkSum);
        }
        return ans;
    }

    List<Long> getColCheckSum(List<List<Long>> mat) {
        List<Long> ans = new ArrayList<>();
        for (int i = 0; i < mat.get(0).size(); i++) {
            long checkSum = 0;
            for (int j = 0; j < mat.size(); j++) {
                checkSum += mat.get(j).get(i);
            }
            ans.add(checkSum);
        }
        return ans;
    }

    List<Long> getRowCheckSum(List<List<Long>> mat) {
        List<Long> ans = new ArrayList<>();
        for (List<Long> row : mat) {
            long rowCheckSum = 0;
            for (long num : row) {
                rowCheckSum += num;
            }
            ans.add(rowCheckSum);
        }
        return ans;
    }

    public List<Integer> detectError(List<Long> colCheckSumShouldBe, List<Long> realColCheckSum, List<Long> rowCheckSumShouldBe, List<Long> realRowCheckSum) {
        List<Integer> ans = new ArrayList<>();
        int colDiff = 0;
        int rowDiff = 0;
        for (int i = 0; i < colCheckSumShouldBe.size(); i++) {
            if (!colCheckSumShouldBe.get(i).equals(realColCheckSum.get(i))) {
                colDiff = i;
            }
        }
        for (int i = 0; i < rowCheckSumShouldBe.size(); i++) {
            if (!rowCheckSumShouldBe.get(i).equals(realRowCheckSum.get(i))) {
                rowDiff = i;
            }
        }
        ans.add(rowDiff);
        ans.add(colDiff);
        return ans;
    }

    public void errorDetect() {
        long dStart = System.currentTimeMillis();

        //get the origin check sum list
        List<Long> originColCheckSumList = getCheckSumMN(mat1);
        List<Long> originRowCheckSumList = getCheckSumMN(mat2);

        //cal the correct check sum list
        List<Long> colCheckSumShouldBe = calColCheckSum(originColCheckSumList, mat2);
        List<Long> rowCheckSumShouldBe = calRowCheckSum(mat1, originRowCheckSumList);

        //get the new check sum list
        List<Long> newColCheckSum = getColCheckSum(mat3);
        List<Long> newRowCheckSum = getRowCheckSum(mat3);

        List<Integer> errorPos = detectError(colCheckSumShouldBe, newColCheckSum, rowCheckSumShouldBe, newRowCheckSum);

        long dEnd = System.currentTimeMillis();

        System.out.println("Error detected, position: Row: " + errorPos.get(0) + " Col: " + errorPos.get(1));
        System.out.println("Error detection time: " + (dEnd - dStart) + " ms");
    }

    @Override
    public void run() {
        doMatCal();
    }
}
