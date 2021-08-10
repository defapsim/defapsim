package com.defapsim.misc.xlscreator;

import com.defapsim.application.Application;
import com.defapsim.application.Component;
import com.defapsim.evaluation.Evaluation;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.Device;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * This class is built according to the Expression Builder pattern and allows the creation of XLS files, which can be used for the evaluation
 */
public class XLSExporter {

    /**
     * Private static Singleton instance
     */
    private static final XLSExporter instance = new XLSExporter();

    /**
     * Private constructor to prevent it from being accessed
     */
    private XLSExporter() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static XLSExporter getInstance() {
        return instance;
    }

    private Workbook workbook = new XSSFWorkbook();


    public XLSExporter buildInfrastructureXLSBlockHeadline(Integer seed, Workbook workbook, Integer relativeDistance) {

        if(workbook == null)
            return new XLSExporter();

        Sheet sheetInfrastructure = workbook.getSheet("infrastructure");

        if(sheetInfrastructure == null)
            sheetInfrastructure = workbook.createSheet("infrastructure");

        // SEED
        Row headerSeedRow = sheetInfrastructure.getRow(0);

        if(headerSeedRow == null)
            headerSeedRow = sheetInfrastructure.createRow(0);

        Font headerSeedFont = workbook.createFont();
        headerSeedFont.setFontHeightInPoints((short) 12);

        CellStyle headerSeedCellStyle = workbook.createCellStyle();
        headerSeedCellStyle.setFont(headerSeedFont);
        headerSeedCellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.index);
        //headerSeedCellStyle.set(IndexedColors.GREY_50_PERCENT.index);
        headerSeedCellStyle.setAlignment(HorizontalAlignment.CENTER);

        Cell seedCell1 = headerSeedRow.createCell((relativeDistance == 0 ? 0 : (4 * relativeDistance) + 1));
        seedCell1.setCellValue("Seed " + seed);
        seedCell1.setCellStyle(headerSeedCellStyle);

        sheetInfrastructure.addMergedRegion(new CellRangeAddress(0,0,
                (relativeDistance == 0 ? 0 : (4 * relativeDistance) + 1),
                (relativeDistance == 0 ? 3 : 4 * (relativeDistance + 1))));

        // PARAMETER
        Row headerParametersRow = sheetInfrastructure.getRow(1);

        if(headerParametersRow == null)
            headerParametersRow = sheetInfrastructure.createRow(1);

        List<String> columns = new LinkedList<>() {{
            this.add("Name");
            this.add("Memory");
            this.add("Computing power");
            this.add("Processing speed");
        }};

        Font headerParameterFont = workbook.createFont();
        headerParameterFont.setBold(true);
        headerParameterFont.setFontHeightInPoints((short) 12);

