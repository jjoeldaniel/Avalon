package me.joel.commands.mod;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Purge extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("purge")) {

            // # of messages to be purged
            int amount = Objects.requireNonNull(event.getOption("number")).getAsInt();

            // If <= 0
            if (amount <= 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setDescription("`" + amount + "` is not a valid number!")
                        .setFooter("Use /help for a list of commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

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
    }
}
