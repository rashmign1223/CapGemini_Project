
package GenericUtilites;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {

    static String filePath = "./src/test/resources/DemoWeb1111.xlsx";

    public static String getData(int rowNum, int colNum)
            throws IOException {
        return readDataFromExcel("Sheet1", rowNum, colNum);
    }

    public static int getRowCount() throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet("Sheet1");

            if (sheet == null) {
                throw new IllegalArgumentException("Sheet1 not found.");
            }

            return sheet.getLastRowNum();
        }
    }

    public static String readDataFromExcel(
            String sheetName, int rowNum, int colNum)
            throws IOException {

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new IllegalArgumentException(
                        "Sheet not found: " + sheetName);
            }

            Row row = sheet.getRow(rowNum);

            if (row == null || row.getCell(colNum) == null) {
                return "";
            }

            return new DataFormatter().formatCellValue(
                    row.getCell(colNum)).trim();
        }
    }

    public static String readData(
            String sheetName, int rowNum, int colNum)
            throws IOException {
        return readDataFromExcel(sheetName, rowNum, colNum);
    }

    public static List<Object[]> getData(String sheetName) {
        List<Object[]> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new IllegalArgumentException(
                        "Sheet not found: " + sheetName);
            }

            Row header = sheet.getRow(0);

            if (header == null || header.getLastCellNum() < 1) {
                throw new IllegalArgumentException(
                        "Header row is missing: " + sheetName);
            }

            int columnCount = header.getLastCellNum();
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                Object[] rowData = new Object[columnCount];
                boolean hasData = false;

                for (int j = 0; j < columnCount; j++) {
                    String value = "";

                    if (row.getCell(j) != null) {
                        value = formatter.formatCellValue(
                                row.getCell(j)).trim();
                    }

                    rowData[j] = value;

                    if (!value.isEmpty()) {
                        hasData = true;
                    }
                }

                if (hasData) {
                    rows.add(rowData);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to read Excel file: " + filePath, e);
        }

        return rows;
    }

    public static Object[][] getDataArray(String sheetName) {
        List<Object[]> rows = getData(sheetName);
        return rows.toArray(new Object[0][]);
    }

    // Save credentials in the row matching the TestCaseId.
    // Column I = RegisteredEmail; Column J = RegisteredPassword.
    public static void saveRegisteredCredentials(
            String sheetName,
            String testCaseId,
            String registeredEmail,
            String registeredPassword) {

        try {
            Workbook workbook;

            // Load the workbook before opening the output stream.
            try (FileInputStream fis = new FileInputStream(filePath)) {
                workbook = WorkbookFactory.create(fis);
            }

            try (Workbook wb = workbook) {
                Sheet sheet = wb.getSheet(sheetName);

                if (sheet == null) {
                    throw new IllegalArgumentException(
                            "Sheet not found: " + sheetName);
                }

                Row header = sheet.getRow(0);

                if (header == null) {
                    throw new IllegalArgumentException(
                            "Header row is missing.");
                }

                // Add column headers if they do not exist.
                if (header.getCell(8) == null) {
                    header.createCell(8).setCellValue("RegisteredEmail");
                }

                if (header.getCell(9) == null) {
                    header.createCell(9).setCellValue("RegisteredPassword");
                }

                boolean found = false;

                // Find the correct row using TestCaseId in column A.
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    if (row == null || row.getCell(0) == null) {
                        continue;
                    }

                    String currentId = new DataFormatter()
                            .formatCellValue(row.getCell(0)).trim();

                    if (currentId.equalsIgnoreCase(testCaseId.trim())) {
                        if (row.getCell(8) == null) {
                            row.createCell(8);
                        }

                        if (row.getCell(9) == null) {
                            row.createCell(9);
                        }

                        row.getCell(8).setCellValue(registeredEmail);
                        row.getCell(9).setCellValue(registeredPassword);

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    throw new IllegalArgumentException(
                            "TestCaseId not found in Excel: " + testCaseId);
                }

                try (FileOutputStream fos =
                             new FileOutputStream(filePath)) {
                    wb.write(fos);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to save registered credentials.", e);
        }
    }
}
