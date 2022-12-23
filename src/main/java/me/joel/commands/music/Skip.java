package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class Skip extends ListenerAdapter {

    private static boolean sendNowPlaying = true;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("skip")) {

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            List<AudioTrack> playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();
            AudioTrack audioTrack;
            audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();

            if (audioTrack == null) {
                builder = new EmbedBuilder()
                    .setDescription("No song is playing or an error has occurred!")
                    .setColor(Color.green)
                    .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            builder = new EmbedBuilder()
                .setDescription("Song(s) skipped")
                .setFooter("Use /help for a list of music commands!")
                .setColor(Color.green);

            if (event.getOption("target") != null) {
                int songSkip = (event.getOption("target").getAsInt()) - 1;

                if (songSkip >= playlist.size() || songSkip < 0) {
                    EmbedBuilder skipOutOfBounds = new EmbedBuilder()
                            .setColor(Color.green)
                            .setDescription("That isn't a valid song number!")
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(skipOutOfBounds.build()).setEphemeral(true).queue();
                    return;
                }

                AudioTrack songToSkip = playlist.get(songSkip);
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.remove(songToSkip);

                event.replyEmbeds(builder.build()).queue();
                return;
            }

            else if (event.getOption("num") != null) {
                int songs = event.getOption("num").getAsInt();

                if (songs > playlist.size() || songs < 1) {
                    EmbedBuilder builder1 = new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("That isn't a valid number!")
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                    return;
                }

                AudioEventAdapter.setLoop(false);

                // disable now playing until songs are skipped
                sendNowPlaying = false;

                // skip -1
                for (int i = 0; i < songs-1; i++) {
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
                }

                // re enable now playing
                sendNowPlaying = true;

                // final skip
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();


                event.replyEmbeds(builder.build()).queue();
                return;
            }

            AudioEventAdapter.setLoop(false);

            if (AudioEventAdapter.isShuffling()) {
                int num;
                if (playlist.size() == 0) {
                    num = 0;
                }
                else {
                    num = me.joel.Util.randomWithRange(0, playlist.size());
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
                    num = me.joel.Util.randomWithRange(0, playlist.size());
                    randomTrack = playlist.get(num);
                }

                AudioTrack cloneTrack = playlist.get(num).makeClone();
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.startTrack(cloneTrack, false);
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.remove(randomTrack);
            }
            else {
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
            }

            event.replyEmbeds(builder.build()).queue();
        }
    }

    /**
     * onTrackStart Now Playing
     * @return Toggles on/off
     */
    public static boolean sendNowPlaying() {
        return sendNowPlaying;
    }
}
