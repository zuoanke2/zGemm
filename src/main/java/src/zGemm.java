package src;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class zGemm {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner sc = new Scanner(System.in);
        System.out.println("input your selection the next line, 1 is version1, 2 is version2(distributed version), 3 is for developer testing, don't input 3:");
        int select = sc.nextInt();
        if (select == 1) {
            String filePath1 = "/Users/xcaonimax/Desktop/javaProjects/zGemm/matrix1.csv";
            String filePath2 = "/Users/xcaonimax/Desktop/javaProjects/zGemm/matrix2.csv";
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

            System.out.println("");
            System.out.println("Error detected, position: Row: " + errorPos.get(0) + " Col: " + errorPos.get(1));
            System.out.println("");
            System.out.println("Matrix multiple time: " + (cEnd - cStart) + " ms");
            System.out.println("Error detection time: " + (dEnd - dStart) + " ms");
            System.out.println("");

            System.out.println("You can review the file output.csv to check the result!");

            String filename = "output.csv";
            File file=new File(filename);
            try {
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw=new BufferedWriter(fw);
                for(int i = 0; i < 1024; i++){
                    for(int j = 0; j < 1024; j++){
                        bw.write(String.valueOf(newMat.get(i).get(j)));
                        bw.write(",");
                    }
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } else if (select == 2) {
            String filePath1 = "/Users/xcaonimax/Desktop/javaProjects/zGemm/matrix1.csv";
            String filePath2 = "/Users/xcaonimax/Desktop/javaProjects/zGemm/matrix1.csv";
            MatFuncs mf = new MatFuncs();
            //get matrix
            List<List<Long>> mat1data = mf.readMat(filePath1);
            List<List<Long>> mat2data = mf.readMat(filePath2);
            //split matrix A and B
            List<SplitedMat> matListA = mf.splitMatrixByRow(mat1data);
            List<SplitedMat> matListB = mf.splitMatrixByCol(mat2data);

            List<SplitedMat> matListC = new ArrayList<>();

            ExecutorService executor = Executors.newFixedThreadPool(16);
            List<Future> futureList = new ArrayList<>();
            List<Matrix> taskList = new ArrayList<>();

            long start = System.currentTimeMillis();

            for (int i = 0; i < matListA.size(); i++) {
                for (int j = 0; j < matListB.size(); j++) {
                    Matrix task = new Matrix(matListA.get(i), matListB.get(j));
                    taskList.add(task);
                    Future future = executor.submit(task);
                    futureList.add(future);
                }
            }

            executor.shutdown();

            while (!executor.isTerminated()) {

            }

            Random rm = new Random();
            int putNum = rm.nextInt(16);
            int count = 0;
            for (Future future : futureList) {
                if (count == putNum) {
                    SplitedMat sm = (SplitedMat) future.get();
                    System.out.println("");
                    System.out.println("******************************************************");
                    System.out.println("will inject an error in No."+ (count + 1) + " block");
                    mf.putAnError(sm.mat);
                    System.out.println("******************************************************");
                    System.out.println("");
                    matListC.add(sm);
                    count++;
                } else {
                    matListC.add((SplitedMat) future.get());
                    count++;
                }
            }
            count = 0;
            long TotalErrorDetectTime = 0;
            for (Matrix task : taskList) {
                List<Integer> errorPos = task.errorDetect();
                System.out.println("Block No." + (count + 1) + " error detecting...");
                if (errorPos.size() == 0) {
                    System.out.println("Block No." + (count + 1) + " has no error!");
                    System.out.println("Error detection time: " + task.errorDetectionTime + " ms");
                    System.out.println("");
                } else {
                    System.out.println("Detected an ERROR in block No." + (count + 1));
                    System.out.println("Error position in this block: Row: " + errorPos.get(0) + " Col: " + errorPos.get(1));
                    System.out.println("Error detection time: " + task.errorDetectionTime + " ms");
                    System.out.println("");
                }
                TotalErrorDetectTime += task.errorDetectionTime;
                count++;
            }

            System.out.println("");
            System.out.println("Generating whole result...");
            List<List<Long>> mat3 = mf.combineMatrix(matListC);

            long reDetStart = System.currentTimeMillis();

            System.out.println("");
            System.out.println("Whole result error detecting...");
            //get the origin check sum list
            List<Long> originColCheckSumList = mf.getColCheckSum(mat1data);
            List<Long> originRowCheckSumList = mf.getRowCheckSum(mat2data);

            //cal the correct check sum list
            List<Long> colCheckSumShouldBe = mf.calColCheckSum(originColCheckSumList, mat2data);
            List<Long> rowCheckSumShouldBe = mf.calRowCheckSum(mat1data, originRowCheckSumList);

            //get the new check sum list
            List<Long> newColCheckSum = mf.getColCheckSum(mat3);
            List<Long> newRowCheckSum = mf.getRowCheckSum(mat3);

            List<Integer> errorPos = mf.detectError(colCheckSumShouldBe, newColCheckSum, rowCheckSumShouldBe, newRowCheckSum);

            long reDetEnd = System.currentTimeMillis();

            TotalErrorDetectTime += reDetEnd - reDetStart;

            System.out.println("");
            System.out.println("Final result matrix Error detected, position: Row: " + errorPos.get(0) + " Col: " + errorPos.get(1));
            System.out.println("Final result matrix Error detection time: " + (reDetEnd - reDetStart) + " ms");
            System.out.println("");

            long end = System.currentTimeMillis();

            System.out.println("");
            System.out.println("All work Done!");
            System.out.println("Parallel computing time: " + (end - start) + " ms.");
            System.out.println("Include total error detection time: " + TotalErrorDetectTime + " ms.");
            System.out.println("");

            System.out.println("You can review the file output_distributed.csv to check the result!");

            String filename = "output_distributed.csv";
            File file=new File(filename);
            try {
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw=new BufferedWriter(fw);
                for(int i = 0; i < 1024; i++){
                    for(int j = 0; j < 1024; j++){
                        bw.write(String.valueOf(mat3.get(i).get(j)));
                        bw.write(",");
                    }
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (select == 3) {
            String filePath1 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/test1.csv";
            String filePath2 = "/Users/zuoankembp/Desktop/CSE8377/zGemm/test2.csv";
            MatFuncs mf = new MatFuncs();
            //get matrix
            List<List<Long>> mat1 = mf.readMat(filePath1);
            List<List<Long>> mat2 = mf.readMat(filePath2);

            SplitedMat sm1 = new SplitedMat(0, 0, mat1);
            SplitedMat sm2 = new SplitedMat(0, 0, mat2);
            Matrix mx = new Matrix(sm1, sm2);
            mx.doMatCal();
            mf.putAnError(mx.sm.mat);
            mx.errorDetect();
            System.out.println("finish");
        }
    }

}