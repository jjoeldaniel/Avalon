package me.joel.commands.guild;

import me.joel.commands.music.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Leave extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("leave")) {

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            Member bot = event.getGuild().retrieveMemberById("971239438892019743").complete();

            if (!bot.getVoiceState().inAudioChannel()) {
                EmbedBuilder noVC = new EmbedBuilder()
                        .setDescription("The bot is not in a VC!")
                        .setColor(Color.red);

                event.replyEmbeds(noVC.build()).setEphemeral(true).queue();
            }

            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
            audioManager.closeAudioConnection();

            builder = new EmbedBuilder()
                .setColor(Color.green)
                .setDescription("Left " + memberChannel.getName() + "!");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
        }
    }
}
