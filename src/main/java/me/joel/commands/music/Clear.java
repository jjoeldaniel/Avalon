package me.joel.commands.music;

import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Clear extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("clear")) {

            /// JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size() == 0) {
                builder = new EmbedBuilder()
                    .setDescription("The queue is empty or an error has occurred!")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(Color.red);

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            AudioEventAdapter.setLoop(false);
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.clear();
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.destroy();

            builder = new EmbedBuilder()
                .setDescription("Queue cleared")
                .setFooter("Use /help for a list of music commands!")
                .setColor(Color.green);

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
