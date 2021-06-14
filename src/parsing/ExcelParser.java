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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

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
        Date paymentDate = currBill.getPaymentDate();

        String fullDate = getFullDate(paymentDate);
        String monthNameDate = monthNameDate(paymentDate);
        String endMonthDate = getEndOfMonthDate(paymentDate);

        sheet = book.getSheetAt(0);
        setStringValue(1, 13, address);
        setStringValue(2, 1, monthNameDate);
        setStringValue(13, 3, endMonthDate);
        setStringValue(13, 14, fullDate);
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

    private static String getFullDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date) + "г.";
    }

    private static String monthNameDate(Date date) {
        String monthFormat = new SimpleDateFormat("MM").format(date);
        Month month = Month.of(Integer.parseInt(monthFormat));
        Locale rusLocale = Locale.forLanguageTag("ru");

        String monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, rusLocale);
        String yearFormat = new SimpleDateFormat("yyyy").format(date);

        return monthName + " " + yearFormat;
    }

    private static String getEndOfMonthDate(Date date) {
        String dateFormat = new SimpleDateFormat("dd.MM.yyyy").format(date)  + "г.";
        return dateFormat.replace(dateFormat.substring(0, 2), "31");
    }
}
