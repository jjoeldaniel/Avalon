package me.joel.commands.guild;

import me.joel.Console;
import me.joel.Database;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Trigger extends ListenerAdapter {

    // User ID : Trigger
    static HashMap<String, String> triggers = new HashMap<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        String sql = "SELECT * FROM triggers";

        try {
            ResultSet set = Database.getConnect().createStatement().executeQuery(sql);
            int size = 1000;

            try {
                set.next();
                for (int i = 1; i < size; i++) {
                    String id = set.getString(1);
                    String message = set.getString(2);
                    set.next();

                    if (id.equals("null")) break;
                    triggers.put(id, message);
                }
            }
            catch (Exception ignore) {}

        } catch (SQLException e) {
            Console.warn("Unable to initialize Triggers");
            e.printStackTrace();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("trigger"))
        {
            if (event.getSubcommandName().equals("new"))
            {
                String triggerWord = event.getOption("word").getAsString().toLowerCase();
                String id = event.getUser().getId();

                triggers.put(id, triggerWord);

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.green)
                        .setDescription("Trigger set!");

                try {
                    String sql = "REPLACE INTO triggers(user_id, trigger_word) values ('" + id + "', '" + triggerWord + "')";
                    Database.getConnect().createStatement().execute(sql);

                } catch (SQLException e) {
                    Console.warn("Unable to add Triggers");
                    e.printStackTrace();

                    builder.setDescription("Failed to set trigger, try again!");
                    builder.setColor(Color.red);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
            else if (event.getSubcommandName().equals("reset"))
            {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Trigger deleted!");

                try {
                    String sql = "DELETE FROM triggers WHERE user_id=" + event.getUser().getId();
                    Database.getConnect().createStatement().execute(sql);

                } catch (SQLException e) {
                    Console.warn("Unable to delete Triggers");
                    e.printStackTrace();

                    builder.setDescription("Failed to delete trigger, try again!");
                    builder.setColor(Color.red);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild() || event.getMember() == null || event.getMember().getUser().isBot()) return;

        String trigger = event.getMessage().getContentRaw().toLowerCase();
        User user;

        // Check if contains trigger word for user
        for (String id : triggers.keySet()) {

            // True if ID contains value matching message
            if (trigger.contains(triggers.get(id))) {

                try {
                    event.getGuild().retrieveMemberById(id).complete();
                } catch (ErrorResponseException ignore) {
                    continue;
                }

                user = event.getGuild().getMemberById(id).getUser();

                // If message is from user
                if (event.getMember().getUser() == user) continue;

                // View Permission check
                Member member = event.getGuild().getMemberById(id);
                if (!member.hasPermission(event.getGuildChannel(), Permission.VIEW_CHANNEL)) return;

                Console.debug("Trigger \"" + trigger +  "\" for user: " + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")");

                // Embed
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Message Trigger");
                builder.setColor(Util.randColor());
                builder.setFooter("All timestamps are formatted in PST / UTC+7 !");
                builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());

                // Jump URL
                String link = event.getMessage().getJumpUrl();

                // Spam Check
                // Scans previous 25 messages and returns if message contained trigger word within 10 seconds
                MessageHistory log = event.getChannel().getHistoryBefore(event.getMessageId(), 25).complete();

                for (var message : log.getRetrievedHistory()) {
                    if (message.getContentRaw().contains(trigger)) {
                        long time_diff = event.getMessage().getTimeCreated().toEpochSecond() - message.getTimeCreated().toEpochSecond();
                        if (time_diff <= 15) return;
                    }
                }

                // Retrieve last 4 messages in channel message history
                MessageHistory history = event.getChannel().getHistoryBefore(event.getMessageId(), 4).complete();
                List<String> messages = new ArrayList<>();

                // Add messages to list
                for (Message message : history.getRetrievedHistory()) {

                    // Timestamp
                    int hours = message.getTimeCreated().getHour()-8; if (hours < 0) hours+=12;
                    int minutes = message.getTimeCreated().getMinute();
                    int seconds = message.getTimeCreated().getSecond();

                    String hour_string = String.valueOf(hours);
                    String minute_string = String.valueOf(minutes);
                    String second_string = String.valueOf(seconds);

                    if (hours < 10) {
                        hour_string = "0" + hours;
                    }
                    if (minutes < 10) {
                        minute_string = "0" + minutes;
                    }
                    if (seconds < 10) {
                        second_string = "0" + seconds;
                    }

                    String time = hour_string + ":" + minute_string + ":" + second_string;

                    messages.add("**[" +  time + "] " + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator() + ":** " + message.getContentRaw() + "\n");
                }
                // Reverse messages in order of least -> most recent
                Collections.reverse(messages);

                // Add trigger message
                builder.addField("", "**[" + TimeFormat.TIME_LONG.now() + "] " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + ":** " + event.getMessage().getContentRaw(), false);

                // Finish embed
                String message = String.join("", messages);
                builder.setDescription(message);
                builder.addField("**Source Message**", "[Jump to](" + link + ")" , false);

                // DM
                user.openPrivateChannel()
                        .flatMap(channel -> channel.sendMessageEmbeds(builder.build()))
                        .queue();
            }
        }
    }
}
