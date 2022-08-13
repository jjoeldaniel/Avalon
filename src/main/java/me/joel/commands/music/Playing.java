package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.PlayerManager;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Playing extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("playing")) {

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();

            if (track == null) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("No song is playing!")
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = nowPlaying(track);
            builder.setFooter("");

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();

        }
    }

    private static String getTrackTime(AudioTrack track) {
        int totalSeconds = (int) (Math.ceil(track.getPosition()) / 1000);
        int totalMinutes = 0;
        int totalHours = 0;

        // seconds -> minutes, minutes -> hours
        while (totalSeconds >= 60) {
            totalSeconds = totalSeconds - 60;
            totalMinutes++;
        }
        while (totalMinutes >= 60) {
            totalMinutes = totalMinutes - 60;
            totalHours++;
        }

        String totalSecondsString = String.valueOf(totalSeconds);
        if (totalSeconds < 10) totalSecondsString = "0" + totalSecondsString;

        if (totalHours > 0) {
            String totalMinutesString = String.valueOf(totalMinutes);
            if (totalMinutes < 10) totalMinutesString = "0" + totalMinutesString;

            String totalHoursString = String.valueOf(totalHours);
            if (totalHours < 10) totalHoursString = "0" + totalHoursString;

            return "[" + totalHoursString + ":" + totalMinutesString + ":" + totalSecondsString + "]";
        }

        return "[" + totalMinutes + ":" + totalSecondsString + "]";
    }

    public static EmbedBuilder nowPlaying(AudioTrack track) {
        // Time from ms to m:s
        long trackLength = track.getInfo().length;
        long minutes = (trackLength / 1000) / 60;
        long seconds = ((trackLength / 1000) % 60);
        long hours = 0;

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        String songHours = String.valueOf(hours);
        if (hours < 10) songHours = "0" + hours;
        String songMinutes = String.valueOf(minutes);
        if (minutes < 10) songMinutes = "0" + minutes;
        String songSeconds = String.valueOf(seconds);
        if (seconds < 10) songSeconds = "0" + seconds;

        // Thumbnail
        String trackThumbnail = PlayerManager.getThumbnail(track.getInfo().uri);

        // Embed
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setAuthor("Now Playing")
                .setTitle(track.getInfo().title, track.getInfo().uri)
                .setDescription("`" + getTrackTime(track) + " / [" + songMinutes + ":" + songSeconds + "]`")
                .setThumbnail(trackThumbnail);

        if (hours > 0) {
            builder.setDescription("`" + getTrackTime(track) + " / [" + songHours + ":" + songMinutes + ":" + songSeconds + "]`");
        }
        if (track.getInfo().uri.contains("/track")) {
            builder.setThumbnail(Util.randomThumbnail());
        }

        return builder;
    }
}
