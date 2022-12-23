package periodicallyprogramming.avalon;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {

    /**
     ** Current time + date
     * @return 24hr time in HH:mm:ss and Date in M/d format
     */
    private static String getTime() {
        // returns current time in 24hr format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        sdf = new SimpleDateFormat("M/d/yy");
        String str2 = sdf.format(new Date());

        return str + " | " + str2;
    }

    public static void log(String text) {
        System.out.println("[LOG] [" + getTime() + "] " + text);
    }

    public static void info(String text) {
        System.out.println("[INFO] [" + getTime() + "] " + text);
    }


    public static void debug(String text) {
        System.out.println("[DEBUG] [" + getTime() + "] " + text);
    }

    public static void warn(String text) {
        System.out.println("[WARN] [" + getTime() + "] " + text);
    }

    public static void line() {
        System.out.println("----------------------------------------");
    }

}
