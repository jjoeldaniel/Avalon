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
                    event.getTextChannel().sendMessage("You need to be in a voice channel to use `/play`").queue();
                    return;
                }
                event.deferReply().queue();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                String link = Objects.requireNonNull(event.getOption("song")).getAsString();

                if (!isURL(link)) {
                    link = ("ytsearch:" + link + " audio");
                    System.out.println("Invalid link. New link: " + link);
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

            event.getHook().sendMessage("sent").queue();
            event.getHook().deleteOriginal().queue();
        }

        // Pause
        if (event.getName().equals("pause")) {
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(true);
            event.reply("Playback paused").setEphemeral(true).queue();
        }

        // Resume
        if (event.getName().equals("resume")) {
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(false);
            event.reply("Playback resumed").setEphemeral(true).queue();
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
                        .setTitle("Queue [" + queueSize + "]")
                        .setDescription("No songs currently playing")
                        .setColor(Util.randColor());
                event.replyEmbeds(builder.build()).queue();
                return;
            }

            String currentArtist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().author;
            List<AudioTrack> playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();
            System.out.print("queueSize = " + queueSize);

            if (queueSize == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .setColor(Util.randColor());
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 1) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 2) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;

            }
            if (queueSize == 3) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;

            }
            if (queueSize == 4) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                        .addField("[4]", playlist.get(3).getInfo().title + " by " + playlist.get(3).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 5) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                        .addField("[4]", playlist.get(3).getInfo().title + " by " + playlist.get(3).getInfo().author, false)
                        .addField("[5]", playlist.get(4).getInfo().title + " by " + playlist.get(4).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 6) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                        .addField("[4]", playlist.get(3).getInfo().title + " by " + playlist.get(3).getInfo().author, false)
                        .addField("[5]", playlist.get(4).getInfo().title + " by " + playlist.get(4).getInfo().author, false)
                        .addField("[6]", playlist.get(5).getInfo().title + " by " + playlist.get(5).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 7) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                        .addField("[4]", playlist.get(3).getInfo().title + " by " + playlist.get(3).getInfo().author, false)
                        .addField("[5]", playlist.get(4).getInfo().title + " by " + playlist.get(4).getInfo().author, false)
                        .addField("[6]", playlist.get(5).getInfo().title + " by " + playlist.get(5).getInfo().author, false)
                        .addField("[7]", playlist.get(6).getInfo().title + " by " + playlist.get(6).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 8) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                        .addField("[4]", playlist.get(3).getInfo().title + " by " + playlist.get(3).getInfo().author, false)
                        .addField("[5]", playlist.get(4).getInfo().title + " by " + playlist.get(4).getInfo().author, false)
                        .addField("[6]", playlist.get(5).getInfo().title + " by " + playlist.get(5).getInfo().author, false)
                        .addField("[7]", playlist.get(6).getInfo().title + " by " + playlist.get(6).getInfo().author, false)
                        .addField("[8]", playlist.get(7).getInfo().title + " by " + playlist.get(7).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (queueSize == 9) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Queue [" + queueSize + "]")
                        .addField("Currently playing", currentSong + " by " + currentArtist, false)
                        .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                        .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                        .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                        .addField("[4]", playlist.get(3).getInfo().title + " by " + playlist.get(3).getInfo().author, false)
                        .addField("[5]", playlist.get(4).getInfo().title + " by " + playlist.get(4).getInfo().author, false)
                        .addField("[6]", playlist.get(5).getInfo().title + " by " + playlist.get(5).getInfo().author, false)
                        .addField("[7]", playlist.get(6).getInfo().title + " by " + playlist.get(6).getInfo().author, false)
                        .addField("[8]", playlist.get(7).getInfo().title + " by " + playlist.get(7).getInfo().author, false)
                        .addField("[9]", playlist.get(8).getInfo().title + " by " + playlist.get(8).getInfo().author, false)
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.replyEmbeds(builder.build()).queue();
                return;
            }
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Queue [" + queueSize + "]")
                    .addField("Currently playing", currentSong + " by " + currentArtist, false)
                    .addField("[1]", playlist.get(0).getInfo().title + " by " + playlist.get(0).getInfo().author, false)
                    .addField("[2]", playlist.get(1).getInfo().title + " by " + playlist.get(1).getInfo().author, false)
                    .addField("[3]", playlist.get(2).getInfo().title + " by " + playlist.get(2).getInfo().author, false)
                    .addField("[4]", playlist.get(3).getInfo().title + " by " + playlist.get(3).getInfo().author, false)
                    .addField("[5]", playlist.get(4).getInfo().title + " by " + playlist.get(4).getInfo().author, false)
                    .addField("[6]", playlist.get(5).getInfo().title + " by " + playlist.get(5).getInfo().author, false)
                    .addField("[7]", playlist.get(6).getInfo().title + " by " + playlist.get(6).getInfo().author, false)
                    .addField("[8]", playlist.get(7).getInfo().title + " by " + playlist.get(7).getInfo().author, false)
                    .addField("[9]", playlist.get(8).getInfo().title + " by " + playlist.get(8).getInfo().author, false)
                    .addField("[10]", playlist.get(9).getInfo().title + " by " + playlist.get(9).getInfo().author, false)
                    .setColor(Util.randColor())
                    .setFooter("Use /help for a list of music commands!");
            event.replyEmbeds(builder.build()).queue();

        }
    }

    // Grabs event member
    public Member getMember() {
        return member;
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
