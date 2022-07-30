package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Util
{

    // Random number from 0 - i
    public static int randomWithRange(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt(min, max);
    }

    // Random dog thumbnail
    public static String randomThumbnail()
    {
        ArrayList<String> dogs = new ArrayList<>();
        dogs.add("https://c.tenor.com/HxNhwwXRcdEAAAAj/brickhill-bh.gif");
        dogs.add("https://c.tenor.com/eRTjXVkts5kAAAAj/pug-dog.gif");
        dogs.add("https://c.tenor.com/BpGpliwaBcMAAAAj/dogjam-dog.gif");
        dogs.add("https://c.tenor.com/l6wuj9Zdl6wAAAAj/dancing-doge.gif");
        dogs.add("https://c.tenor.com/V4jrINyqhGcAAAAj/dance-dancing.gif");
        dogs.add("https://c.tenor.com/0vy31sXqwYcAAAAj/dog-doggo.gif");
        dogs.add("https://c.tenor.com/9TsiJq--SsEAAAAj/doge-dance.gif");

        int num = randomWithRange(0, dogs.size());
        return dogs.get(num);
    }

    // Generic error
    public static EmbedBuilder genericError()
    {
        return new EmbedBuilder()
                .setDescription("An error has occurred!")
                .setColor(randColor())
                .setFooter("Use /help for the commands list");
    }

    // Random color
    public static Color randColor()
    {
        int num1 = randomWithRange(0, 255);
        int num2 = randomWithRange(0, 255);
        int num3 = randomWithRange(0, 255);
        return new Color(num1, num2, num3);
    }

    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

}
