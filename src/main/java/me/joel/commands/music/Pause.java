package me.joel.commands.music;

import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Pause extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Command name
        var invoke = event.getName();

        if (invoke.equals("pause")) {

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            EmbedBuilder builder;
            builder = Util.compareVoice(Objects.requireNonNull(event.getMember()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack() != null) {
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.setPaused(true);

                EmbedBuilder builder1 = new EmbedBuilder()
                        .setDescription("Playback paused")
                        .setColor(me.joel.Util.randColor())
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder1.build()).setEphemeral(false).queue();
                return;
            }

            EmbedBuilder builder2 = new EmbedBuilder()
                    .setDescription("No song is playing or an error has occurred!")
                    .setColor(me.joel.Util.randColor())
                    .setFooter("Use /help for a list of music commands!");

            event.replyEmbeds(builder2.build()).setEphemeral(true).queue();
        }
    }
}
