package me.joel;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class MusicCommands extends ListenerAdapter {

    static Member member;

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        member = event.getMember();

        // Play
        if (event.getName().equals("play")) {
            try {
                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/play`!");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }
                event.deferReply().queue();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                String link = Objects.requireNonNull(event.getOption("song")).getAsString();

                if (!isURL(link)) {
                    link = ("ytsearch:" + link + " audio");
                }

                // Joins VC
                audioManager.openAudioConnection(memberChannel);
                Member bot = event.getMember().getGuild().getMemberById("971239438892019743");
                assert bot != null;
                event.getGuild().deafen(bot, true).queue();

                // Plays song
                PlayerManager.getINSTANCE().loadAndPlay(event.getTextChannel(), link);
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setVolume(30);
            }
            catch (Exception exception) {
                System.out.println("Error occurred during playback");
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .addField("Song queued", "If you see this message, an error has occurred!", false);

            event.getHook().sendMessageEmbeds(builder.build()).queue();
            event.getHook().deleteOriginal().queue();
        }

        // Pause
        if (event.getName().equals("pause")) {
            if (!PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.isPaused()) {
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(true);

                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Playback paused")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("Error occurred attempting to pause playback")
                    .setColor(Util.randColor());

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // Resume
        if (event.getName().equals("resume")) {

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.isPaused()) {
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(false);

                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Playback resumed")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("Error occurred attempting to resume playback")
                    .setColor(Util.randColor());

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // Clear
        if (event.getName().equals("clear")) {
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.destroy();
            event.reply("Queue cleared").queue();
        }

        // Skip
        if (event.getName().equals("skip")) {
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
            event.reply("Song skipped").queue();
        }

        // Queue
        if (event.getName().equals("queue")) {

            String currentSong;
            int queueSize = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size();

            try {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .setDescription("No songs currently playing")
                        .setColor(Util.randColor());
                event.replyEmbeds(builder.build()).queue();
                return;
            }

            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            List<AudioTrack> playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();

            if (queueSize == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 1) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 2) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;

            }
            if (queueSize == 3) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;

            }
            if (queueSize == 4) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 5) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 6) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false)
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + PlayerManager.getThumbnail(playlist.get(5).getInfo().uri) + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 7) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false)
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + PlayerManager.getThumbnail(playlist.get(5).getInfo().uri) + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + PlayerManager.getThumbnail(playlist.get(6).getInfo().uri) + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 8) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false)
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                        .addField("[8]", "[" + playlist.get(7).getInfo().title + "](" + playlist.get(7).getInfo().uri + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 9) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                        .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                        .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                        .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                        .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false)
                        .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                        .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                        .addField("[8]", "[" + playlist.get(7).getInfo().title + "](" + playlist.get(7).getInfo().uri + ")\n", false)
                        .addField("[9]", "[" + playlist.get(8).getInfo().title + "](" + playlist.get(8).getInfo().uri + ")\n", false)
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            EmbedBuilder builder = new EmbedBuilder()
                    .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                    .setTitle("Queue [" + queueSize + "]")
                    .addField("Currently playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addField("[1]", "[" + playlist.get(0).getInfo().title + "](" + playlist.get(0).getInfo().uri + ")\n", false)
                    .addField("[2]", "[" + playlist.get(1).getInfo().title + "](" + playlist.get(1).getInfo().uri + ")\n", false)
                    .addField("[3]", "[" + playlist.get(2).getInfo().title + "](" + playlist.get(2).getInfo().uri + ")\n", false)
                    .addField("[4]", "[" + playlist.get(3).getInfo().title + "](" + playlist.get(3).getInfo().uri + ")\n", false)
                    .addField("[5]", "[" + playlist.get(4).getInfo().title + "](" + playlist.get(4).getInfo().uri + ")\n", false)
                    .addField("[6]", "[" + playlist.get(5).getInfo().title + "](" + playlist.get(5).getInfo().uri + ")\n", false)
                    .addField("[7]", "[" + playlist.get(6).getInfo().title + "](" + playlist.get(6).getInfo().uri + ")\n", false)
                    .addField("[8]", "[" + playlist.get(7).getInfo().title + "](" + playlist.get(7).getInfo().uri + ")\n", false)
                    .addField("[9]", "[" + playlist.get(8).getInfo().title + "](" + playlist.get(8).getInfo().uri + ")\n", false)
                    .addField("[10]", "[" + playlist.get(9).getInfo().title + "](" + playlist.get(8).getInfo().uri + ")\n", false)
                    .setColor(Util.randColor())
                    .setThumbnail(Util.randomThumbnail())
                    .setFooter("Use /help for a list of music commands!");
            event.replyEmbeds(builder.build()).queue();

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
