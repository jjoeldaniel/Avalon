package me.joel.commands.mod;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Broadcast extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("broadcast")) {

            // Insufficient Permissions
            if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && !event.getMember().getId().equals("205862976689799168")) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("You don't have permission for this command!")
                        .setColor(Util.randColor())
                        .setFooter("Think this is an error?", "Try contacting your local server administrator/moderator!");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
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
}
