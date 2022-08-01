package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ModCommands extends ListenerAdapter {
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        try {
            // Reload Commands
            if (event.getName().equals("reload_commands")) {
                if (!event.isFromGuild()) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("This command only works in a server!")
                            .setColor(Util.randColor())
                            .setFooter("Use /help for the commands list");

                    event.replyEmbeds(builder.build()).queue();
                    return;
                }

                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER)) {
                    try {
                        Objects.requireNonNull(event.getGuild()).updateCommands().addCommands(

                                // General
                                Commands.slash("whois", "Provides user information")
                                        .addOption(OptionType.MENTIONABLE, "user", "Sends user info", true),
                                Commands.slash("afk", "Sets AFK status"),
                                Commands.slash("confess", "Sends anonymous confession")
                                        .addOption(OptionType.STRING, "message", "Confession message", true),

                                // Mod
                                Commands.slash("kick", "Kicks selected user")
                                        .addOption(OptionType.MENTIONABLE, "user", "Kicks selected user", true).addOption(OptionType.STRING, "reason", "Optional kick reason", false),
                                Commands.slash("ban", "Bans selected user")
                                        .addOption(OptionType.MENTIONABLE, "user", "Bans selected user", true).addOption(OptionType.STRING, "reason", "Optional ban reason", false),
                                Commands.slash("timeout", "Time-outs selected user")
                                        .addOption(OptionType.MENTIONABLE, "user", "Times out selected user", true).addOption(OptionType.INTEGER, "length", "Time in hours", false),
                                Commands.slash("broadcast", "Broadcasts message in selected channel")
                                        .addOption(OptionType.CHANNEL, "channel", "Channel message is broadcast in", true).addOption(OptionType.STRING, "message", "Broadcast message", true),
                                Commands.slash("purge", "Purges up to 100 messages")
                                        .addOption(OptionType.INTEGER, "number", "Number of messages to purge", true),

                                // Music
                                Commands.slash("play", "Requests a song")
                                        .addOption(OptionType.STRING, "song", "Accepts youtube links or song names", true),
                                Commands.slash("pause", "Pause playback"),
                                Commands.slash("volume", "Requests a song")
                                        .addOption(OptionType.STRING, "num", "Sets volume (between 1 and 100)", true),
                                Commands.slash("resume", "Resume playback"),
                                Commands.slash("clear", "Clears queue"),
                                Commands.slash("skip", "Skips song")
                                        .addOption(OptionType.INTEGER, "song_num", "Removes selected song from queue", false),
                                Commands.slash("queue", "Displays music queue"),
                                Commands.slash("playing", "Displays currently playing song"),
                                Commands.slash("loop", "Loops currently playing song")

                        ).queue();

                        EmbedBuilder builder = new EmbedBuilder()
                                .setTitle("Commands have been reloaded!")
                                .setColor(Util.randColor())
                                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();

                    } catch (Exception exception) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .addField("An error has occurred attempting to reload commands!", "If this persists, try re-adding the bot to the server!", false)
                                .setColor(Util.randColor())
                                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    }
                }

            }

            // Kick
            if (event.getName().equals("kick")) {
                if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                    EmbedBuilder noPerms = new EmbedBuilder()
                            .setDescription("You don't have permission for this command!")
                            .setColor(Util.randColor())
                            .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
                    event.replyEmbeds(noPerms.build()).queue();
                    return;
                }
                Member target = Objects.requireNonNull(event.getOption("user")).getAsMember();
                String reason = null;
                try {
                    assert target != null;
                    target.kick().queue();

                    if (target.isOwner() || target.hasPermission(Permission.ADMINISTRATOR)) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setTitle("You can't kick this person!")
                                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                                .setColor(Util.randColor())
                                .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }

                    // Check for reason
                    try {
                        reason = Objects.requireNonNull(event.getOption("reason")).getAsString();
                    } catch (Exception ignore) {
                    }

                    if (reason == null) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setTitle(target.getEffectiveName() + " has been kicked")
                                .setImage(target.getEffectiveAvatarUrl())
                                .setColor(Util.randColor());
                        event.replyEmbeds(builder.build()).queue();
                        return;
                    }

                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle(target.getEffectiveName() + " has been kicked")
                            .addField("Reason", reason, false)
                            .setImage(target.getEffectiveAvatarUrl())
                            .setColor(Util.randColor());
                    event.replyEmbeds(builder.build()).queue();
                } catch (Exception e) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("You can't kick this person!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Util.randColor())
                            .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }

            // Ban
            if (event.getName().equals("ban")) {
                if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS)) {
                    EmbedBuilder noPerms = new EmbedBuilder()
                            .setDescription("You don't have permission for this command!")
                            .setColor(Util.randColor())
                            .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
                    event.replyEmbeds(noPerms.build()).queue();
                    return;
                }
                Member target = Objects.requireNonNull(event.getOption("user")).getAsMember();
                String reason = null;
                try {
                    assert target != null;

                    if (target.isOwner() || target.hasPermission(Permission.ADMINISTRATOR)) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setTitle("You can't ban this person!")
                                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                                .setColor(Util.randColor())
                                .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }

                    // Check for reason
                    try {
                        reason = Objects.requireNonNull(event.getOption("reason")).getAsString();
                    } catch (Exception ignore) {
                    }

                    if (reason == null) {
                        target.ban(0).queue();
                        EmbedBuilder builder = new EmbedBuilder()
                                .setTitle(target.getEffectiveName() + " has been banned")
                                .setImage(target.getEffectiveAvatarUrl())
                                .setColor(Util.randColor());
                        event.replyEmbeds(builder.build()).queue();
                        return;
                    }

                    target.ban(0, reason).queue();
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle(target.getEffectiveName() + " has been banned")
                            .addField("Reason", reason, false)
                            .setImage(target.getEffectiveAvatarUrl())
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).queue();
                } catch (Exception e) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("You can't ban this person!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Util.randColor())
                            .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }

            // Timeout
            if (event.getName().equals("timeout")) {
                if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MODERATE_MEMBERS)) {
                    EmbedBuilder noPerms = new EmbedBuilder()
                            .setDescription("You don't have permission for this command!")
                            .setColor(Util.randColor())
                            .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
                    event.replyEmbeds(noPerms.build()).queue();
                    return;
                }
                Member target = Objects.requireNonNull(event.getOption("user")).getAsMember();
                long length = 0;
                assert target != null;

                if (target.isOwner() || target.hasPermission(Permission.ADMINISTRATOR)) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("You can't time out this person!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Util.randColor())
                            .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }

                try {
                    length = Objects.requireNonNull(event.getOption("length")).getAsLong();
                } catch (Exception ignore) {
                }

                try {
                    if (length == 0) {
                        target.timeoutFor(1, TimeUnit.HOURS).queue();
                    } else {
                        target.timeoutFor(length, TimeUnit.HOURS).queue();
                    }

                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle(target.getEffectiveName() + " has been timed out")
                            .setImage(target.getEffectiveAvatarUrl())
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).queue();
                } catch (Exception e) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("You can't time out this person!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Util.randColor())
                            .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }

            // Purge
            if (event.getName().equals("purge")) {
                try {
                    if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)) {
                        EmbedBuilder noPerms = new EmbedBuilder()
                                .setDescription("You don't have permission for this command!")
                                .setColor(Util.randColor())
                                .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
                        event.replyEmbeds(noPerms.build()).queue();
                        return;
                    }

                    int amount = Objects.requireNonNull(event.getOption("number")).getAsInt();

                    if (amount > 100) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("Unable to purge over 100 messages!")
                                .setFooter("Use /help for a list of commands!");

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                    if (event.getChannel().getType() == ChannelType.TEXT) {
                        TextChannel textChannel = event.getChannel().asTextChannel();
                        textChannel.getIterableHistory()
                                .takeAsync(amount)
                                .thenAccept(textChannel::purgeMessages);

                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("`" + amount + "` message(s) purged!")
                                .setFooter("Use /help for a list of commands!");

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    } else if (event.getChannel().getType() == ChannelType.VOICE) {
                        VoiceChannel voiceChannel = event.getChannel().asVoiceChannel();
                        voiceChannel.getIterableHistory()
                                .takeAsync(amount)
                                .thenAccept(voiceChannel::purgeMessages);

                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("`" + amount + "` message(s) purged!")
                                .setFooter("Use /help for a list of commands!");

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    }

                } catch (Exception except) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("Unable to purge messages!")
                            .setFooter("Use /help for a list of commands!");

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }

            // Broadcast
            if (event.getName().equals("broadcast")) {
                if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && !(event.getMember().getId().equals("205862976689799168"))) {
                    EmbedBuilder noPerms = new EmbedBuilder()
                            .setDescription("You don't have permission for this command!")
                            .setColor(Util.randColor())
                            .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
                    event.replyEmbeds(noPerms.build()).queue();
                    return;
                }

                GuildChannelUnion channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel();

                if (channel.getType() == ChannelType.VOICE) {
                    VoiceChannel voiceChannel = channel.asVoiceChannel();
                    String message = Objects.requireNonNull(event.getOption("message")).getAsString();

                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("Message sent!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Util.randColor())
                            .setDescription("\"" + message + "\"");

                    voiceChannel.sendMessage(message).queue();
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                } else {
                    TextChannel textChannel = channel.asTextChannel();
                    String message = Objects.requireNonNull(event.getOption("message")).getAsString();

                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("Message sent!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Util.randColor())
                            .setDescription("\"" + message + "\"");

                    textChannel.sendMessage(message).queue();
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }
        } catch (Exception e) {
            event.replyEmbeds(Util.genericError().build()).setEphemeral(true).queue();
        }

    }
}
