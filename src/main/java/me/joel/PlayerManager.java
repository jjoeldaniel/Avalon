package me.joel;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

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

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                System.out.println("Loading track...");
                musicManager.scheduler.queue(audioTrack);

                // Time from ms to m:s
                long trackLength = audioTrack.getInfo().length;
                long minutes = (trackLength / 1000) / 60;
                long seconds = ((trackLength / 1000) % 60);

                // Thumbnail
                String trackThumbnail = getThumbnail(audioTrack.getInfo().uri);

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setAuthor("Added to queue")
                        .setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
                        .setDescription("`[0:00 / [" + minutes + ":" + seconds + "]`")
                        .setThumbnail(trackThumbnail)
                        .addField("Requested by:", MusicCommands.member.getAsMention(), false)
                        .setFooter("Use /help for a list of music commands!");

                textChannel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                System.out.println("Loading playlist...");

                for (AudioTrack track : audioPlaylist.getTracks()) {
                    musicManager.scheduler.queue(track);
                }

                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));

                    // Thumbnail
                    String trackThumbnail = getThumbnail(tracks.get(0).getInfo().uri);

                    // Playlist size
                    int playlistSize = audioPlaylist.getTracks().size();

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setAuthor("Playlist queued")
                            .setTitle(audioPlaylist.getName())
                            .setDescription("`[" + playlistSize + "] songs`")
                            .setThumbnail(trackThumbnail)
                            .addField("Requested by:", MusicCommands.member.getAsMention(), false)
                            .setFooter("Use /help for a list of music commands!");

                    textChannel.sendMessageEmbeds(builder.build()).queue();
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setDescription("No song found!");

                textChannel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }
    public void loadAndPlayNoURI(TextChannel textChannel, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                System.out.println("Loading track...");
                musicManager.scheduler.queue(audioTrack);

                // Time from ms to m:s
                long trackLength = audioTrack.getInfo().length;
                long minutes = (trackLength / 1000) / 60;
                long seconds = ((trackLength / 1000) % 60);

                // Thumbnail
                String trackThumbnail = getThumbnail(audioTrack.getInfo().uri);

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setAuthor("Added to queue")
                        .setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
                        .setDescription("`[0:00 / [" + minutes + ":" + seconds + "]`")
                        .setThumbnail(trackThumbnail)
                        .addField("Requested by:", MusicCommands.member.getAsMention(), false)
                        .setFooter("Use /help for a list of music commands!");

                textChannel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                System.out.println("Loading track...");

                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));
                }

                AudioTrack audioTrack = tracks.get(0);

                // Time from ms to m:s
                long trackLength = audioTrack.getInfo().length;
                long minutes = (trackLength / 1000) / 60;
                long seconds = ((trackLength / 1000) % 60);

                // Thumbnail
                String trackThumbnail = getThumbnail(audioTrack.getInfo().uri);

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setAuthor("Added to queue")
                        .setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
                        .setDescription("`[0:00 / [" + minutes + ":" + seconds + "]`")
                        .setThumbnail(trackThumbnail)
                        .addField("Requested by:", MusicCommands.member.getAsMention(), false)
                        .setFooter("Use /help for a list of music commands!");

                textChannel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void noMatches() {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setDescription("No song found!");

                textChannel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public static PlayerManager getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new PlayerManager();
        return INSTANCE;
    }

    // Gets YouTube thumbnail
    public static String getThumbnail(String link) {

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
