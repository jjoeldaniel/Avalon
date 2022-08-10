package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.PlayerManager;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Queue extends ListenerAdapter {

    int min = 0;
    int max = 5;
    int pageNumber = 1;

    private static List<AudioTrack> playlist;
    private static int queueSize;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("queue")) {

            // Resets queue
            min = 0;
            max = 5;
            pageNumber = 1;

            // Avalon
            Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
            assert bot != null;

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();
            AudioTrack currentTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
            queueSize = playlist.size();

            // Empty queue
            if (queueSize == 0) {
                // Sends empty message if there is nothing playing, sends now playing otherwise
                if (currentTrack == null) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("The queue is empty or an error has occurred!")
                            .setFooter("Use /help for a list of music commands!")
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }
                // Time from ms to m:s
                long trackLength = currentTrack.getInfo().length;
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
                String trackThumbnail = PlayerManager.getThumbnail(currentTrack.getInfo().uri);

                // Embed
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setAuthor("Now Playing")
                        .setTitle(currentTrack.getInfo().title, currentTrack.getInfo().uri)
                        .setDescription("`[0:00] / [" + songMinutes + ":" + songSeconds + "]`")
                        .setThumbnail(trackThumbnail)
                        .setFooter("The queue is empty!");

                if (hours > 0) {
                    builder.setDescription("`[0:00] / [" + songHours + ":" + songMinutes + ":" + songSeconds + "]`");
                }
                if (currentTrack.getInfo().uri.contains("/track")) {
                    builder.setThumbnail(Util.randomThumbnail());
                }

                event.replyEmbeds(builder.build()).queue();
                return;
            }

            EmbedBuilder builder = queuePage(0, 5, 1, currentTrack, event.getGuild());

            // disable next page if next page is blank
            if (queueSize <= 5) {
                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.primary("previous", "Previous Page").asDisabled(),
                                Button.primary("next", "Next Page").asDisabled())
                        .queue();
            } else {
                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.primary("previous", "Previous Page").asDisabled(),
                                Button.primary("next", "Next Page"))
                        .queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (!event.isFromGuild()) return;

        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        var invoke = event.getComponentId();

        switch (invoke) {

            case ("previous") -> {
                if (max > queueSize) max = queueSize;

                // if first page
                if (pageNumber == 1) {
                    AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                    EmbedBuilder builder = queuePage(min, max, pageNumber, audioTrack, event.getGuild());

                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("previous", "Previous Page").asDisabled(),
                                    Button.primary("next", "Next Page").asEnabled())
                            .queue();
                    return;
                }
                else {
                    min -= 5;
                    max -= 5;
                    pageNumber--;
                }

                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(min, max, pageNumber, audioTrack, event.getGuild());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.primary("previous", "Previous Page").asEnabled(),
                                Button.primary("next", "Next Page").asEnabled())
                        .queue();
            }
            case ("next") -> {
                if (max > queueSize) max = queueSize;

                min += 5;
                max += 5;
                pageNumber++;

                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(min, max, pageNumber, audioTrack, event.getGuild());

                // if next page is empty
                if (max >= queueSize) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("previous", "Previous Page").asEnabled(),
                                    Button.primary("next", "Next Page").asDisabled())
                            .queue();
                    return;
                }

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.primary("previous", "Previous Page").asEnabled(),
                                Button.primary("next", "Next Page").asEnabled())
                        .queue();
            }
        }
    }

    /**
     * Queue Page EmbedBuilder
     * @param range1 Beginning queue index
     * @param range2 Ending queue index
     * @param pageNum Queue page number
     * @param current Current AudioTrack
     * @param guild Event Guild
     * @return Queue Page
     */
    public static EmbedBuilder queuePage(int range1, int range2, int pageNum, AudioTrack current, Guild guild) {

        // Base embed
        EmbedBuilder queuePage = new EmbedBuilder()
                .setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl())
                .setTitle("Queue [" + queueSize + "]")
                .addField("Now playing", "[" + current.getInfo().title + "](" + current.getInfo().uri + ")\n", false)
                .setColor(Util.randColor())
                .setThumbnail(Util.randomThumbnail())
                .setFooter("Use /help for a list of music commands!");

        // Add tracks in range
        for (int i = range1; i < queueSize && i < range2; ++i) {
            String songTile = playlist.get(i).getInfo().title;
            String songURI = playlist.get(i).getInfo().uri;

            queuePage
                    .addField("[" + (i + 1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                    .setFooter("Page " + pageNum);
        }

        return queuePage;
    }
}