        CellStyle headerParameterCellStyle = workbook.createCellStyle();
        headerParameterCellStyle.setFont(headerParameterFont);
        headerParameterCellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = (relativeDistance == 0 ? 0 : (4 * relativeDistance) + 1), columnspos = 0; i <= (relativeDistance == 0 ? 3 : 4 * (relativeDistance + 1)); i++, columnspos++) {
            Cell cell = headerParametersRow.createCell(i);
            cell.setCellValue(columns.get(columnspos));
            cell.setCellStyle(headerParameterCellStyle);
        }

        return this;
    }


    public void buildInfrastructureXLSBlockDevices(String phaseName, List<Device> deviceList, Workbook workbook, Integer relativeX, Integer relativeY) {

        if(workbook == null)
            return;

        Sheet sheetInfrastructure = workbook.getSheet("infrastructure");

        if(sheetInfrastructure == null)
            sheetInfrastructure = workbook.createSheet("infrastructure");

        //PHASE NAME
        Row headerPhaseRow = sheetInfrastructure.getRow(relativeY);

        if(headerPhaseRow == null)
            headerPhaseRow = sheetInfrastructure.createRow(relativeY);

        Font headerPhaseFont = workbook.createFont();
        headerPhaseFont.setFontHeightInPoints((short) 12);

        CellStyle headerPhaseCellStyle = workbook.createCellStyle();
        headerPhaseCellStyle.setFont(headerPhaseFont);
        headerPhaseCellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.index);
        headerPhaseCellStyle.setAlignment(HorizontalAlignment.CENTER);

        Cell phaseCell = headerPhaseRow.createCell((relativeX == 0 ? 0 : (4 * relativeX) + 1));
        phaseCell.setCellValue("Phase: " + phaseName);
        phaseCell.setCellStyle(headerPhaseCellStyle);

        sheetInfrastructure.addMergedRegion(new CellRangeAddress(relativeY, relativeY,
                (relativeX == 0 ? 0 : (4 * relativeX) + 1),
                (relativeX == 0 ? 3 : 4 * (relativeX + 1))));

        CellStyle inputCellStyle = workbook.createCellStyle();
        inputCellStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum = relativeY + 1;
        for (Device device : deviceList) {

            Row row = sheetInfrastructure.getRow(rowNum);
            if(row == null)
                row = sheetInfrastructure.createRow(rowNum);

            int cellPos = (relativeX == 0 ? 0 : (4 * relativeX) + 1);

            row.createCell(cellPos++).setCellValue(device.getIdentifier());

            row.createCell(cellPos).setCellValue(device.getMemory());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(device.getComputingPower());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(device.getProcessingSpeed());
            row.getCell(cellPos).setCellStyle(inputCellStyle);
            rowNum++;
        }

        for(int i = 0; i <= (relativeX == 0 ? 3 : 4 * (relativeX + 1)); i++) {
            sheetInfrastructure.autoSizeColumn(i);
        }
    }

    public XLSExporter buildApplicationXLSBlockHeadline(Integer seed, Workbook workbook, Integer relativeDistance) {

        if(workbook == null)
            return new XLSExporter();

        Sheet sheetApplication = workbook.getSheet("application");

        if(sheetApplication == null)
            sheetApplication = workbook.createSheet("application");

        // SEED
        Row headerSeedRow = sheetApplication.getRow(0);

        if(headerSeedRow == null)
            headerSeedRow = sheetApplication.createRow(0);

        Font headerSeedFont = workbook.createFont();
        headerSeedFont.setFontHeightInPoints((short) 12);

        CellStyle headerSeedCellStyle = workbook.createCellStyle();
        headerSeedCellStyle.setFont(headerSeedFont);
        headerSeedCellStyle.setAlignment(HorizontalAlignment.CENTER);

        Cell seedCell1 = headerSeedRow.createCell((relativeDistance == 0 ? 0 : (4 * relativeDistance) + 1));
        seedCell1.setCellValue("Seed " + seed);
        seedCell1.setCellStyle(headerSeedCellStyle);

        sheetApplication.addMergedRegion(new CellRangeAddress(0,0,
                (relativeDistance == 0 ? 0 : (4 * relativeDistance) + 1),
                (relativeDistance == 0 ? 3 : 4 * (relativeDistance + 1))));

        // PARAMETER
        Row headerParametersRow = sheetApplication.getRow(1);

        if(headerParametersRow == null)
            headerParametersRow = sheetApplication.createRow(1);

        List<String> columns = new LinkedList<>() {{
            this.add("Name");
            this.add("Memory demand");
            this.add("Computing power demand");
            this.add("Worst-case execution time");
        }};

        Font headerParameterFont = workbook.createFont();
        headerParameterFont.setBold(true);
        headerParameterFont.setFontHeightInPoints((short) 12);

        CellStyle headerParameterCellStyle = workbook.createCellStyle();
        headerParameterCellStyle.setFont(headerParameterFont);
        headerParameterCellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = (relativeDistance == 0 ? 0 : (4 * relativeDistance) + 1), columnspos = 0; i <= (relativeDistance == 0 ? 3 : 4 * (relativeDistance + 1)); i++, columnspos++) {
            Cell cell = headerParametersRow.createCell(i);
            cell.setCellValue(columns.get(columnspos));
            cell.setCellStyle(headerParameterCellStyle);
        }

        return this;
    }


    public void buildApplicationXLSBlockComponents(String phaseName, List<Component> componentList, Workbook workbook, Integer relativeX, Integer relativeY) {
        Sheet sheetInfrastructure = workbook.getSheet("application");

        if(sheetInfrastructure == null)
            sheetInfrastructure = workbook.createSheet("application");

        //PHASE NAME
        Row headerPhaseRow = sheetInfrastructure.getRow(relativeY);

        if(headerPhaseRow == null)
            headerPhaseRow = sheetInfrastructure.createRow(relativeY);

        Font headerPhaseFont = workbook.createFont();
        headerPhaseFont.setFontHeightInPoints((short) 12);

        CellStyle headerPhaseCellStyle = workbook.createCellStyle();
        headerPhaseCellStyle.setFont(headerPhaseFont);
        headerPhaseCellStyle.setAlignment(HorizontalAlignment.CENTER);

        Cell phaseCell = headerPhaseRow.createCell((relativeX == 0 ? 0 : (4 * relativeX) + 1));
        phaseCell.setCellValue("Phase: " + phaseName);
        phaseCell.setCellStyle(headerPhaseCellStyle);

        sheetInfrastructure.addMergedRegion(new CellRangeAddress(relativeY, relativeY,
                (relativeX == 0 ? 0 : (4 * relativeX) + 1),
                (relativeX == 0 ? 3 : 4 * (relativeX + 1))));

        //COMPONENTS
        CellStyle inputCellStyle = workbook.createCellStyle();
        inputCellStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum = relativeY + 1;
        for (Component component : componentList) {

            Row row = sheetInfrastructure.getRow(rowNum);
            if(row == null)
                row = sheetInfrastructure.createRow(rowNum);

            int cellPos = (relativeX == 0 ? 0 : (4 * relativeX) + 1);

            row.createCell(cellPos++).setCellValue(component.getIdentifier());

            row.createCell(cellPos).setCellValue(component.getMemoryDemand());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(component.getComputingPowerDemand());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(component.getWorstCaseExecutionTime());
            row.getCell(cellPos).setCellStyle(inputCellStyle);
            rowNum++;
        }

        for(int i = 0; i <= (relativeX == 0 ? 3 : 4 * (relativeX + 1)); i++) {
            sheetInfrastructure.autoSizeColumn(i);
        }
    }



    public XLSExporter buildEvaluationXLSHeadline(Workbook workbook) {

        Sheet sheetEvaluation = workbook.getSheet("evaluation");

        if(sheetEvaluation == null)
            sheetEvaluation = workbook.createSheet("evaluation");

        // HEADLINE
        Row headerParametersRow = sheetEvaluation.getRow(0);

        if(headerParametersRow == null)
            headerParametersRow = sheetEvaluation.createRow(0);

        List<String> columns = new LinkedList<>() {{
            this.add("Seed");
            this.add("ProblemInstance");
            this.add("Cloud servers");
            this.add("Fog nodes");
            this.add("End devices");
            this.add("Components");
            this.add("AlgorithmType");
            this.add("Initial application latency");
            this.add("Application latency after execution");
            this.add("Improvement in %");
            this.add("Amount of total migrations (migrations and trades)");
            this.add("Execution time of the algorithm");
            this.add("Amount of auctions");
            this.add("Amount of migrations");
            this.add("Amount of trades");
        }};

        Font headerParameterFont = workbook.createFont();
        headerParameterFont.setBold(true);
        headerParameterFont.setFontHeightInPoints((short) 12);

        CellStyle headerParameterCellStyle = workbook.createCellStyle();
        headerParameterCellStyle.setFont(headerParameterFont);
        headerParameterCellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int columnpos = 0; columnpos < columns.size(); columnpos++) {
            Cell cell = headerParametersRow.createCell(columnpos);
            cell.setCellValue(columns.get(columnpos));
            cell.setCellStyle(headerParameterCellStyle);
        }
        return this;
    }

    public void buildEvaluationXLSAlgorithmRow(Workbook workbook, Integer seed, Integer problemInstance, List<Evaluation> evaluationList, Integer relativeY) {

        relativeY = (problemInstance - 1) * 7 + 1 + (relativeY * 22 * 7);

        Sheet sheetEvaluation = workbook.getSheet("evaluation");

        if(sheetEvaluation == null)
            sheetEvaluation = workbook.createSheet("evaluation");

        CellStyle inputCellStyle = workbook.createCellStyle();
        inputCellStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum = relativeY + 1;
        for (Evaluation evaluation : evaluationList) {
            Row row = sheetEvaluation.getRow(rowNum);
            if(row == null)
                row = sheetEvaluation.createRow(rowNum);

            int cellPos = 0;

            row.createCell(cellPos).setCellValue(seed);
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(problemInstance);
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getCloudServers());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getFogNodes());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getEndDevices());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getApplicationComponents());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getAlgorithmName());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getApplicationLatencyInitial());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getApplicationLatencyAfterOptimization());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(100 - evaluation.getApplicationLatencyAfterOptimization() / evaluation.getApplicationLatencyInitial() * 100);
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getAmountOfCombinedMigrations());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getExecutionTimeOfTheAlgorithm());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getAmountOfAuctions());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getAmountOfMigrations());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);

            row.createCell(cellPos).setCellValue(evaluation.getAmountOfTrades());
            row.getCell(cellPos++).setCellStyle(inputCellStyle);
            rowNum++;
        }

        for(int i = 0; i < 15; i++) {
            sheetEvaluation.autoSizeColumn(i);
        }
    }

    public void buildXLSFile(String name, Workbook workbook) {
        FileOutputStream fileOut;
        try {
            File directory = new File("evaluation");
            if (!directory.exists()){
                directory.mkdir();
            }
            fileOut = new FileOutputStream("evaluation/" + name + ".xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }































    public XLSExporter buildInfrastructureXLSSheet(Infrastructure infrastructure) {

        Sheet sheetInfrastructure = workbook.createSheet("infrastructure");

        Row headerRow = sheetInfrastructure.createRow(0);

        List<String> columns = new LinkedList<>() {{
            this.add("Name");
            this.add("RAM (GB)");
            this.add("ComputingPower (vCPU)");
            this.add("ProcessingSpeed");
        }};

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillBackgroundColor((short) 10);
        headerCellStyle.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.index);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle inputCellStyle = workbook.createCellStyle();
        inputCellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for (Device device : infrastructure.getDevices()) {
            Row row = sheetInfrastructure.createRow(rowNum++);
            row.createCell(0).setCellValue(device.getIdentifier());
            row.createCell(1).setCellValue(device.getMemory());
            row.getCell(1).setCellStyle(inputCellStyle);
            row.createCell(2).setCellValue(device.getComputingPower());
            row.getCell(2).setCellStyle(inputCellStyle);
            row.createCell(3).setCellValue(device.getProcessingSpeed());
            row.getCell(3).setCellStyle(inputCellStyle);
        }

        for(int i = 0; i < columns.size(); i++) {
            sheetInfrastructure.autoSizeColumn(i);
        }
        return this;
    }

    public XLSExporter buildApplicationXLSSheet(Application application) {

        Sheet sheetApplication = workbook.createSheet("application");

        Row headerRow = sheetApplication.createRow(0);

        List<String> columns = new LinkedList<>() {{
            this.add("Name");
            this.add("RAM demand (GB)");
            this.add("ComputingPower demand (vCPU)");
            this.add("Worst-case execution time (ms)");
        }};

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle inputCellStyle = workbook.createCellStyle();
        inputCellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for (Component component : application.getComponents()) {
            Row row = sheetApplication.createRow(rowNum++);
            row.createCell(0).setCellValue(component.getIdentifier());
            row.createCell(1).setCellValue(component.getMemoryDemand());
            row.getCell(1).setCellStyle(inputCellStyle);
            row.createCell(2).setCellValue(component.getComputingPowerDemand());
            row.getCell(2).setCellStyle(inputCellStyle);
            row.createCell(3).setCellValue(component.getWorstCaseExecutionTime());
            row.getCell(3).setCellStyle(inputCellStyle);
            row.setRowStyle(inputCellStyle);
        }

        for(int i = 0; i<columns.size(); i++) {
            sheetApplication.autoSizeColumn(i);
        }
        return this;
    }

    public XLSExporter buildEvaluationXLSSheet(Evaluation evaluation) {

        Sheet sheetApplication = workbook.createSheet(evaluation.getAlgorithmName() + "-Evaluation");

        Row headerRow = sheetApplication.createRow(1);

        Row rowForAlgorithmName = sheetApplication.createRow(0);
        rowForAlgorithmName.createCell(0).setCellValue("Algorithm name:");
        rowForAlgorithmName.createCell(1).setCellValue(evaluation.getAlgorithmName());

        List<String> columns = new LinkedList<>() {{
            this.add("Initial application latency");
            this.add("Application latency after execution");
            this.add("Improvement in %");
            this.add("Amount of migrations");
            this.add("Execution time of the algorithm (ms)");
        }};

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle inputCellStyle = workbook.createCellStyle();
        inputCellStyle.setAlignment(HorizontalAlignment.CENTER);

        /*CellStyle inputCellStyle2 = workbook.createCellStyle();
        inputCellStyle2.setAlignment(HorizontalAlignment.CENTER);
        DataFormat format = workbook.createDataFormat();
        inputCellStyle2.setDataFormat(format.getFormat("#.#"));*/

        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 2;
        Row row = sheetApplication.createRow(rowNum);
        row.createCell(0).setCellValue(evaluation.getApplicationLatencyInitial());
        row.getCell(0).setCellStyle(inputCellStyle);
        row.createCell(1).setCellValue(evaluation.getApplicationLatencyAfterOptimization());
        row.getCell(1).setCellStyle(inputCellStyle);
        row.createCell(2).setCellValue(100 - evaluation.getApplicationLatencyAfterOptimization() / evaluation.getApplicationLatencyInitial() * 100);
        row.getCell(2).setCellStyle(inputCellStyle);
        row.createCell(3).setCellValue(evaluation.getAmountOfCombinedMigrations());
        row.getCell(3).setCellStyle(inputCellStyle);
        row.createCell(4).setCellValue(evaluation.getExecutionTimeOfTheAlgorithm());
        row.getCell(4).setCellStyle(inputCellStyle);
        row.setRowStyle(inputCellStyle);

        for(int i = 0; i<columns.size(); i++) {
            sheetApplication.autoSizeColumn(i);
        }
        return this;
    }

    public void buildXLSFile(String name) {
        FileOutputStream fileOut;
        try {
            File directory = new File("probleminstances");
            if (!directory.exists()){
                directory.mkdir();
            }
            fileOut = new FileOutputStream("probleminstances/" +name + ".xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
