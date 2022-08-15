package me.joel.commands.mod;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Poll extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("poll")) {

            var poll = event.getOption("message").getAsString();

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setTitle("Poll")
                    .addField(event.getMember().getEffectiveName() + " asks", "*" + poll + "*", false);

            EmbedBuilder submit = new EmbedBuilder()
                    .setColor(Color.green)
                    .setDescription("Poll submitted!");

            event.replyEmbeds(submit.build()).setEphemeral(true).queue();

            Emoji thumbsUp = Emoji.fromFormatted("U+1F44D");
            Emoji thumbsDown = Emoji.fromFormatted("U+1F44E");

            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> {
                message.addReaction(thumbsUp).queue();
                message.addReaction(thumbsDown).queue();
            });


        }
    }
}
