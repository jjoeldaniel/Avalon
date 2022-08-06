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
