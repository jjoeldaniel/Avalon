package me.joel.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import me.joel.commands.music.lavaplayer.PlayerManager;

import java.awt.*;

public class Resume extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Command name
        var invoke = event.getName();

        if (invoke.equals("resume")) {

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack() != null) {
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.setPaused(false);

                EmbedBuilder builder1 = new EmbedBuilder()
                        .setDescription("Playback resumed")
                        .setColor(Color.green)
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder1.build()).setEphemeral(false).queue();
                return;
            }

            EmbedBuilder builder1 = new EmbedBuilder()
                    .setDescription("No song is playing or an error has occurred!")
                    .setFooter("Use /help for a list of music commands!")
                    .setColor(Color.red);

            event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
        }
    }
}
