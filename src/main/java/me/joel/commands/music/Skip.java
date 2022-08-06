package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.AudioEventAdapter;
import me.joel.PlayerManager;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Skip extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        // Avalon
        Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
        assert bot != null;

        // JDA AudioManager
        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        if (invoke.equals("skip")) {

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                AudioEventAdapter.setLoop(false);
                event.replyEmbeds(Embeds.VCRequirement.build()).setEphemeral(true).queue();
                return;
            }

            // Compare JDA and member voice state
            if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                if (!(botVC == memberVC)) {
                    event.replyEmbeds(Embeds.sameVCRequirement.build()).setEphemeral(true).queue();
                    return;
                }
            }

            List<AudioTrack> playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();
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

            if (event.getOption("target") != null) {
                int songSkip = (Objects.requireNonNull(event.getOption("target")).getAsInt()) - 1;

                if (songSkip >= playlist.size() || songSkip < 1) {
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

            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
            event.replyEmbeds(builder.build()).queue();
        }
    }
}
