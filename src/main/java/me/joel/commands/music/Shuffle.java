package me.joel.commands.music;

import me.joel.lavaplayer.AudioEventAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Shuffle extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("shuffle")) {

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (AudioEventAdapter.isLooping()) {
                builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("You can't enable /shuffle and /queue at the same time!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }

            AudioEventAdapter.setShuffle(!AudioEventAdapter.isShuffling());

            if (AudioEventAdapter.isShuffling()) {
                builder = new EmbedBuilder()
                        .setColor(Color.green)
                        .setDescription("The queue is now shuffling!");
            } else {
                builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("The queue is no longer shuffling!");
            }

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
