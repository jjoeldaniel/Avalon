package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.joel.Util;
import me.joel.commands.music.lavaplayer.AudioEventAdapter;
import me.joel.commands.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

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

            // uses custom emojis from avalon support server
            Guild guild = event.getJDA().getGuildById("971225319153479790");
            if (guild.getEmojiById("1008350970872336455") != null && guild.getEmojiById("1008350949716267018") != null && guild.getEmojiById("1008350808041066588") != null && guild.getEmojiById("1008350819655102546") != null) {

                Emoji forward = guild.getEmojiById("1008350970872336455");
                Emoji backward = guild.getEmojiById("1008350949716267018");
                Emoji play = guild.getEmojiById("1008350808041066588");
                Emoji pause = guild.getEmojiById("1008350819655102546");

                event.replyEmbeds(builder.build()).setEphemeral(true)
                        .addActionRow(
                                Button.primary("rewind", backward),
                                Button.primary("pause", pause),
                                Button.primary("resume", play),
                                Button.primary("forward", forward)
                        )
                        .queue();
                return;
            }

            event.replyEmbeds(builder.build()).setEphemeral(true)
                    .addActionRow(
                            Button.primary("rewind", Emoji.fromFormatted("U+23EA")),
                            Button.primary("pause", Emoji.fromFormatted("U+23F8")),
                            Button.primary("resume", Emoji.fromFormatted("U+25B6")),
                            Button.primary("forward", Emoji.fromFormatted("U+23E9"))
                    )
                    .queue();

        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        var invoke = event.getComponentId();

        if (invoke.equals("pause") || invoke.equals("resume") || invoke.equals("forward") | invoke.equals("rewind")) {
            if (!inVC(event.getMember())) {
                EmbedBuilder builder = nowPlaying(PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack());
                builder.setFooter("");

                event.editMessageEmbeds(builder.build()).queue();
                return;
            }
            else if (!inBotVC(event.getMember(), event.getGuild().getSelfMember())) {
                EmbedBuilder builder = nowPlaying(PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack());
                builder.setFooter("");

                event.editMessageEmbeds(builder.build()).queue();
                return;
            }
        }

        switch (invoke) {
            case "pause" -> {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.setPaused(true);

                EmbedBuilder builder = nowPlaying(PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack());
                builder.setFooter("");

                event.editMessageEmbeds(builder.build()).queue();
            }
            case "resume" -> {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.setPaused(false);

                EmbedBuilder builder = nowPlaying(PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack());
                builder.setFooter("");

                event.editMessageEmbeds(builder.build()).queue();
            }
            case "forward" -> {
                if (PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.queue.size() != 0) {
                    final AudioManager audioManager = event.getGuild().getAudioManager();

                    if (AudioEventAdapter.isShuffling()) {
                        List<AudioTrack> playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();

                        int num;
                        if (playlist.size() == 0) {
                            num = 0;
                        }
                        else {
                            num = Util.randomWithRange(0, playlist.size());
                        }

                        if (playlist.isEmpty()) {
                            EmbedBuilder builder1 = new EmbedBuilder()
                                    .setColor(Color.red)
                                    .setDescription("That isn't a valid number!")
                                    .setFooter("Use /help for a list of music commands!");

                            event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                            return;
                        }

                        AudioTrack randomTrack = playlist.get(num);
                        while (randomTrack == PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack()) {
                            num = Util.randomWithRange(0, playlist.size());
                            randomTrack = playlist.get(num);
                        }

                        AudioTrack cloneTrack = playlist.get(num).makeClone();
                        PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.startTrack(cloneTrack, false);
                        PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.remove(randomTrack);
                    }
                    else {
                        PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
                    }
                }

                EmbedBuilder builder = nowPlaying(PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack());
                builder.setFooter("");

                event.editMessageEmbeds(builder.build()).queue();
            }
            case "rewind" -> {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack().setPosition(0);

                EmbedBuilder builder = nowPlaying(PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack());
                builder.setFooter("");

                event.editMessageEmbeds(builder.build()).queue();
            }
        }
    }

    private static boolean inVC(Member member) {
        return member.getVoiceState().inAudioChannel();
    }

    private static boolean inBotVC(Member member, Member bot) {
        return (member.getVoiceState().getChannel() == bot.getVoiceState().getChannel());
    }

    /**
     * Gets track current time
     * @param track Track
     * @return Time in [h:m:s] format
     */
    public static String getTrackCurrentTime(AudioTrack track) {

        // seconds are measured in thousands
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

    /**
     * Gets track total time
     * @param track Track
     * @return Time in {h:m:s] format
     */
    public static String getTrackTotalTime(AudioTrack track) {

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

        if (hours <= 0) return "[" + songMinutes + ":" + songSeconds + "]";

        return "[" + songHours + ":" + songMinutes + ":" + songSeconds + "]";
    }

    /**
     * Displays currently playing song in embed
     * @param track Current track
     * @return Embed
     */
    public static EmbedBuilder nowPlaying(AudioTrack track) {

        boolean isSpotify = track.getInfo().uri.contains("spotify.com");

        // Thumbnail
        String trackThumbnail = PlayerManager.getThumbnail(track.getInfo().uri);

        /* Checks for long overflow

        Explanation: If adding 1 to the track.length results in an overflow, then the original number was likely
        not valid to begin with.

        In this case, this is only the result of attempting to access a YouTube stream videos length
         */
        try {
            Math.addExact(track.getInfo().length, 1);
        }
        catch (ArithmeticException e) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setAuthor("Now Playing")
                    .setTitle(track.getInfo().title, track.getInfo().uri)
                    .setThumbnail(trackThumbnail);

            if (isSpotify) {
                builder.setTitle(track.getInfo().title + " - " + track.getInfo().author, track.getInfo().uri);
            }

            return builder;
        }

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setAuthor("Now Playing")
                .setTitle(track.getInfo().title, track.getInfo().uri)
                .setDescription("`" + getTrackCurrentTime(track) + " / " + getTrackTotalTime(track) + "`")
                .setThumbnail(trackThumbnail);

        if (isSpotify) {
            builder.setTitle(track.getInfo().title + " - " + track.getInfo().author, track.getInfo().uri);
        }

        return builder;

    }
}
