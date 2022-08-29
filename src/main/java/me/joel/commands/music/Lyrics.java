package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.GLA;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class Lyrics extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("lyrics")) {

            AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack();

            if (track == null) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("No song is playing!");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
            else if (!track.getInfo().uri.contains("spotify.com")) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Only Spotify tracks are supported, sorry!");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }

            GLA gla = new GLA();

            try {
                String lyrics = gla.search(track.getInfo().title + " " + track.getInfo().author).getHits().get(0).fetchLyrics();
                System.out.print(lyrics);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
