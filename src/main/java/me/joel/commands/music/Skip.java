package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
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

        if (invoke.equals("skip")) {

            // Avalon
            Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
            assert bot != null;

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                event.replyEmbeds(Util.VCRequirement.build()).setEphemeral(true).queue();
                return;
            }

            // Compare JDA and member voice state
            if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                if (!(botVC == memberVC)) {
                    event.replyEmbeds(Util.sameVCRequirement.build()).setEphemeral(true).queue();
                    return;
                }
            }

            List<AudioTrack> playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();
            AudioTrack audioTrack;
            audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();

            if (audioTrack == null) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("No song is playing or an error has occurred!")
                        .setColor(me.joel.Util.randColor())
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("Song(s) skipped")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(me.joel.Util.randColor());

            if (event.getOption("target") != null) {
                int songSkip = (Objects.requireNonNull(event.getOption("target")).getAsInt()) - 1;

                if (songSkip >= playlist.size() || songSkip < 0) {
                    EmbedBuilder skipOutOfBounds = new EmbedBuilder()
                            .setColor(me.joel.Util.randColor())
                            .setDescription("That isn't a valid song number!")
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(skipOutOfBounds.build()).setEphemeral(true).queue();
                    return;
                }

                AudioEventAdapter.setLoop(false);
                AudioEventAdapter.setShuffle(false);
                AudioTrack songToSkip = playlist.get(songSkip);
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.remove(songToSkip);

                event.replyEmbeds(builder.build()).queue();
                return;
            }

            else if (event.getOption("num") != null) {
                int songs = Objects.requireNonNull(event.getOption("num")).getAsInt();

                if (songs > playlist.size() || songs < 1) {
                    EmbedBuilder builder1 = new EmbedBuilder()
                            .setColor(me.joel.Util.randColor())
                            .setDescription("That isn't a valid number!")
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                    return;
                }

                AudioEventAdapter.setLoop(false);
                AudioEventAdapter.setShuffle(false);
                for (int i = 0; i < songs; i++) {
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
                }

                event.replyEmbeds(builder.build()).queue();
                return;
            }

            AudioEventAdapter.setLoop(false);

            if (AudioEventAdapter.isShuffling()) {
                int num = me.joel.Util.randomWithRange(0, playlist.size());

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
}
