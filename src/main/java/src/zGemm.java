package src;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class zGemm {
    public List<List<Long>> readMat(String strPath) {
        System.out.println("Initializing...");
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(strPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = StrUtil.trim(values[i]);
                }
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<List<Long>> mat1 = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            List<Long> tempList = new ArrayList<>();
            for (String numStr : records.get(i)) {
                tempList.add(Long.parseLong(numStr));
            }
            mat1.add(tempList);
        }
        return mat1;
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

    List<Long> calColCheckSum(List<Long> colCheckSum, List<List<Long>> mat) {
        List<Long> ans = new ArrayList<>();
        for (int i = 0; i < mat.get(0).size(); i++) {
            long sum = 0;
            for (int j = 0, k = 0; j < colCheckSum.size() && k < mat.size(); j++, k++) {
                sum += colCheckSum.get(j) * mat.get(k).get(i);
            }
            ans.add(sum);
        }
        return ans;
    }

    List<Long> calRowCheckSum(List<List<Long>> mat, List<Long> rowCheckSum) {
        List<Long> ans = new ArrayList<>();
        for (int i = 0; i < mat.size(); i++) {
            long sum = 0;
            for (int j = 0, k = 0; j < mat.get(0).size() && k < rowCheckSum.size(); j++, k++) {
                sum += mat.get(i).get(j) * rowCheckSum.get(k);
            }
            ans.add(sum);
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

//    public List<List<Long>> addColCheckSum(List<List<Long>> mat) {
//        List<Long> colCheckSum = new ArrayList<>();
//        for (int i = 0; i < mat.get(0).size(); i++) {
//            long checkSum = 0;
//            for (int j = 0; j < mat.size(); j++) {
//                checkSum += mat.get(j).get(i);
//            }
//            colCheckSum.add(checkSum);
//        }
//        mat.add(colCheckSum);
//        return mat;
//    }
//
//    public List<List<Long>> addRowCheckSum(List<List<Long>> mat) {
//        for (List<Long> row : mat) {
//            long rowCheckSum = 0;
//            for (long num : row) {
//                rowCheckSum += num;
//            }
//            row.add(rowCheckSum);
//        }
//        return mat;
//    }

    public void putAnError(List<List<Long>> mat) {
        Random rm = new Random();
        int row = rm.nextInt(mat.size());
        int col = rm.nextInt(mat.get(0).size());
        long origin = mat.get(row).get(col);
        long error = rm.nextLong();
        mat.get(row).set(col, error);
        System.out.println("put an error in: Row: " + row + " Col: " + col);
        System.out.println("Original valus: " + origin);
        System.out.println("error valus: " + error);
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

    public static void main(String[] args) {
        String filePath1 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/matrix1.csv";
        String filePath2 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/matrix2.csv";
        zGemm zg = new zGemm();
        //get matrix
        List<List<Long>> mat1 = zg.readMat(filePath1);
        List<List<Long>> mat2 = zg.readMat(filePath2);

        long cStart = System.currentTimeMillis();
        //matrix mul
        List<List<Long>> newMat = zg.matMuli(mat1, mat2);
        long cEnd = System.currentTimeMillis();

        zg.putAnError(newMat);

        System.out.println("**************************************************");
        System.out.println("Matrix multiple finished! Has inject one ERROR!");
        System.out.println("Error detecting started!");
        System.out.println("**************************************************");

        long dStart = System.currentTimeMillis();

        //get the origin check sum list
        List<Long> originColCheckSumList = zg.getColCheckSum(mat1);
        List<Long> originRowCheckSumList = zg.getRowCheckSum(mat2);
        //cal the correct check sum list
        List<Long> colCheckSumShouldBe = zg.calColCheckSum(originColCheckSumList, mat2);
        List<Long> rowCheckSumShouldBe = zg.calRowCheckSum(mat1, originRowCheckSumList);

        //get the new check sum list
        List<Long> newColCheckSum = zg.getColCheckSum(newMat);
        List<Long> newRowCheckSum = zg.getRowCheckSum(newMat);

        List<Integer> errorPos = zg.detectError(colCheckSumShouldBe, newColCheckSum, rowCheckSumShouldBe, newRowCheckSum);

        long dEnd = System.currentTimeMillis();

        System.out.println("Error detected, position: Row: " + errorPos.get(0) + " Col: " + errorPos.get(1));
        System.out.println("Matrix multiple time: " + (cEnd - cStart) + " ms");
        System.out.println("Error detection time: " + (dEnd - dStart) + " ms");
    }

}