package me.joel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {


    public static void log(String text) {
        // returns current time in 24hr format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        sdf = new SimpleDateFormat("M/d");
        String str2 = sdf.format(new Date());

        System.out.println("[LOG] [" + str + " " + str2 + "] " + text);
    }

    public static void info(String text) {
        // returns current time in 24hr format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        sdf = new SimpleDateFormat("M/d");
        String str2 = sdf.format(new Date());

        System.out.println("[INFO] [" + str + " " + str2 + "] " + text);
    }


    public static void debug(String text) {
        // returns current time in 24hr format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        sdf = new SimpleDateFormat("M/d");
        String str2 = sdf.format(new Date());

        System.out.println("[DEBUG] [" + str + " " + str2 + "] " + text);
    }

    public static void warn(String text) {
        // returns current time in 24hr format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        sdf = new SimpleDateFormat("M/d");
        String str2 = sdf.format(new Date());

        System.out.println("[WARN] [" + str + " " + str2 + "] " + text);
    }

    public static void line() {
        System.out.println("----------------------------------------");
    }

}
