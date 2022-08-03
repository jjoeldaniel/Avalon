package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModCommands extends ListenerAdapter {

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        try {

            var invoke = event.getName();

            switch (invoke) {
                case ("purge") -> {
                    // Insufficient Permissions
                    if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)) {
                        EmbedBuilder builder = noPermissions();
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }

                    // # of messages to be purged
                    int amount = Objects.requireNonNull(event.getOption("number")).getAsInt();

                    // Max of 100 messages
                    if (amount > 100) {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("Unable to purge over 100 messages!")
                                .setFooter("Use /help for a list of commands!");

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }

                    // Text Channel
                    if (event.getChannel().getType() == ChannelType.TEXT) {
                        TextChannel textChannel = event.getChannel().asTextChannel();
                        textChannel.getIterableHistory()
                                .takeAsync(amount)
                                .thenAccept(textChannel::purgeMessages);
                    }

                    // Voice Channel
                    else if (event.getChannel().getType() == ChannelType.VOICE) {
                        VoiceChannel voiceChannel = event.getChannel().asVoiceChannel();
                        voiceChannel.getIterableHistory()
                                .takeAsync(amount)
                                .thenAccept(voiceChannel::purgeMessages);
                    }

                    // Reply
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("`" + amount + "` message(s) purged!")
                            .setFooter("Use /help for a list of commands!");

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
                case ("broadcast") -> {
                    // Insufficient Permissions
                    if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && !(event.getMember().getId().equals("205862976689799168"))) {
                        EmbedBuilder builder = noPermissions();
                        event.replyEmbeds(builder.build()).queue();
                        return;
                    }

                    // Get channel and message
                    GuildChannelUnion channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel();
                    String message = Objects.requireNonNull(event.getOption("message")).getAsString();

                    // Embed
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("Message sent!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Util.randColor())
                            .setDescription("\"" + message + "\"");

                    // Text Channel
                    if (channel.getType() == ChannelType.VOICE) {
                        VoiceChannel voiceChannel = channel.asVoiceChannel();
                        voiceChannel.sendMessage(message).queue();
                    }
                    // Voice Channel
                    else if (channel.getType() == ChannelType.TEXT) {
                        TextChannel textChannel = channel.asTextChannel();
                        textChannel.sendMessage(message).queue();
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }

        } catch (Exception e) {
            event.replyEmbeds(Util.genericError().build()).setEphemeral(true).queue();
        }
    }

    /**
     *  Insufficient permissions embed
     */
    public static EmbedBuilder noPermissions() {
        return new EmbedBuilder()
                .setDescription("You don't have permission for this command!")
                .setColor(Util.randColor())
                .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
    }
}
