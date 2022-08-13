package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.PlayerManager;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class Queue extends ListenerAdapter {

    // Queue vars
    int min = 0;
    int max = 5;
    int pageNumber = 1;

    private static List<AudioTrack> playlist;
    private static int queueSize;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("queue")) {

            // Resets queue
            min = 0;
            max = 5;
            pageNumber = 1;

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

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
                            .setColor(Color.red);

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                EmbedBuilder builder = Playing.nowPlaying(currentTrack);
                builder.setFooter("The queue is empty!");

                event.replyEmbeds(builder.build()).queue();
                return;
            }

            EmbedBuilder builder = queuePage(0, 5, 1, currentTrack, event.getGuild());

            // disable next page if next page is blank
            if (queueSize <= 5) {
                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.primary("previous", "Previous Page").asDisabled(),
                                Button.primary("next", "Next Page").asDisabled(),
                                Button.success("first", "Page 1").asDisabled())
                        .queue();
            } else {
                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.primary("previous", "Previous Page").asDisabled(),
                                Button.primary("next", "Next Page").asEnabled(),
                                Button.success("first", "Page 1").asDisabled())
                        .queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (!event.isFromGuild()) return;

        final AudioManager audioManager = event.getGuild().getAudioManager();
        var invoke = event.getComponentId();

        AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();

        switch (invoke) {

            case ("first") -> {
                min = 0;
                max = 5;
                pageNumber = 1;

                EmbedBuilder builder = queuePage(min, max, pageNumber, audioTrack, event.getGuild());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.primary("previous", "Previous Page").asDisabled(),
                                Button.primary("next", "Next Page").asEnabled(),
                                Button.success("first", "Page 1").asDisabled())
                        .queue();
            }
            case ("previous") -> {
                if (max > queueSize) max = queueSize;

                // if first page
                if (pageNumber == 1) {
                    EmbedBuilder builder = queuePage(min, max, pageNumber, audioTrack, event.getGuild());

                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("previous", "Previous Page").asDisabled(),
                                    Button.primary("next", "Next Page").asEnabled(),
                                    Button.success("first", "Page 1").asDisabled())
                            .queue();
                    return;
                }
                else {
                    min -= 5;
                    max -= 5;
                    pageNumber--;
                }

                EmbedBuilder builder = queuePage(min, max, pageNumber, audioTrack, event.getGuild());

                if (pageNumber == 1) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("previous", "Previous Page").asDisabled(),
                                    Button.primary("next", "Next Page").asEnabled(),
                                    Button.success("first", "Page 1").asDisabled())
                            .queue();
                    return;
                }
                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.primary("previous", "Previous Page").asEnabled(),
                                Button.primary("next", "Next Page").asEnabled(),
                                Button.success("first", "Page 1").asEnabled())
                        .queue();
            }
            case ("next") -> {
                if (max > queueSize) max = queueSize;

                min += 5;
                max += 5;
                pageNumber++;

                EmbedBuilder builder = queuePage(min, max, pageNumber, audioTrack, event.getGuild());

                // if next page is empty
                if (max >= queueSize) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("previous", "Previous Page").asEnabled(),
                                    Button.primary("next", "Next Page").asDisabled(),
                                    Button.success("first", "Page 1").asEnabled())
                            .queue();
                    return;
                }

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.primary("previous", "Previous Page").asEnabled(),
                                Button.primary("next", "Next Page").asEnabled(),
                                Button.success("first", "Page 1").asEnabled())
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

        // occurs when clearing queue then interacting with queue menu
        if (current == null) {
            return new EmbedBuilder()
                    .setDescription("The queue is empty or an error has occurred!")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(Color.red);
        }

        // Base embed
        EmbedBuilder queuePage = new EmbedBuilder()
                .setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl())
                .setTitle(current.getInfo().title, current.getInfo().uri)
                .setDescription("`" + Playing.getTrackCurrentTime(current) + " / " + Playing.getTrackTotalTime(current) + "`")
                .addField("Upcoming Tracks", "", false)
                .setColor(Util.randColor())
                .setThumbnail(Util.randomThumbnail())
                .setFooter("Use /help for a list of music commands!");

        // Add tracks in range
        for (int i = range1; i < queueSize && i < range2; ++i) {
            String songTile = playlist.get(i).getInfo().title;
            String songURI = playlist.get(i).getInfo().uri;

            queuePage
                    .addField("", "` " + i+1 + "` [" + songTile + "](" + songURI + ")\n", false);
        }
        queuePage.setFooter("Queue Size: " + queueSize + "\nPage " + pageNum);

        return queuePage;
    }
}
