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

public class MusicCommands extends ListenerAdapter {

    private static Member member;
    static List<AudioTrack> playlist;
    static int queueSize;

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
            }

        } catch (Exception e) {
            event.getHook().sendMessageEmbeds(Util.genericError().build()).queue();
        }
    }

    /**
     * @return Event channel
     */
    public static MessageChannelUnion returnChannel() {
        return newEvent.getChannel();
    }
}
