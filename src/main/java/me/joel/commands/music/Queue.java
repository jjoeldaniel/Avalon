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

    private static List<AudioTrack> playlist;
    private static int queueSize;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("queue")) {

            // Avalon
            Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
            assert bot != null;

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();
            AudioTrack currentTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
            queueSize = playlist.size();

            if (queueSize == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("The queue is empty or an error has occurred!")
                        .setFooter("Use /help for a list of music commands!")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = queuePage(0, 5, 1, currentTrack, event.getGuild());

            // disable next page if next page is blank
            if (queueSize <= 5) {
                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.primary("page1", "Previous Page").asDisabled(),
                                Button.primary("page2", "Next Page").asDisabled())
                        .queue();
            } else {
                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.primary("page1", "Previous Page").asDisabled(),
                                Button.primary("page2", "Next Page"))
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

            case ("page1") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(0, 5, 1, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 5) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page1", "Previous Page").asDisabled(),
                                    Button.primary("page2", "Next Page").asDisabled())
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page1", "Previous Page").asDisabled(),
                                    Button.primary("page2", "Next Page"))
                            .queue();
                }
            }
            case ("page2") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(5, 10, 2, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 10) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page1", "Previous Page"),
                                    Button.primary("page3", "Next Page").asDisabled())
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page1", "Previous Page"),
                                    Button.primary("page3", "Next Page"))
                            .queue();
                }
            }
            case ("page3") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(10, 15, 3, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 15) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page2", "Previous Page"),
                                    Button.primary("page4", "Next Page").asDisabled(),
                                    Button.success("page1", "First Page"))
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page2", "Previous Page"),
                                    Button.primary("page4", "Next Page"),
                                    Button.success("page1", "First Page"))
                            .queue();
                }
            }
            case ("page4") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(15, 20, 4, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 20) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page3", "Previous Page"),
                                    Button.primary("page5", "Next Page").asDisabled(),
                                    Button.success("page1", "First Page"))
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page3", "Previous Page"),
                                    Button.primary("page5", "Next Page"),
                                    Button.success("page1", "First Page"))
                            .queue();
                }
            }
            case ("page5") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(20, 25, 5, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 25) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page4", "Previous Page"),
                                    Button.primary("page6", "Next Page").asDisabled(),
                                    Button.success("page1", "First Page"))
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page4", "Previous Page"),
                                    Button.primary("page6", "Next Page"),
                                    Button.success("page1", "First Page"))
                            .queue();
                }
            }
            case ("page6") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(25, 30, 6, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 30) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page5", "Previous Page"),
                                    Button.primary("page7", "Next Page").asDisabled(),
                                    Button.success("page1", "First Page"))
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page5", "Previous Page"),
                                    Button.primary("page7", "Next Page"),
                                    Button.success("page1", "First Page"))
                            .queue();
                }
            }
            case ("page7") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(30, 35, 7, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 35) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page6", "Previous Page"),
                                    Button.primary("page8", "Next Page").asDisabled(),
                                    Button.success("page1", "First Page"))
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page6", "Previous Page"),
                                    Button.primary("page8", "Next Page"),
                                    Button.success("page1", "First Page"))
                            .queue();
                }
            }
            case ("page8") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(35, 40, 8, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 40) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page7", "Previous Page"),
                                    Button.primary("page9", "Next Page").asDisabled(),
                                    Button.success("page1", "First Page"))
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page7", "Previous Page"),
                                    Button.primary("page9", "Next Page"),
                                    Button.success("page1", "First Page"))
                            .queue();
                }
            }
            case ("page9") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(40, 45, 9, audioTrack, event.getGuild());

                // disable next page if next page is blank
                if (queueSize <= 45) {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page8", "Previous Page"),
                                    Button.primary("page10", "Next Page").asDisabled(),
                                    Button.success("page1", "First Page"))
                            .queue();
                } else {
                    event.editMessageEmbeds(builder.build())
                            .setActionRow(
                                    Button.primary("page8", "Previous Page"),
                                    Button.primary("page10", "Next Page"),
                                    Button.success("page1", "First Page"))
                            .queue();
                }
            }
            case ("page10") -> {
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();
                EmbedBuilder builder = queuePage(45, 50, 10, audioTrack, event.getGuild());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.primary("page9", "Previous Page"),
                                Button.success("page1", "First Page"))
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
