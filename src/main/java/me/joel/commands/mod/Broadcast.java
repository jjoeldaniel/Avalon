package me.joel.commands.mod;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Broadcast extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("broadcast")) {

            // Insufficient Permissions
            if (!(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && !event.getMember().getId().equals("205862976689799168")) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("You don't have permission for this command!")
                        .setColor(Color.red)
                        .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            // Get channel and message
            GuildChannelUnion channel = (event.getOption("channel")).getAsChannel();

            // Return if bot cant see channel
            if ( PermissionUtil.checkPermission(channel.getPermissionContainer(), event.getGuild().getSelfMember(), Permission.VIEW_CHANNEL)) {
                EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("I can't see that channel!")
                    .setColor(Color.red);

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            } else if ( PermissionUtil.checkPermission(channel.getPermissionContainer(), event.getGuild().getSelfMember(), Permission.MESSAGE_SEND)) {
                EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("I can't message in that channel!")
                    .setColor(Color.red);

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            String message = event.getOption("message").getAsString();

            // Embed
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Message sent!")
                    .setColor(Color.green)
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

            event.replyEmbeds(builder.build()).setEphemeral(true).queue(null, null);
        }

    }
}
