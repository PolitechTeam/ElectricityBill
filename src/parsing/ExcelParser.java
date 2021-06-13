package parsing;

import Model.Bill;
import Model.User;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class ExcelParser {
    public static XSSFWorkbook open(String fileName) {
        XSSFWorkbook book = null;

        try (InputStream fis = new FileInputStream(fileName)) {
            book = new XSSFWorkbook(fis);
            System.out.println("Файл успешно загружен!");
        } catch (IOException e) {
            e.printStackTrace();
        } return book;
    }

    public static void save(XSSFWorkbook book, String fileName) {
        try (OutputStream fis = new FileOutputStream(fileName)) {
            book.write(fis);
            System.out.println("Файл успешно сохранен!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(XSSFWorkbook book, User user, Bill currBill, int prevIndication) {
        int currIndication = currBill.getIndication();

        String fullName = user.getFullName();
        String address = user.getAddress();

        float tariff = 1.8f;
        int energyCount = currIndication - prevIndication;
        float fullPrice = energyCount * tariff;

        XSSFSheet sheet = book.getSheetAt(0);
        setCellValue(sheet, 1, 13, address);
        setCellValue(sheet, 5, 3, fullName);

        setCellValue(sheet, 9, 24, fullPrice);
        setCellValue(sheet, 14, 24, fullPrice);

        setCellValue(sheet, 23, 4, fullPrice);
        setCellValue(sheet, 23, 8, energyCount);
        setCellValue(sheet, 23, 12, tariff);
        setCellValue(sheet, 23, 14, fullPrice);
        setCellValue(sheet, 23, 18, fullPrice);
        setCellValue(sheet, 23, 25, fullPrice);

        setCellValue(sheet, 25, 4, fullPrice);
        setCellValue(sheet, 25, 14, fullPrice);
        setCellValue(sheet, 25, 18, fullPrice);
        setCellValue(sheet, 25, 25, fullPrice);

        setCellValue(sheet, 26, 25, fullPrice);

        setCellValue(sheet, 34, 4, prevIndication);
        setCellValue(sheet, 34, 6, currIndication);
        setCellValue(sheet, 34, 8, energyCount);

        System.out.println("Данные успешно занесены!");
    }

    private static void setCellValue(XSSFSheet sheet, int rowIndex, int colIndex, float value) {
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(colIndex);
        cell.setCellValue(value);
    }

    private static void setCellValue(XSSFSheet sheet, int rowIndex, int colIndex, String value) {
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(colIndex);
        cell.setCellValue(value);
    }
}
