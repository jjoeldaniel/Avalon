package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class GuildEvents extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Console.info("Active Bot: " + event.getJDA().getSelfUser().getName());
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            member.deafen(true).queue();
        }
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
    }

    @Override
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            if (member.getVoiceState().inAudioChannel()) {
                member.deafen(true).queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        // paw patrol and avalon server welcome messages
        if (event.getGuild().getId().equals("645471751316307998") || event.getGuild().getId().equals("971225319153479790")) {

            Member member = event.getMember();
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

            // find welcome channel
            TextChannel channel = event.getGuild().getTextChannelById(Util.findChannel("welcome", event.getGuild()));

            if (channel != null) {
                channel.sendMessageEmbeds(memberJoin.build()).queue();
            }
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        // paw patrol and avalon server welcome messages
        if (event.getGuild().getId().equals("645471751316307998") || event.getGuild().getId().equals("971225319153479790")) {

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

            // find welcome channel
            TextChannel channel = event.getGuild().getTextChannelById(Util.findChannel("welcome", event.getGuild()));

            if (channel != null) {

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
                        channel.sendMessageEmbeds(memberLeave.build()).queue();
                    }
                    else if (isKick) {
                        memberLeave.setTitle("A member has been kicked!");
                        channel.sendMessageEmbeds(memberLeave.build()).queue();
                    }
                    else {
                        channel.sendMessageEmbeds(memberLeave.build()).queue();
                    }

                });
            }
        }
    }
}
