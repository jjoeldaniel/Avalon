package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Util {

    /**
     * Random int
     * @param min Floor
     * @param max Ceiling
     * @return Int in range of floor and ceiling
     */
    public static int randomWithRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(min, max);
    }

    /**
     * Finds a guild channel
     * @param query Channel name
     * @param guild Guild
     * @return The first channel ID containing query
     */
    public static String findChannel(String query, Guild guild) {
        int channelNum = guild.getTextChannels().size();
        String channelID = null;

        for (int i = 0; i < channelNum; ++i) {
            if (guild.getTextChannels().get(i).getName().contains(query)) {
                channelID = guild.getTextChannels().get(i).getId();
                break;
            }
        }
        return channelID;
    }


    /**
     * Random dog thumbnail
     * @return Thumbnail URL
     */
    public static String randomThumbnail() {
        ArrayList<String> dogs = new ArrayList<>();

        dogs.add("https://c.tenor.com/HxNhwwXRcdEAAAAj/brickhill-bh.gif");
        dogs.add("https://c.tenor.com/eRTjXVkts5kAAAAj/pug-dog.gif");
        dogs.add("https://c.tenor.com/BpGpliwaBcMAAAAj/dogjam-dog.gif");
        dogs.add("https://c.tenor.com/l6wuj9Zdl6wAAAAj/dancing-doge.gif");
        dogs.add("https://c.tenor.com/V4jrINyqhGcAAAAj/dance-dancing.gif");
        dogs.add("https://c.tenor.com/0vy31sXqwYcAAAAj/dog-doggo.gif");
        dogs.add("https://c.tenor.com/9TsiJq--SsEAAAAj/doge-dance.gif");
        dogs.add("https://c.tenor.com/IgknKg_YnbgAAAAC/fluffy-cute.gif");
        dogs.add("https://c.tenor.com/_4xCiEhhoZsAAAAd/dog-smile.gif");
        dogs.add("https://c.tenor.com/F5KHwoW46WQAAAAd/hotdog.gif");
        dogs.add("https://c.tenor.com/nEsdZ0qa6QcAAAAd/dog.gif");
        dogs.add("https://c.tenor.com/lfLqrldlPO0AAAAd/bee-dog-dog-bee.gif");
        dogs.add("https://c.tenor.com/he7fisueH2QAAAAd/puppy-cute.gif");
        dogs.add("https://c.tenor.com/f-paU3H3PAMAAAAd/dog-happy.gif");
        dogs.add("https://c.tenor.com/X85gBisS1bAAAAAd/dog-looking.gif");

        int num = randomWithRange(0, dogs.size());
        return dogs.get(num);
    }

    /**
     * Generic error
     * @return Error embed
     */
    public static EmbedBuilder genericError() {
        return new EmbedBuilder()
                .setDescription("An error has occurred!")
                .setColor(Color.red)
                .setFooter("Use /help for the commands list");
    }

    /**
     * Generates colors
     * @return Color
     */
    public static Color randColor() {
        int num1 = randomWithRange(0, 255);
        int num2 = randomWithRange(0, 255);
        int num3 = randomWithRange(0, 255);
        return new Color(num1, num2, num3);
    }

}
