package me.joel.commands.guild;

import me.joel.commands.music.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Join extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("join")) {

            // Avalon
            Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
            assert bot != null;

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                event.replyEmbeds(Util.VCRequirement.build()).setEphemeral(true).queue();
                return;
            }

            // Compare JDA and member voice state
            if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                if (!(botVC == memberVC)) {
                    event.replyEmbeds(Util.sameVCRequirement.build()).setEphemeral(true).queue();
                    return;
                }
            }

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            if (bot.getVoiceState().getChannel() == memberChannel) {
                assert memberChannel != null;
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("Already in " + memberChannel.getName() + "!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }


            audioManager.openAudioConnection(memberChannel);

            assert memberChannel != null;
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(me.joel.Util.randColor())
                    .setDescription("Joined " + memberChannel.getName() + "!");

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }
    }
}
