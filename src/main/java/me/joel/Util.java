package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;
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
