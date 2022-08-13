package me.joel.lavaplayer;

import com.github.topislavalinkplugins.topissourcemanagers.applemusic.AppleMusicSourceManager;
import com.github.topislavalinkplugins.topissourcemanagers.spotify.SpotifyConfig;
import com.github.topislavalinkplugins.topissourcemanagers.spotify.SpotifySourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.Util;
import me.joel.commands.music.Play;
import me.joel.commands.music.Playing;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        // Spotify Source Manager
        SpotifyConfig spotifyConfig = new SpotifyConfig();
        spotifyConfig.setClientId("3451401ce3b148039cbba35a2c25cd5f");
        spotifyConfig.setClientSecret("08becf6c9969424c833f0d8daaf00135");
        spotifyConfig.setCountryCode("US");
        this.audioPlayerManager.registerSourceManager(new SpotifySourceManager(null, spotifyConfig, this.audioPlayerManager));

        // Apple Music Source Manager
        this.audioPlayerManager.registerSourceManager(new AppleMusicSourceManager(null, "us", this.audioPlayerManager));

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) ->
        {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(MessageChannelUnion channel, String trackURL) {

        Guild guild = null;

        if (channel.getType() == ChannelType.TEXT) guild = channel.asTextChannel().getGuild();
        else if (channel.getType() == ChannelType.VOICE) guild = channel.asVoiceChannel().getGuild();

        assert guild != null;
        final GuildMusicManager musicManager = this.getMusicManager(guild);

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {

            // YT Tracks
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                EmbedBuilder builder = addedTrackToQueue(audioTrack, musicManager.scheduler.queue.size());

                if (musicManager.scheduler.queue.size() == 0) {
                    builder.setAuthor(("Added to queue"));

                    builder.setThumbnail(getThumbnail(audioTrack.getInfo().uri));
                }

                channel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

                // tracks
                if (!Play.isURL(trackURL)) {
                    final List<AudioTrack> tracks = audioPlaylist.getTracks();
                    if (!tracks.isEmpty()) {
                        musicManager.scheduler.queue(tracks.get(0));
                    }

                    AudioTrack audioTrack = tracks.get(0);
                    EmbedBuilder builder = addedTrackToQueue(audioTrack, musicManager.scheduler.queue.size());
                    if (musicManager.scheduler.queue.size() == 0) {
                        builder.setAuthor(("Added to queue"));
                    }
                    channel.sendMessageEmbeds(builder.build()).queue();
                    return;
                }

                // playlists
                for (AudioTrack track : audioPlaylist.getTracks()) {
                    musicManager.scheduler.queue(track);
                }

                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {

                    // Thumbnail
                    String trackThumbnail = getThumbnail(tracks.get(0).getInfo().uri);

                    // Playlist size
                    int playlistSize = audioPlaylist.getTracks().size();
                    String firstURI = audioPlaylist.getTracks().get(0).getInfo().uri;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setAuthor("Playlist queued")
                            .setTitle(audioPlaylist.getName(), firstURI)
                            .setDescription("`[" + playlistSize + "] songs`")
                            .setThumbnail(trackThumbnail)
                            .addField("Requested by:", me.joel.commands.music.Util.getMember().getAsMention(), false);

                    channel.sendMessageEmbeds(builder.build()).queue();
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("No song(s) found!");

                channel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Loading track failed!");

                channel.sendMessageEmbeds(builder.build()).queue();
            }
        });

    }

    public static PlayerManager getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new PlayerManager();
        return INSTANCE;
    }

    public static EmbedBuilder addedTrackToQueue(AudioTrack audioTrack, int queueSize) {

        EmbedBuilder builder1 = Playing.nowPlaying(audioTrack);
        builder1.setAuthor("Added to queue (#" + queueSize + ")");
        builder1.addField("Requested by:", me.joel.commands.music.Util.getMember().getAsMention(), false);

        return builder1;
    }

    // Gets YouTube thumbnail
    public static String getThumbnail(String link) {
        if (link.contains("spotify.com")) {
            return Spotify.searchSpotify(link);
        }

        int linkLength = link.length() + 1;
        String linkPrefix = "https://img.youtube.com/vi/";
        String linkSuffix = "/0.jpg";
        StringBuilder stringBuilder = new StringBuilder()
                .append(link)
                .delete(0, linkLength - 12);
        String videoID = stringBuilder.toString();

        return linkPrefix + videoID + linkSuffix;
    }
}
