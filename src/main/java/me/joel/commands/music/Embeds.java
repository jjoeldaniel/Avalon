package me.joel.commands.music;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Utility class containing useful embeds
 */
public class Embeds {

    /**
     * User is required to be in VC
     */
    static final EmbedBuilder VCRequirement = new EmbedBuilder()
            .setColor(Util.randColor())
            .setDescription("You need to be in a voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");

    /**
     * User is required to be in same VC as bot
     */
    static final EmbedBuilder sameVCRequirement = new EmbedBuilder()
            .setColor(Util.randColor())
            .setDescription("You need to be in the same voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");

}
