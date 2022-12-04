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

            List<SplitedMat> matListC = new ArrayList<>();

            Matrix mx1 = new Matrix(matListA.get(0), matListB.get(0));
            Thread t1 = new Thread(mx1);
            t1.start();

            Matrix mx2 = new Matrix(matListA.get(1), matListB.get(1));
            Thread t2 = new Thread(mx2);
            t2.start();

            Matrix mx3 = new Matrix(matListA.get(2), matListB.get(2));
            Thread t3 = new Thread(mx3);
            t3.start();

            Matrix mx4 = new Matrix(matListA.get(3), matListB.get(3));
            Thread t4 = new Thread(mx4);
            t4.start();

            Matrix mx5 = new Matrix(matListA.get(4), matListB.get(4));
            Thread t5 = new Thread(mx5);
            t5.start();

            Matrix mx6 = new Matrix(matListA.get(5), matListB.get(5));
            Thread t6 = new Thread(mx6);
            t6.start();

            Matrix mx7 = new Matrix(matListA.get(6), matListB.get(6));
            Thread t7 = new Thread(mx7);
            t7.start();

            Matrix mx8 = new Matrix(matListA.get(7), matListB.get(7));
            Thread t8 = new Thread(mx8);
            t8.start();

            Matrix mx9 = new Matrix(matListA.get(8), matListB.get(8));
            Thread t9 = new Thread(mx9);
            t9.start();

            Matrix mx10 = new Matrix(matListA.get(9), matListB.get(9));
            Thread t10 = new Thread(mx10);
            t10.start();

            Matrix mx11 = new Matrix(matListA.get(10), matListB.get(10));
            Thread t11 = new Thread(mx11);
            t11.start();

            Matrix mx12 = new Matrix(matListA.get(11), matListB.get(11));
            Thread t12 = new Thread(mx12);
            t12.start();

            Matrix mx13 = new Matrix(matListA.get(12), matListB.get(12));
            Thread t13 = new Thread(mx13);
            t13.start();

            Matrix mx14 = new Matrix(matListA.get(13), matListB.get(13));
            Thread t14 = new Thread(mx14);
            t14.start();

            Matrix mx15 = new Matrix(matListA.get(14), matListB.get(14));
            Thread t15 = new Thread(mx15);
            t15.start();

            Matrix mx16 = new Matrix(matListA.get(15), matListB.get(15));
            Thread t16 = new Thread(mx16);
            t16.start();

            System.out.println("");
            System.out.println("Computing...");
            System.out.println("");

            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
            t9.join();
            t10.join();
            t11.join();
            t12.join();
            t13.join();
            t14.join();
            t15.join();
            t16.join();

            System.out.println("Distributed matrix multiple finished!");
            System.out.println("");
            System.out.println("Generating result...");
            System.out.println("");
            matListC.add(mx1.sm);
            matListC.add(mx2.sm);
            matListC.add(mx3.sm);
            matListC.add(mx4.sm);
            matListC.add(mx5.sm);
            matListC.add(mx6.sm);
            matListC.add(mx7.sm);
            matListC.add(mx8.sm);
            matListC.add(mx9.sm);
            matListC.add(mx10.sm);
            matListC.add(mx11.sm);
            matListC.add(mx12.sm);
            matListC.add(mx13.sm);
            matListC.add(mx14.sm);
            matListC.add(mx15.sm);
            matListC.add(mx16.sm);

            List<List<Long>> mat3 = new ArrayList<>();
            mat3 = mf.combineMatrix(matListC);

            System.out.println("All work Done");


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