package me.joel.commands.music;

import me.joel.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Volume extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("volume")) {

            // Avalon
            Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
            assert bot != null;

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                event.replyEmbeds(Util.VCRequirement.build()).setEphemeral(true).queue();
                return;
            }

            // Compare JDA and member voice state
            if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                long memberVC = Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong();
                long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                if (!(botVC == memberVC)) {
                    event.replyEmbeds(Util.sameVCRequirement.build()).setEphemeral(true).queue();
                    return;
                }
            }

            int num = Objects.requireNonNull(event.getOption("num")).getAsInt();

            if (num <= 0 || num > 100) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setTitle("Error! You can't set the volume to 0 or above 100.")
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            int prevVolume = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getVolume();
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.setVolume(num / 2);

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(me.joel.Util.randColor())
                    .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setTitle("Volume is now set to " + num + "%. (Prev: " + prevVolume * 2 + "%)")
                    .setFooter("Use /help for a list of music commands!");

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
