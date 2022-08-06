package me.joel;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MusicCommands extends ListenerAdapter {

    private static Member member;
    List<AudioTrack> playlist;
    int queueSize;

    private static SlashCommandInteractionEvent newEvent;

    public static Member getMember() {
        return member;
    }

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // newEvent
        newEvent = event;

        // check if event is not from guild
        if (!event.isFromGuild()) return;

        // JDA AudioManager
        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        // gets event member
        member = event.getMember();

        // paw bot
        Member bot = event.getGuild().getMemberById("971239438892019743");
        assert bot != null;

        // VCRequirement embed
        final EmbedBuilder VCRequirement = new EmbedBuilder()
                .setColor(Util.randColor())
                .setDescription("You need to be in a voice channel to use this command!")
                .setFooter("Use /help for a list of music commands!");

        // sameVCRequirement embed
        final EmbedBuilder sameVCRequirement = new EmbedBuilder()
                .setColor(Util.randColor())
                .setDescription("You need to be in the same voice channel to use this command!")
                .setFooter("Use /help for a list of music commands!");

        try {
            var invoke = event.getName();

            switch (invoke) {
                case ("loop") -> {
                    // Checks requester voice state
                    if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                        event.getHook().sendMessageEmbeds(VCRequirement.build()).setEphemeral(true).queue();
                        return;
                    }

                    // Compare JDA and member voice state
                    if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                        long memberVC = Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong();
                        long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                        if (!(botVC == memberVC)) {
                            event.getHook().sendMessageEmbeds(sameVCRequirement.build()).setEphemeral(true).queue();
                            return;
                        }
                    }

                    if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack() == null) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("There is no song currently playing!");

                        event.replyEmbeds(builder.build()).queue();
                        return;
                    }

                    if (!AudioEventAdapter.isLooping()) {
                        AudioEventAdapter.setLoop(true);
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("Song is now looping!");

                        event.replyEmbeds(builder.build()).queue();
                    } else {
                        AudioEventAdapter.setLoop(false);
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("Song is no longer looping!");

                        event.replyEmbeds(builder.build()).queue();
                    }
                }
                case ("volume") -> {
                    // Checks requester voice state
                    if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                        event.replyEmbeds(VCRequirement.build()).setEphemeral(true).queue();
                        return;
                    }

                    // Compare JDA and member voice state
                    if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                        long memberVC = Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong();
                        long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                        if (!(botVC == memberVC)) {
                            event.replyEmbeds(sameVCRequirement.build()).setEphemeral(true).queue();
                            return;
                        }
                    }

                    int num = Objects.requireNonNull(event.getOption("num")).getAsInt();

                    if (num <= 0 || num > 100) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                                .setTitle("Error! You can't set the volume to 0 or above 100.")
                                .setFooter("Use /help for a list of music commands!");

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                    int prevVolume = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getVolume();
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.setVolume(num / 2);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                            .setTitle("Volume is now set to " + num + "%. (Prev: " + prevVolume * 2 + "%)")
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(builder.build()).queue();
                }
                case ("playing") -> {
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

                        event.replyEmbeds(builder.build()).queue();

                    } catch (Exception exception) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("No song is playing!")
                                .setFooter("Use /help for a list of music commands!");
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    }
                }
                case ("clear") -> {
                    // Checks requester voice state
                    if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                        event.replyEmbeds(VCRequirement.build()).setEphemeral(true).queue();
                        return;
                    }

                    // Compare JDA and member voice state
                    if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                        long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                        long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                        if (!(botVC == memberVC)) {
                            event.replyEmbeds(sameVCRequirement.build()).setEphemeral(true).queue();
                            return;
                        }
                    }

                    if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size() == 0) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setDescription("The queue is empty or an error has occurred!")
                                .setFooter("Use /help for a list of music commands!")
                                .setColor(Util.randColor());

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }

                    AudioEventAdapter.setLoop(false);
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.clear();
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.destroy();

                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Queue cleared")
                            .setFooter("Use /help for a list of music commands!")
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).queue();
                }
                case ("skip") -> {
                    // Checks requester voice state
                    if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                        AudioEventAdapter.setLoop(false);
                        event.replyEmbeds(VCRequirement.build()).setEphemeral(true).queue();
                        return;
                    }

                    // Compare JDA and member voice state
                    if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                        long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                        long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                        if (!(botVC == memberVC)) {
                            event.replyEmbeds(sameVCRequirement.build()).setEphemeral(true).queue();
                            return;
                        }
                    }

                    AudioTrack audioTrack;
                    audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();

                    if (audioTrack == null) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setDescription("No song is playing or an error has occurred!")
                                .setColor(Util.randColor())
                                .setFooter("Use /help for a list of music commands!");

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Song(s) skipped")
                            .setFooter("Use /help for a list of music commands!")
                            .setColor(Util.randColor());

                    if (event.getOption("song_num") != null) {
                        int songSkip = (Objects.requireNonNull(event.getOption("song_num")).getAsInt()) - 1;

                        if (songSkip >= 2) {
                            if (songSkip >= playlist.size()) {
                                EmbedBuilder skipOutOfBounds = new EmbedBuilder()
                                        .setColor(Util.randColor())
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
                    }

                    else if (event.getOption("songs_to_skip") != null) {
                        int songs = Objects.requireNonNull(event.getOption("songs_to_skip")).getAsInt();

                        if (songs > playlist.size()) {
                            EmbedBuilder builder1 = new EmbedBuilder()
                                    .setColor(Util.randColor())
                                    .setDescription("That isn't a valid number!")
                                    .setFooter("Use /help for a list of music commands!");

                            event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                            return;
                        }
                        for (int i = 0; i < songs; i++) {
                            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
                        }

                        event.replyEmbeds(builder.build()).queue();
                        return;
                    }

                    event.replyEmbeds(builder.build()).queue();
                    AudioEventAdapter.setLoop(false);
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
                }
                case ("queue") -> {
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

        } catch (Exception e) {
            event.getHook().sendMessageEmbeds(Util.genericError().build()).queue();
        }
    }

    // Button Interactions
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
    public EmbedBuilder queuePage(int range1, int range2, int pageNum, AudioTrack current, Guild guild) {

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

    /**
     * @return Event channel
     */
    public static MessageChannelUnion returnChannel() {
        return newEvent.getChannel();
    }

    /**
     * @return True if url is valid
     */
    public static boolean isURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
