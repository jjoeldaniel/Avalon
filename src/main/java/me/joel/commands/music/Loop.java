package me.joel.commands.music;

import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class Loop extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("loop")) {

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack() == null) {
                EmbedBuilder builder1 = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("There is no song currently playing!");

                event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder1;

            if (AudioEventAdapter.isShuffling()) {
                builder1 = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("You can't enable /shuffle and /queue at the same time!");
            }
            else if (!AudioEventAdapter.isLooping()) {
                AudioEventAdapter.setLoop(true);
                builder1 = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("Song is now looping!");
            } else {
                AudioEventAdapter.setLoop(false);
                builder1 = new EmbedBuilder()
                        .setColor(me.joel.Util.randColor())
                        .setDescription("Song is no longer looping!");
            }

            event.replyEmbeds(builder1.build()).queue();
        }
    }
}
