package me.joel.commands.guild;

import me.joel.Database;
import me.joel.GuildEvents;
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

        if (event.getSubcommandName() != null && event.getSubcommandName().equals("confession"))
        {
            TextChannel channel = null;
            int confession_number = event.getOption("number").getAsInt();

            try {
                String sql = "SELECT mod_ch FROM guild_settings WHERE guild_id=" + event.getGuild().getId();
                ResultSet set = Database.getConnect().createStatement().executeQuery(sql);
                String channelID = set.getString(1);

                if (channelID != null) {
                    channel = event.getGuild().getTextChannelById(channelID);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            // No channel Found
            if (channel == null) {
                EmbedBuilder error = new EmbedBuilder()
                        .setTitle("Error!")
                        .setDescription("Your servers moderators haven't configured the bot yet!")
                        .setColor(Color.red);

                event.replyEmbeds(error.build()).setEphemeral(true).queue();
                return;
            }

            // TODO: Add action buttons
            String message = GuildEvents.message_record.get(event.getGuild()).get(confession_number);

            // Check if number exists
            if (message == null) {
                event.reply("Confession #" + confession_number + " not found!").setEphemeral(true).queue();
                return;
            }
            event.reply("Confession #" + confession_number + " reported!").setEphemeral(true).queue();

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.red)
                    .setTitle("Report")
                    .addField("Confession #" + confession_number, "\"" + message + "\"", false);

            channel.sendMessageEmbeds(builder.build()).queue();

        }
        else if (invoke.equals("confess"))
        {
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

            // Get list of confession messages
            int num = GuildEvents.confession_record.get(event.getGuild()).size();

            // Confession Post
            EmbedBuilder confessionPost = new EmbedBuilder()
                    .setTitle("Anonymous Confession #" + (++num))
                    .setDescription("\"" + message + "\"")
                    .setColor(Util.randColor())
                    .setFooter("Use /report " + num + " to report this confession!");

            // Submit message
            EmbedBuilder confessionSubmit = new EmbedBuilder()
                    .setTitle("Confession Submitted")
                    .setDescription("\"" + message + "\"")
                    .setColor(Color.green);

            int finalNum = num;
            channel.sendMessageEmbeds(confessionPost.build()).queue(message1 -> {
                GuildEvents.message_record.get(event.getGuild()).put(finalNum, message);
                GuildEvents.confession_record.get(event.getGuild()).put(finalNum, event.getMember());
                    });
            event.replyEmbeds(confessionSubmit.build()).setEphemeral(true).queue();
        }
    }
}
