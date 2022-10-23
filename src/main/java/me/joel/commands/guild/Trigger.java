package me.joel.commands.guild;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Trigger extends ListenerAdapter {

    // User ID : Trigger
    static HashMap<String, String> triggers = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("trigger")) {
            String triggerWord = event.getOption("word").getAsString();
            String id = event.getUser().getId();

            triggers.put(id, triggerWord);

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.green)
                    .setDescription("Trigger set!");

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild() || event.getMember().getUser().isBot()) return;

        String trigger = event.getMessage().getContentRaw();
        User user;

        // Check if contains trigger word for user
        for (String id : triggers.keySet()) {

            // True if ID contains value matching message
            if (triggers.get(id).equals(trigger) && (event.getGuild().getMemberById(id) != null)) {
                user = event.getGuild().getMemberById(id).getUser();

                // If message is from user
//                if (event.getMember().getUser() == user) continue;

                // Embed
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Message Trigger");
                builder.setColor(Util.randColor());
                builder.setFooter("All timestamps are formatted in PST / UTC+7 !");
                builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());

                // Jump URL
                String link = event.getMessage().getJumpUrl();

                // Spam Check
                // Scans previous 100 messages and returns if message contained trigger word within 30 seconds
                MessageHistory log = event.getChannel().getHistoryBefore(event.getMessageId(), 100).complete();

                for (var message : log.getRetrievedHistory()) {
                    if (message.getContentRaw().contains(trigger)) {
                        long time_diff = event.getMessage().getTimeCreated().toEpochSecond() - message.getTimeCreated().toEpochSecond();
                        if (time_diff <= 30) return;
                    }
                }

                // Retrieve last 4 messages in channel message history
                MessageHistory history = event.getChannel().getHistoryBefore(event.getMessageId(), 4).complete();
                List<String> messages = new ArrayList<>();

                // Add messages to list
                for (Message message : history.getRetrievedHistory()) {

                    // Timestamp
                    int hours = message.getTimeCreated().getHour() + 17; if (hours >= 24) hours -= 24;
                    int minutes = message.getTimeCreated().getMinute();
                    int seconds = message.getTimeCreated().getSecond();
                    String time = hours + ":" + minutes + ":" + seconds;

                    messages.add("**[" + time + "] " + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator() + ":** " + message.getContentRaw() + "\n");
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
