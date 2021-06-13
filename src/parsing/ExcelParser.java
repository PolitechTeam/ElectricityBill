package parsing;

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
        } catch (IOException e) {
            e.printStackTrace();
        } return book;
    }

    public static void save(XSSFWorkbook book, String fileName) {
        try (OutputStream fis = new FileOutputStream(fileName)) {
            book.write(fis);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(XSSFWorkbook book) {
        XSSFSheet sheet = book.getSheetAt(0);
        XSSFRow row = sheet.getRow(9);
        XSSFCell cell = row.getCell(24);

        System.out.println(getCellValue(cell));
        cell.setCellValue(100);
        System.out.println(getCellValue(cell));
    }

    private static String getCellValue(XSSFCell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return String.valueOf(cell.getCellType());
        }
    }
}
