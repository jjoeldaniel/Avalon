package me.joel.commands.guild;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Toggle extends ListenerAdapter {

    private static boolean insults = false;
    private static boolean gmgn = false;
    private static boolean nowPlaying = true;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("toggle")) {

            var sub_invoke = event.getSubcommandName();

            switch (sub_invoke) {
                case ("insults") -> {
                    insults = !insults;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.green)
                            .setDescription("Insults are now toggled `ON`");

                    if (!insults) {
                        builder.setColor(Color.red);
                        builder.setDescription("Insults are now toggled `OFF`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
                case ("goodmorning_goodnight") -> {
                    gmgn = !gmgn;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.green)
                            .setDescription("Messages are now toggled `ON`");

                    if (!gmgn) {
                        builder.setColor(Color.red);
                        builder.setDescription("Messages are now toggled `OFF`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
                case ("now_playing") -> {
                    nowPlaying = !nowPlaying;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.green)
                            .setDescription("`Now Playing` messages are now toggled `ON`");

                    if (!nowPlaying) {
                        builder.setColor(Color.red);
                        builder.setDescription("`Now Playing` messages are now toggled `OFF`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }
        }
    }

    public static boolean insults() {
        return insults;
    }

    public static boolean gmgn() {
        return gmgn;
    }

    public static boolean isNowPlaying() {
        return nowPlaying;
    }
}
