package me.joel.commands.music;

import me.joel.AudioEventAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Shuffle extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("shuffle")) {

            // Avalon
            Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
            assert bot != null;

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                AudioEventAdapter.setLoop(false);
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

            if (AudioEventAdapter.isShuffling()) AudioEventAdapter.setShuffle(false);
            else AudioEventAdapter.setShuffle(true);

            EmbedBuilder builder;

            if (AudioEventAdapter.isLooping()) {
                builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("You can't enable /shuffle and /queue at the same time!");
            }

            if (AudioEventAdapter.isShuffling()) {
                builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("The queue is now shuffling!");
            } else {
                builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("The queue is no longer shuffling!");
            }

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
