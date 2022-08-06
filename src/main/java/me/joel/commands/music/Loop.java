package me.joel.commands.music;

import me.joel.AudioEventAdapter;
import me.joel.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Loop extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        // Avalon
        Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
        assert bot != null;

        // JDA AudioManager
        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        if (invoke.equals("loop")) {

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                event.getHook().sendMessageEmbeds(Util.VCRequirement.build()).setEphemeral(true).queue();
                return;
            }

            // Compare JDA and member voice state
            if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                long memberVC = Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong();
                long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                if (!(botVC == memberVC)) {
                    event.getHook().sendMessageEmbeds(Util.sameVCRequirement.build()).setEphemeral(true).queue();
                    return;
                }
            }

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack() == null) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("There is no song currently playing!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder;

            if (AudioEventAdapter.isShuffling()) {
                builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("You can't enable /shuffle and /queue at the same time!");
            }
            else if (!AudioEventAdapter.isLooping()) {
                AudioEventAdapter.setLoop(true);
                builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("Song is now looping!");
            } else {
                AudioEventAdapter.setLoop(false);
                builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("Song is no longer looping!");
            }

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
