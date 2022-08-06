package me.joel.commands.guild;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Confess extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("confess")) {

            String message = Objects.requireNonNull(event.getOption("message")).getAsString();
            String channelID = "";

            // If message contains @role or @everyone
            if (Objects.requireNonNull(event.getOption("message")).getMentions().getRoles().size() > 0 || message.contains("@everyone")) {
                EmbedBuilder noMentions = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setDescription("You can't @ roles in a confession!");

                event.replyEmbeds(noMentions.build()).setEphemeral(true).queue();
                return;
            }
            // If message contains @member
            if (Objects.requireNonNull(event.getOption("message")).getMentions().getUsers().size() > 0) {
                EmbedBuilder noMentions = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setDescription("You can't @ someone in a confession!");

                event.replyEmbeds(noMentions.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder confessionPost = new EmbedBuilder()
                    .setTitle("Anonymous Confession")
                    .setDescription("\"" + message + "\"")
                    .setColor(Util.randColor());

            // Find confessions channel
            try {
                int channelNum = Objects.requireNonNull(event.getGuild()).getTextChannels().size();
                for (int i = 0; i < channelNum; ++i) {
                    if (event.getGuild().getTextChannels().get(i).getName().contains("confessions")) {
                        channelID = event.getGuild().getTextChannels().get(i).getId();
                    }
                }

                Objects.requireNonNull(event.getGuild().getTextChannelById(channelID)).sendMessageEmbeds(confessionPost.build()).queue();

                EmbedBuilder confessionSubmit = new EmbedBuilder()
                        .setTitle("Confession Submitted")
                        .setDescription("\"" + message + "\"")
                        .setColor(Util.randColor());

                event.replyEmbeds(confessionSubmit.build()).setEphemeral(true).queue();
            } catch (Exception channelNotFound) {
                EmbedBuilder confessionError = new EmbedBuilder()
                        .setTitle("Error!")
                        .setDescription("No confession channel found!")
                        .setColor(Util.randColor());

                event.replyEmbeds(confessionError.build()).setEphemeral(true).queue();
            }
        }
    }
}
