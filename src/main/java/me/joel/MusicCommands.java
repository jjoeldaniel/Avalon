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
                skipNum = Objects.requireNonNull(event.getOption("number")).getAsInt();
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

            EmbedBuilder page1 = new EmbedBuilder();
            page1.copyFrom(queue);

            for (int i = 0; i < queueSize && i < 5; ++i) {
                page1
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 1");
            }
            event.getHook().sendMessageEmbeds(page1.build())
                    .addActionRow(Button.primary("page2", "Next Page"))
                    .queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (event.getComponentId().equals("page1")) {
            EmbedBuilder page1 = new EmbedBuilder();
            page1.copyFrom(queue);

            for (int i = 0; i < queueSize && i < 5; ++i) {
                page1
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 1");
            }
            event.editMessageEmbeds(page1.build())
                    .setActionRow(Button.primary("page2", "Next Page"))
                    .queue();
        }

        if (event.getComponentId().equals("page2")) {
            EmbedBuilder page2 = new EmbedBuilder();
            page2.copyFrom(queue);

            for (int i = 5; i < queueSize && i < 10; ++i) {
                page2
                    .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 2");
            }
            event.editMessageEmbeds(page2.build())
                    .setActionRow(Button.primary("page1", "Previous Page"), Button.primary("page3", "Next Page"))
                    .queue();
        }

        else if (event.getComponentId().equals("page3")) {
            EmbedBuilder page3 = new EmbedBuilder();
            page3.copyFrom(queue);

            for (int i = 10; i < queueSize && i < 15; ++i) {
                page3
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 3");
            }
            event.editMessageEmbeds(page3.build())
                    .setActionRow(Button.primary("page2", "Previous Page"), Button.primary("page4", "Next Page"))
                    .queue();
        }
        else if (event.getComponentId().equals("page4")) {
            EmbedBuilder page4 = new EmbedBuilder();
            page4.copyFrom(queue);

            for (int i = 15; i < queueSize && i < 20; ++i) {
                page4
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 4");
            }
            event.editMessageEmbeds(page4.build())
                    .setActionRow(Button.primary("page3", "Previous Page"), Button.primary("page5", "Next Page"))
                    .queue();
        }
        else if (event.getComponentId().equals("page5")) {
            EmbedBuilder page5 = new EmbedBuilder();
            page5.copyFrom(queue);

            for (int i = 20; i < queueSize && i < 25; ++i) {
                page5
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 5");
            }
            event.editMessageEmbeds(page5.build())
                    .setActionRow(Button.primary("page4", "Previous Page"), Button.primary("page6", "Next Page"))
                    .queue();
        }
        else if (event.getComponentId().equals("page6")) {
            EmbedBuilder page6 = new EmbedBuilder();
            page6.copyFrom(queue);

            for (int i = 25; i < queueSize && i < 30; ++i) {
                page6
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 6");
            }
            event.editMessageEmbeds(page6.build())
                    .setActionRow(Button.primary("page5", "Previous Page"), Button.primary("page7", "Next Page"))
                    .queue();
        }
        else if (event.getComponentId().equals("page7")) {
            EmbedBuilder page7 = new EmbedBuilder();
            page7.copyFrom(queue);

            for (int i = 30; i < queueSize && i < 35; ++i) {
                page7
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 7");
            }
            event.editMessageEmbeds(page7.build())
                    .setActionRow(Button.primary("page6", "Previous Page"), Button.primary("page8", "Next Page"))
                    .queue();
        }
        else if (event.getComponentId().equals("page8")) {
            EmbedBuilder page8 = new EmbedBuilder();
            page8.copyFrom(queue);

            for (int i = 35; i < queueSize && i < 40; ++i) {
                page8
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 8");
            }
            event.editMessageEmbeds(page8.build())
                    .setActionRow(Button.primary("page7", "Previous Page"), Button.primary("page9", "Next Page"))
                    .queue();
        }
        else if (event.getComponentId().equals("page9")) {
            EmbedBuilder page9 = new EmbedBuilder();
            page9.copyFrom(queue);

            for (int i = 40; i < queueSize && i < 45; ++i) {
                page9
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 9");
            }
            event.editMessageEmbeds(page9.build())
                    .setActionRow(Button.primary("page8", "Previous Page"), Button.primary("page10", "Next Page"))
                    .queue();
        }
        else if (event.getComponentId().equals("page10")) {
            EmbedBuilder page10 = new EmbedBuilder();
            page10.copyFrom(queue);

            for (int i = 45; i < queueSize && i < 50; ++i) {
                page10
                        .addField("[" + (i+1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)
                        .setFooter("Page 10");
            }
            event.editMessageEmbeds(page10.build())
                    .setActionRow(Button.primary("page9", "Previous Page"), Button.primary("page1", "Page 1"))
                    .queue();
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
