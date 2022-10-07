package me.joel.commands.guild;

import me.joel.commands.music.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Join extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("join")) {

            final AudioManager audioManager = event.getGuild().getAudioManager();
            final Member bot = event.getGuild().retrieveMemberById("971239438892019743").complete();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            if (event.getMember().hasPermission(Permission.VOICE_MOVE_OTHERS) && memberChannel != null && bot.getVoiceState().inAudioChannel()) {

                if (bot.getVoiceState().getChannel() == memberChannel) {
                    EmbedBuilder builder1 = new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Already in " + memberChannel.getName() + "!");

                    event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                    return;
                }

                audioManager.openAudioConnection(memberChannel);

                EmbedBuilder builder1 = new EmbedBuilder()
                        .setColor(Color.green)
                        .setDescription("Joined " + memberChannel.getName() + "!");

                event.replyEmbeds(builder1.build()).setEphemeral(false).queue();
                return;
            }

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (bot.getVoiceState().getChannel() == memberChannel) {
                EmbedBuilder builder1 = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Already in " + memberChannel.getName() + "!");

                event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                return;
            }

            audioManager.openAudioConnection(memberChannel);

            EmbedBuilder builder1 = new EmbedBuilder()
                    .setColor(Color.green)
                    .setDescription("Joined " + memberChannel.getName() + "!");

            event.replyEmbeds(builder1.build()).setEphemeral(false).queue();
        }
    }
}
