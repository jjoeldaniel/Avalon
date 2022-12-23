package periodicallyprogramming.avalon.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.GLA;
import genius.SongSearch;
import periodicallyprogramming.avalon.Util;
import periodicallyprogramming.avalon.lavaplayer.PlayerManager;
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
                return;
            }
            else if (!track.getInfo().uri.startsWith("https://open.spotify.com/") && (!(track.getInfo().uri.contains("https://music.apple.com/")))) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Only Spotify/Apple Music tracks are supported, sorry!");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            event.deferReply().queue();

            GLA gla = new GLA();
            EmbedBuilder lyricsEmbed = new EmbedBuilder();
            String lyrics;

            try {
                SongSearch.Hit hit = gla.search(track.getInfo().title + " " + track.getInfo().author).getHits().get(0);
                lyrics = hit.fetchLyrics();
                lyricsEmbed.setThumbnail(hit.getThumbnailUrl());
                lyricsEmbed.setTitle(hit.getTitleWithFeatured() + " - " + hit.getArtist().getName());
            } catch (IOException e) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("No lyrics were found!");
                event.getHook().sendMessageEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            lyricsEmbed.setColor(Util.randColor());
            lyricsEmbed.setDescription(lyrics);

            event.getHook().sendMessageEmbeds(lyricsEmbed.build()).queue();
        }
    }
}
