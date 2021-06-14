import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test {

    public static void main(String[] args) {
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        System.out.println(currentDate);
    }
}
