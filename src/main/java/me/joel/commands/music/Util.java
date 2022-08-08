package me.joel.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
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
        member = event.getMember();
    }

    /**
     * @return Event member
     */
    public static Member getMember() {
        return member;
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
