package src;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class zGemm {
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

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("input your selection, 1 is version1, 2 is version2(distributed version): ");
        int select = sc.nextInt();
        if (select == 1) {
            String filePath1 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/test1.csv";
            String filePath2 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/matrix2.csv";
            MatFuncs mf = new MatFuncs();
            //get matrix
            List<List<Long>> mat1 = mf.readMat(filePath1);
            List<List<Long>> mat2 = mf.readMat(filePath2);

            long cStart = System.currentTimeMillis();
            //matrix mul
            List<List<Long>> newMat = mf.matMuli(mat1, mat2);
            long cEnd = System.currentTimeMillis();

            mf.putAnError(newMat);

            System.out.println("**************************************************");
            System.out.println("Matrix multiple finished! Has inject one ERROR!");
            System.out.println("Error detecting started!");
            System.out.println("**************************************************");

            long dStart = System.currentTimeMillis();

            //get the origin check sum list
            List<Long> originColCheckSumList = mf.getColCheckSum(mat1);
            List<Long> originRowCheckSumList = mf.getRowCheckSum(mat2);
            //cal the correct check sum list
            List<Long> colCheckSumShouldBe = mf.calColCheckSum(originColCheckSumList, mat2);
            List<Long> rowCheckSumShouldBe = mf.calRowCheckSum(mat1, originRowCheckSumList);

            //get the new check sum list
            List<Long> newColCheckSum = mf.getColCheckSum(newMat);
            List<Long> newRowCheckSum = mf.getRowCheckSum(newMat);

            List<Integer> errorPos = mf.detectError(colCheckSumShouldBe, newColCheckSum, rowCheckSumShouldBe, newRowCheckSum);

            long dEnd = System.currentTimeMillis();

            System.out.println("Error detected, position: Row: " + errorPos.get(0) + " Col: " + errorPos.get(1));
            System.out.println("Matrix multiple time: " + (cEnd - cStart) + " ms");
            System.out.println("Error detection time: " + (dEnd - dStart) + " ms");
        } else if (select == 2) {
            String filePath1 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/matrix1.csv";
            String filePath2 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/matrix2.csv";
            MatFuncs mf = new MatFuncs();
            //get matrix
            List<List<Long>> mat1data = mf.readMat(filePath1);
            List<List<Long>> mat2data = mf.readMat(filePath2);
            //split matrix A and B
            List<SplitedMat> matListA = mf.splitMatrix(mat1data);
            List<SplitedMat> matListB = mf.splitMatrix(mat2data);

            Matrix mx1 = new Matrix(matListA.get(0), matListB.get(0));
        } else if (select == 3) {
            String filePath1 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/matrix1.csv";
            String filePath2 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/matrix2.csv";
            MatFuncs mf = new MatFuncs();
            //get matrix
            List<List<Long>> mat1 = mf.readMat(filePath1);
            List<List<Long>> mat2 = mf.readMat(filePath2);
            List<SplitedMat> splitedMats = mf.splitMatrix(mat1);
            List<List<Long>> oriMat = mf.combineMatrix(splitedMats);
            System.out.println("finish");
        }
    }

}