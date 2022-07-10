package me.joel;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
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

public class MusicCommands extends ListenerAdapter {

    static Member member;
    EmbedBuilder queue = new EmbedBuilder();
    List<AudioTrack> playlist;
    int queueSize;

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        member = event.getMember();

        // Play
        if (event.getName().equals("play")) {
            event.deferReply().queue();
            // Loops 'i' times due to occasional issues which result in songs not properly being queued
            // Unsure of how to fix core issue, this is a solid fix for now, however
            for (int i = 0; i < 5; ++i) {
                try {
                    // Checks requester voice state
                    if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in a voice channel to use `/play`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.getHook().sendMessageEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }

                    final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                    String link = Objects.requireNonNull(event.getOption("song")).getAsString();
                    System.out.println("Link: " + link);

                    // TODO: Add Spotify support
                    // Spotify links
                    if (link.contains("spotify.com")) {
                        System.out.println("Input type: SPOTIFY");
                    }
                    // Invalid links
                    else if (!isURL(link)) {
                        link = ("ytsearch:" + link + " audio");
                        System.out.println("Input type: NON_URI");
                        // Joins VC
                        audioManager.openAudioConnection(memberChannel);
                        Member bot = event.getMember().getGuild().getMemberById("971239438892019743");
                        assert bot != null;
                        event.getGuild().deafen(bot, true).queue();

                        // Plays song
                        PlayerManager.getINSTANCE().loadAndPlayNoURI(event.getTextChannel(), link);
                        PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setVolume(50);
                    }
                    // Valid links (Basically just YouTube)
                    else {
                        System.out.println("Input type: YOUTUBE");
                        // Joins VC
                        audioManager.openAudioConnection(memberChannel);
                        Member bot = event.getMember().getGuild().getMemberById("971239438892019743");
                        assert bot != null;
                        event.getGuild().deafen(bot, true).queue();

                        // Plays song
                        PlayerManager.getINSTANCE().loadAndPlay(event.getTextChannel(), link);
                        PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setVolume(50);

                    }
                    EmbedBuilder error = new EmbedBuilder()
                            .setDescription("If you still see this message, an error has occurred!");
                    event.getHook().sendMessageEmbeds(error.build()).setEphemeral(true).queue();
                    event.getHook().deleteOriginal().queue();

                    break;
                }

                catch (Exception exception) { // Add real exception handling later
                    System.out.println("Error occurred during playback");
                }
            }
        }

        // Pause
        if (event.getName().equals("pause")) {
            if (!PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.isPaused() && PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack() != null) {
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(true);

                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Playback paused")
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("No song is playing or an error has occurred!")
                    .setColor(Util.randColor())
                    .setFooter("Use /help for a list of music commands!");

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // Playing
        if (event.getName().equals("playing")) {

            try {
                AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack();

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

            }
            catch (Exception exception) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("No song is playing!")
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
        }

        // Resume
        if (event.getName().equals("resume")) {

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.isPaused() && PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack() != null) {
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(false);

                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Playback resumed")
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("No song is playing or an error has occurred!")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(Util.randColor());

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // Clear
        if (event.getName().equals("clear")) {

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size() == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("The queue is empty or an error has occurred!")
                        .setFooter("Use /help for a list of music commands!")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).queue();
                return;
            }

            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.clear();
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.destroy();

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("Queue cleared")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(Util.randColor());

            event.replyEmbeds(builder.build()).queue();
        }

        // Skip
        if (event.getName().equals("skip")) {
            int skipNum = 0;
            try {
                skipNum = Objects.requireNonNull(event.getOption("skip_number")).getAsInt();
            }
            catch (Exception ignore) {}

            if (skipNum > 0) {
                for (int i = 0; i < skipNum; ++i) {
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
                }
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Songs skipped")
                        .setFooter("Use /help for a list of music commands!")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).queue();
                return;
            }
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("Song skipped")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(Util.randColor());

            event.replyEmbeds(builder.build()).queue();
        }

        // Queue TODO: Overhaul queue
        if (event.getName().equals("queue")) {

            String currentSong;
            queueSize = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size();

            if (queueSize == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("The queue is empty or an error has occurred!")
                        .setFooter("Use /help for a list of music commands!")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).queue();
                return;
            }

            event.deferReply().queue();

            try {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore) { return; }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();

            queue
                    .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                    .setTitle("Queue [" + queueSize + "]")
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false)
                    .setColor(Util.randColor())
                    .setThumbnail(Util.randomThumbnail())
                    .setFooter("Use /help for a list of music commands!");

            if (queueSize == 1) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.copyFrom(queue);
                builder
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false);
                event.getHook().sendMessageEmbeds(builder.build()).queue();
            }
            else if (queueSize == 2) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.copyFrom(queue);
                builder
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false);
                event.getHook().sendMessageEmbeds(builder.build()).queue();

            }
            else if (queueSize == 3) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.copyFrom(queue);
                builder
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false);
                event.getHook().sendMessageEmbeds(builder.build()).queue();

            }
            else if (queueSize == 4) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.copyFrom(queue);
                builder
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false);
                event.getHook().sendMessageEmbeds(builder.build()).queue();
            }
            else if (queueSize == 5) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.copyFrom(queue);
                builder
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false);
                event.getHook().sendMessageEmbeds(builder.build()).queue();
            }
            else if (queueSize > 5) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.copyFrom(queue);
                builder
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false)
                        .setFooter("Page 1");
                event.getHook().sendMessageEmbeds(builder.build())
                        .addActionRow(Button.primary("page2", "Next Page"))
                        .queue();
            }

        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (event.getComponentId().equals("page1")) {
            EmbedBuilder page1 = new EmbedBuilder();
            page1.copyFrom(queue);

            page1
                    .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                    .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                    .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                    .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                    .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false);

            event.editMessageEmbeds(page1.build())
                    .setActionRow(Button.primary("page2", "Next Page"))
                    .queue();
        }

        // FUNCTIONAL, DO NOT CHANGE AT ALL
        else if (event.getComponentId().equals("page2")) {
            EmbedBuilder page2 = new EmbedBuilder();
            page2.copyFrom(queue);

            if (queueSize == 6) {
                page2
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .setFooter("Page 2");

                event.editMessageEmbeds(page2.build())
                        .setActionRow(Button.primary("page1", "Previous Page"))
                        .queue();
            }
            else if (queueSize == 7) {
                page2
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                        .setFooter("Page 2");

                event.editMessageEmbeds(page2.build())
                        .setActionRow(Button.primary("page1", "Previous Page"))
                        .queue();
            }
            else if (queueSize == 8) {
                page2
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                        .addField("[8]", "[" + playlist.get(7).getInfo().title + "](" + playlist.get(7).getInfo().uri + ")\n", false)
                        .setFooter("Page 2");

                event.editMessageEmbeds(page2.build())
                        .setActionRow(Button.primary("page1", "Previous Page"))
                        .queue();
            }
            else if (queueSize == 9) {
                page2
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                        .addField("[8]", "[" + playlist.get(7).getInfo().title + "](" + playlist.get(7).getInfo().uri + ")\n", false)
                        .addField("[9]", "[" + playlist.get(8).getInfo().title + "](" + playlist.get(8).getInfo().uri + ")\n", false)
                        .setFooter("Page 2");

                event.editMessageEmbeds(page2.build())
                        .setActionRow(Button.primary("page1", "Previous Page"))
                        .queue();
            }
            else if (queueSize == 10) {
                page2
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                        .addField("[8]", "[" + playlist.get(7).getInfo().title + "](" + playlist.get(7).getInfo().uri + ")\n", false)
                        .addField("[9]", "[" + playlist.get(8).getInfo().title + "](" + playlist.get(8).getInfo().uri + ")\n", false)
                        .addField("[10]", "[" + playlist.get(9).getInfo().title + "](" + playlist.get(9).getInfo().uri + ")\n", false)
                        .setFooter("Page 2");

                event.editMessageEmbeds(page2.build())
                        .setActionRow(Button.primary("page1", "Previous Page"))
                        .queue();
            }
            else if (queueSize > 10) {
                page2
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                        .addField("[8]", "[" + playlist.get(7).getInfo().title + "](" + playlist.get(7).getInfo().uri + ")\n", false)
                        .addField("[9]", "[" + playlist.get(8).getInfo().title + "](" + playlist.get(8).getInfo().uri + ")\n", false)
                        .addField("[10]", "[" + playlist.get(9).getInfo().title + "](" + playlist.get(9).getInfo().uri + ")\n", false)
                        .setFooter("Page 2");

                event.editMessageEmbeds(page2.build())
                        .setActionRow(Button.primary("page1", "Previous Page"))
                        .queue();
            }

        }
    }

    // Validates links
    public boolean isURL(String url) {
        try {
            new URI(url);
            return true;
        }
        catch (URISyntaxException e) {
            return false;
        }
    }

}
