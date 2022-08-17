package me.joel.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

/**
 * Utility class
 */
public class Util extends ListenerAdapter {

    /**
     * Validates member voice state
     * @param member Compared member
     * @return VCReq embed if member is not in VC, sameVCReq embed if in different VC, null if neither
     */
    public static EmbedBuilder compareVoice(Member member, Member bot) {

        EmbedBuilder builder = null;

        // Checks requester voice state
        if (!member.getVoiceState().inAudioChannel()) {
            builder = VCRequirement;
            return builder;
        }

        // Compare bot and member voice state
        if (bot.getVoiceState().inAudioChannel()) {

            if (bot.getVoiceState().getChannel() != member.getVoiceState().getChannel()) {
                builder = sameVCRequirement;
            }
            return builder;
        }

        return null;
    }

    public static Member getAvalon(Guild guild) {
        return guild.getSelfMember();
    }

    /**
     * User is required to be in VC
     */
    public static final EmbedBuilder VCRequirement = new EmbedBuilder()
            .setColor(Color.red)
            .setDescription("You need to be in a voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");

    /**
     * User is required to be in same VC as bot
     */
    public static final EmbedBuilder sameVCRequirement = new EmbedBuilder()
            .setColor(Color.red)
            .setDescription("You need to be in the same voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");


}
