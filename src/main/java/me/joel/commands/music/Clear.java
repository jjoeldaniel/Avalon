package me.joel.commands.music;

import me.joel.AudioEventAdapter;
import me.joel.PlayerManager;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Clear extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        // Avalon
        Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
        assert bot != null;

        // JDA AudioManager
        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        if (invoke.equals("clear")) {

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
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
}
