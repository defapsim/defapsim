package com.defapsim.evaluation.probleminstances;

import com.defapsim.misc.xlscreator.XLSExporter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class starts the runs for the experiments
 */

public class ProblemInstanceRunner {

    public static void main(String[] args) {
        Workbook evaluation_workbook = new XSSFWorkbook();

        int[] seeds = { 5401, 9245, 1589, 9187, 3610, 8594, 5802, 1932, 8175, 8977 };
        int relativeDistance = 0;

        XLSExporter.getInstance().buildEvaluationXLSHeadline(evaluation_workbook);

        for (int k : seeds) {
            runExperiment(k, evaluation_workbook, relativeDistance++, 1.F);

            XLSExporter.getInstance().buildXLSFile("experiment_1", evaluation_workbook);
        }

        Workbook evaluation_workbook_2 = new XSSFWorkbook();

        relativeDistance = 0;

        XLSExporter.getInstance().buildEvaluationXLSHeadline(evaluation_workbook_2);

        for (int j : seeds) {
            runExperiment(j, evaluation_workbook_2, relativeDistance++, 0.1F);
        }

        XLSExporter.getInstance().buildXLSFile("experiment_2", evaluation_workbook_2);

        Workbook evaluation_workbook_3 = new XSSFWorkbook();

        relativeDistance = 0;

        XLSExporter.getInstance().buildEvaluationXLSHeadline(evaluation_workbook_3);

        for (int seed : seeds) {
            runExperiment(seed, evaluation_workbook_3, relativeDistance++, 0.01F);
        }

        XLSExporter.getInstance().buildXLSFile("experiment_3", evaluation_workbook_3);
    }

    public static void runExperiment(Integer seed, Workbook evaluation_workbook, Integer relativeDistance, Float worstCaseMultiplier) {
        System.out.println("Running Instance 1 with Seed: " + seed);
        ProblemInstance1.runProblemInstance1(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 2 with Seed: " + seed);
        ProblemInstance2.runProblemInstance2(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 3 with Seed: " + seed);
        ProblemInstance3.runProblemInstance3(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 4 with Seed: " + seed);
        ProblemInstance4.runProblemInstance4(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 5 with Seed: " + seed);
        ProblemInstance5.runProblemInstance5(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 6 with Seed: " + seed);
         ProblemInstance6.runProblemInstance6(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 7 with Seed: " + seed);
        ProblemInstance7.runProblemInstance7(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 8 with Seed: " + seed);
        ProblemInstance8.runProblemInstance8(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 9 with Seed: " + seed);
        ProblemInstance9.runProblemInstance9(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 10 with Seed: " + seed);
        ProblemInstance10.runProblemInstance10(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 11 with Seed: " + seed);
        ProblemInstance11.runProblemInstance11(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 12 with Seed: " + seed);
        ProblemInstance12.runProblemInstance12(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 13 with Seed: " + seed);
        ProblemInstance13.runProblemInstance13(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 14 with Seed: " + seed);
        ProblemInstance14.runProblemInstance14(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 15 with Seed: " + seed);
        ProblemInstance15.runProblemInstance15(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 16 with Seed: " + seed);
        ProblemInstance16.runProblemInstance16(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 17 with Seed: " + seed);
        ProblemInstance17.runProblemInstance17(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 18 with Seed: " + seed);
        ProblemInstance18.runProblemInstance18(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 19 with Seed: " + seed);
        ProblemInstance19.runProblemInstance19(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 20 with Seed: " + seed);
        ProblemInstance20.runProblemInstance20(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 21 with Seed: " + seed);
        ProblemInstance21.runProblemInstance21(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        System.out.println("Running Instance 22 with Seed: " + seed);
        ProblemInstance22.runProblemInstance22(seed, worstCaseMultiplier, evaluation_workbook, relativeDistance);
        }
}
