package me.joel.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class
 */
public class Util extends ListenerAdapter {

    private static Member member;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equalsIgnoreCase("play")) {
            member = event.getMember();
        }
    }

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

    /**
     * /play member
     * @return Event member
     */
    public static Member getMember() {
        return member;
    }

    public static Member getAvalon(Guild guild) {
        return guild.retrieveMemberById("971239438892019743").complete();
    }

    /**
     * User is required to be in VC
     */
    public static final EmbedBuilder VCRequirement = new EmbedBuilder()
            .setColor(me.joel.Util.randColor())
            .setDescription("You need to be in a voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");

    /**
     * User is required to be in same VC as bot
     */
    public static final EmbedBuilder sameVCRequirement = new EmbedBuilder()
            .setColor(me.joel.Util.randColor())
            .setDescription("You need to be in the same voice channel to use this command!")
            .setFooter("Use /help for a list of music commands!");


}
