package me.joel;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

public class Util {

    // Random number from 0 - i
    public static int randomWithRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(min, max);
    }

    // Random color
    public static Color randColor() {
        int num1 = randomWithRange(0, 255);
        int num2 = randomWithRange(0, 255);
        int num3 = randomWithRange(0, 255);
        return new Color(num1, num2, num3);
    }

    // Thumbnail Builder for YouTube
    public static String getThumbnail(String link) {

        int linkLength = link.length() + 1;
        String linkPrefix = "https://img.youtube.com/vi/";
        String linkSuffix = "/0.jpg";
        StringBuilder stringBuilder = new StringBuilder()
                .append(link)
                .delete(0, linkLength - 12);
        String videoID = stringBuilder.toString();

        return linkPrefix + videoID + linkSuffix;
    }


    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

}
