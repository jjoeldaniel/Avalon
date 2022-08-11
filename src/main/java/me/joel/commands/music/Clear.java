package me.joel.commands.music;

import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Clear extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("clear")) {

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            EmbedBuilder builder1;
            builder1 = Util.compareVoice(Objects.requireNonNull(event.getMember()));

            if (builder1 != null) {
                event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                return;
            }

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size() == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("The queue is empty or an error has occurred!")
                        .setFooter("Use /help for a list of music commands!")
                        .setColor(me.joel.Util.randColor());

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            AudioEventAdapter.setLoop(false);
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.clear();
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.destroy();

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("Queue cleared")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(me.joel.Util.randColor());

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
