package me.joel.commands.global;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Ping extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("ping")) {
            EmbedBuilder ping = new EmbedBuilder()
                    .setTitle("Pong!")
                    .setColor(Util.randColor());
            event.replyEmbeds(ping.build()).setEphemeral(true).queue();
        }
    }
}
