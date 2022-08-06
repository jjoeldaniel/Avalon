package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.PlayerManager;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Playing extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("playing")) {

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            try {
                AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();

                // Time from ms to m:s
                long trackLength = track.getInfo().length;
                long minutes = (trackLength / 1000) / 60;
                long seconds = ((trackLength / 1000) % 60);
                String songSeconds = String.valueOf(seconds);
                if (seconds < 10) songSeconds = "0" + seconds;

                // Thumbnail
                String trackThumbnail = PlayerManager.getThumbnail(track.getInfo().uri);

                // Embed
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setAuthor("Now Playing")
                        .setTitle(track.getInfo().title, track.getInfo().uri)
                        .setDescription("`[0:00 / [" + minutes + ":" + songSeconds + "]`")
                        .setThumbnail(trackThumbnail)
                        .setFooter("Use /help for a list of music commands!");

                if (track.getInfo().uri.contains("/track")) {
                    builder.setThumbnail(Util.randomThumbnail());
                }

                event.replyEmbeds(builder.build()).queue();

            } catch (Exception exception) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setDescription("No song is playing!")
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
        }
    }
}
