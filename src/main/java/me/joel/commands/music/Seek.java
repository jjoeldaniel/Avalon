package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Seek extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("seek")) {

            AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack();

            if (track == null) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("No song is playing!")
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            long seconds = 0;
            long minutes = 0;
            long hours = 0;

            if (event.getOption("seconds") != null) seconds = event.getOption("seconds").getAsLong() * 1000;
            if (event.getOption("minutes") != null) minutes = event.getOption("minutes").getAsLong() * 60000;
            if (event.getOption("hours") != null) hours = event.getOption("hours").getAsLong() * 3600000;
            long seek = seconds + minutes + hours;

            if (seek >= track.getDuration() || seek == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("That isn't a valid number!")
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            track.setPosition(seek);

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.green)
                    .setTitle("Seeking at");

            builder.appendDescription("\n`SECONDS:" + seconds/1000 + "`");
            builder.appendDescription("\n`MINUTES:" + minutes/60000 + "`");
            builder.appendDescription("\n`HOURS:" + hours/3600000 + "`");


            event.replyEmbeds(builder.build()).queue();
        }
    }
}
