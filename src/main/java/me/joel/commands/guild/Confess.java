package me.joel.commands.guild;

import me.joel.Database;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Confess extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("confess")) {

            String message = event.getOption("message").getAsString();

            // If message contains @role or @everyone
            if (event.getOption("message").getMentions().getRoles().size() > 0 || message.contains("@everyone")) {
                EmbedBuilder noMentions = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("You can't @ roles in a confession!");

                event.replyEmbeds(noMentions.build()).setEphemeral(true).queue();
                return;
            }
            // If message contains @member
            if (event.getOption("message").getMentions().getUsers().size() > 0) {
                EmbedBuilder noMentions = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("You can't @ someone in a confession!");

                event.replyEmbeds(noMentions.build()).setEphemeral(true).queue();
                return;
            }

            // Get ID
            TextChannel channel = null;

            String sql = "SELECT confession_ch FROM guild_settings WHERE guild_id=" + event.getGuild().getId();
            try {
                ResultSet set = Database.getConnect().createStatement().executeQuery(sql);

                String channelID = set.getString(1);

                if (channelID != null) {
                    channel = event.getGuild().getTextChannelById(channelID);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // No channel Found
            if (channel == null) {
                EmbedBuilder confessionError = new EmbedBuilder()
                        .setTitle("Error!")
                        .setDescription("No confession channel found!")
                        .setColor(Color.red);

                event.replyEmbeds(confessionError.build()).setEphemeral(true).queue();
                return;
            }

            // Channel found

            // Confession Post
            EmbedBuilder confessionPost = new EmbedBuilder()
                    .setTitle("Anonymous Confession")
                    .setDescription("\"" + message + "\"")
                    .setColor(Util.randColor());

            // Submit message
            EmbedBuilder confessionSubmit = new EmbedBuilder()
                    .setTitle("Confession Submitted")
                    .setDescription("\"" + message + "\"")
                    .setColor(Color.green);

            channel.sendMessageEmbeds(confessionPost.build()).queue();
            event.replyEmbeds(confessionSubmit.build()).setEphemeral(true).queue();
        }
    }
}
