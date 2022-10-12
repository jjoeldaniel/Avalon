package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GuildEvents extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Console.info("Active Bot: " + event.getJDA().getSelfUser().getName());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        Console.info("Left server: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
    }
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Console.info("Joined server: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");

        final String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot";

        EmbedBuilder builder = new EmbedBuilder()
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .setTitle("Thank you for inviting Avalon to " + event.getGuild().getName() + "!")
                .setColor(Util.randColor())
                .setDescription("Make sure to use /help to get the full commands list!")
                .addBlankField(false)
                .addField("Need to contact us?", "Add joel#0005 on Discord for questions!", false)
                .addField("Want to invite Avalon to another server?", "Click on my profile and click \" Add to Server\" to invite Avalon!", false);

        TextChannel channel = event.getGuild().getSystemChannel();

        // Default to "general" channel if no system channel
        if (channel == null) {
            String generalID = Util.findChannel("general", event.getGuild());

            if (generalID != null) {
                event.getGuild().getTextChannelById(generalID).sendMessageEmbeds(builder.build()).setActionRow(
                        Button.link(inviteLink, "Invite")).queue();
            }
            else Console.warn("No system/general channel found for guild: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
        }
        else {
            channel.sendMessageEmbeds(builder.build()).setActionRow(
                Button.link(inviteLink, "Invite")).queue();
        }

        // Initializes guild settings
        try {
            Connection conn = Database.getConnect();
            String sql = "INSERT INTO guild_settings(guild_id, insults, gm_gn, now_playing) VALUES (" + event.getGuild().getId() + ", 1, 1, 1)";
            String sql2 = "INSERT INTO starboard_settings(guild_id, star_limit, star_self) VALUES (" + event.getGuild().getId() + "), 3, 0";

            conn.createStatement().execute(sql);
            conn.createStatement().execute(sql2);
         } catch (SQLException e) {
            Console.warn("Failed to initialize guild settings");
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        // Initializes guild settings if nothing found
        try {
            String sql = "SELECT * FROM guild_settings WHERE guild_id=" + event.getGuild().getId();
            ResultSet set = Database.getConnect().createStatement().executeQuery(sql);

            if (set.getInt(1) == 0) {
                String sql2 = "INSERT INTO guild_settings(guild_id, insults, gm_gn, now_playing) VALUES (" + event.getGuild().getId() + ", 1, 1, 1)";
                Database.getConnect().createStatement().execute(sql2);
            }
        } catch (SQLException e) {
            Console.warn("Failed to initialize guild settings");
            e.printStackTrace();
        }

        // Initializes guild starboard settings if nothing found
        try {
            String sql = "SELECT * FROM starboard_settings WHERE guild_id=" + event.getGuild().getId();
            ResultSet set = Database.getConnect().createStatement().executeQuery(sql);

            if (set.getInt(1) == 0) {
                String sql2 = "INSERT INTO starboard_settings(guild_id, star_limit, star_self) VALUES (" + event.getGuild().getId() + ", 3, 0)";
                Database.getConnect().createStatement().execute(sql2);
            }
        } catch (SQLException e) {
            Console.warn("Failed to initialize guild settings");
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Member member = event.getMember();
        TextChannel channel = null;

        // Get ID
        String sql = "SELECT join_ch FROM guild_settings WHERE guild_id=" + event.getGuild().getId();
        try {
            ResultSet set = Database.getConnect().createStatement().executeQuery(sql);

            String channelID = set.getString(1);

            if (channelID != null) {
                channel = event.getGuild().getTextChannelById(channelID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (channel == null) return;

        EmbedBuilder memberJoin = new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("A new member has joined!")
                .setDescription
                        (
                        "Welcome " + member.getAsMention() + " to " + event.getGuild().getName() +
                        "! There are now " + event.getGuild().getMemberCount() + " members in " + event.getGuild().getName() + "."
                        )
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setFooter("User: " + member.getUser().getName() +"#" + member.getUser().getDiscriminator() + " ID: " + member.getId());

        channel.sendMessageEmbeds(memberJoin.build()).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        User user = event.getUser();
        EmbedBuilder memberLeave = new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("A member has left!")
                .setDescription
                        (
                         user.getAsMention() + " has left " + event.getGuild().getName() +
                        "! There are now " + event.getGuild().getMemberCount() + " members in " + event.getGuild().getName() + "."
                        )
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setFooter("User: " + user.getName() +"#" + user.getDiscriminator() + " ID: " + user.getId());

        // Get ID
        TextChannel channel = null;

        String sql = "SELECT leave_ch FROM guild_settings WHERE guild_id=" + event.getGuild().getId();
        try {
            ResultSet set = Database.getConnect().createStatement().executeQuery(sql);

            String channelID = set.getString(1);

            if (channelID != null) {
                channel = event.getGuild().getTextChannelById(channelID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (channel == null) return;

        TextChannel finalChannel = channel;
        event.getGuild().retrieveAuditLogs().queueAfter(1, TimeUnit.SECONDS, (logs) -> {
            boolean isBan = false, isKick = false;

            for (AuditLogEntry log : logs) {
                if (log.getTargetIdLong() == user.getIdLong()) {
                    isBan = log.getType() == ActionType.BAN;
                    isKick = log.getType() == ActionType.KICK;
                    break;
                }
            }

            if (isBan) {
                memberLeave.setTitle("A member has been banned!");
                finalChannel.sendMessageEmbeds(memberLeave.build()).queue();
            }
            else if (isKick) {
                memberLeave.setTitle("A member has been kicked!");
                finalChannel.sendMessageEmbeds(memberLeave.build()).queue();
            }
            else {
                finalChannel.sendMessageEmbeds(memberLeave.build()).queue();
            }

        });
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        if (event.getMessage().getContentRaw().toLowerCase().contains("joel")) {
            if (event.isFromType(ChannelType.TEXT)) {

                final String id = "205862976689799168";
                final User joel = event.getJDA().getUserById(id);

                if (event.getAuthor() == joel || event.getAuthor().isBot()) return;
                event.getGuild().retrieveMemberById(id).complete();

                final Member joelMember = event.getGuild().getMemberById(id);

                if (!event.getGuild().getMembers().contains(joelMember)) return;

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
                    if (message.getContentRaw().contains("joel")) {
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
                joel.openPrivateChannel()
                        .flatMap(channel -> channel.sendMessageEmbeds(builder.build()))
                        .queue();
            }
        }
    }
}
