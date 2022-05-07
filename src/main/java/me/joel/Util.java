package me.joel;

import java.util.Random;

public class Util {

    public static int randomWithRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(min, max);
    }

}
