package me.joel.commands.music;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;

public class Embed {

    // VCRequirement embed
    static final EmbedBuilder VCRequirement = new EmbedBuilder()
            .setColor(Util.randColor())
            .setDescription("You need to be in a voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");

    // sameVCRequirement embed
    static final EmbedBuilder sameVCRequirement = new EmbedBuilder()
            .setColor(Util.randColor())
            .setDescription("You need to be in the same voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");

}
