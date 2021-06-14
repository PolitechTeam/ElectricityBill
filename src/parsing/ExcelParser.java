package parsing;

import Model.Bill;
import Model.User;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class ExcelParser {
    private static XSSFSheet sheet;

    public static XSSFWorkbook openFromXLSX(String fileName) {
        XSSFWorkbook book = null;

        try (InputStream fis = new FileInputStream(fileName)) {
            book = new XSSFWorkbook(fis);
            System.out.println("Файл успешно загружен!");
        } catch (IOException e) {
            e.printStackTrace();
        } return book;
    }

    public static void saveToXLSX(XSSFWorkbook book, String fileName) {
        try (OutputStream fis = new FileOutputStream(fileName)) {
            book.write(fis);
            System.out.println("Файл успешно сохранен!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToXLSX(XSSFWorkbook book, User user, Bill currBill, int prevIndication) {
        final float TARIFF = 1.8f;
        int currIndication = currBill.getIndication();
        String fullName = user.getFullName();
        String address = user.getAddress();

        sheet = book.getSheetAt(0);
        setStringValue(1, 13, address);
        setStringValue(5, 3, fullName);
        setNumericValue(23, 12, TARIFF);
        setNumericValue(34, 4, prevIndication);
        setNumericValue(34, 6, currIndication);

        System.out.println("Данные успешно занесены!");
    }

    public static void saveToPDF(String xlsxFileName, String pdfFileName) {
        try {
            Workbook book = new Workbook(xlsxFileName);

            PdfSaveOptions options = new PdfSaveOptions();
            options.setOnePagePerSheet(true);
            options.setCalculateFormula(true);

            book.save(pdfFileName, options);
            System.out.println("PDF-файл успешно сохранен!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setNumericValue(int rowIndex, int colIndex, float value) {
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(colIndex);
        cell.setCellValue(value);
    }

    private static void setStringValue(int rowIndex, int colIndex, String value) {
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(colIndex);
        cell.setCellValue(value);
    }
}
