package me.joel;

public class Console {


    public static void log(String text) {
        System.out.println("[LOG] " + text);
    }

    public static void log(String text, Object object) {
        System.out.println("[LOG] " + text + " : " + object);
    }


    public static void info(String text) {
        System.out.println("[INFO] " + text);
    }

    public static void info(String text, Object object) {
        System.out.println("[INFO] " + text + " : " + object);
    }


    public static void debug(String text) {
        System.out.println("[DEBUG] " + text);
    }

    public static void debug(String text, Object object) {
        System.out.println("[DEBUG] " + text + " : " + object);
    }


    public static void warn(String text) {
        System.out.println("[WARN] " + text);
    }

    public static void warn(String text, Object object) {
        System.out.println("[WARN] " + text + " : " + object);
    }

    public static void line() {
        System.out.println("----------------------------------------");
    }

}
