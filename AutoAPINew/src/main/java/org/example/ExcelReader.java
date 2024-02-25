package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    public List<TestCase> readTestCaseFromExcel() throws Exception {
        FileInputStream fis = new FileInputStream(new File("D:\\Automation\\CustAutomation\\AutoAPI.xlsx"));
//creating workbook instance that refers to .xls file
        XSSFWorkbook wb = new XSSFWorkbook(fis);
//creating a Sheet object to retrieve the object
        XSSFSheet sheet = wb.getSheetAt(0);
//evaluating cell type
        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
        List<TestCase> testCases = new ArrayList<>();
        int indexRow = -1;
        for (Row row : sheet) {
            indexRow = indexRow + 1;
            if (indexRow == 0)
                continue;
            TestCase testCase = new TestCase();
            for (Cell cell : row) {
                String rowValue = "";
                switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        rowValue = String.valueOf(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                        rowValue = cell.getStringCellValue();
                        break;
                }
                switch (cell.getColumnIndex()) {
                    case 0:
                        testCase.setUrl(rowValue);
                        break;
                    case 1:
                        testCase.setMethod(rowValue);
                        break;
                    case 2:
                        testCase.setHeader(rowValue);
                        break;
                    case 3:
                        testCase.setBody(rowValue);
                        break;
                    case 4:
                        testCase.setResponseCode((int) Double.parseDouble(rowValue));
                        break;
                    case 5:
                        testCase.setResponse(rowValue);
                        break;
                }
            }
            testCases.add(testCase);
        }
        return testCases;
    }
}
