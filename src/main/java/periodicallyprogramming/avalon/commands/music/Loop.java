package periodicallyprogramming.avalon.commands.music;

import periodicallyprogramming.avalon.lavaplayer.AudioEventAdapter;
import periodicallyprogramming.avalon.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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
                        .setColor(Color.red)
                        .setDescription("There is no song currently playing!");

                event.replyEmbeds(builder1.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder1;

            /* Checks for long overflow

            See Playing.nowPlaying() for full explanation
             */
            try {
                Math.addExact(PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack().getInfo().length, 1);
            }
            catch (ArithmeticException e) {
                EmbedBuilder builder2 = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("/loop is unavailable for YouTube streams");

                // disables loop
                AudioEventAdapter.setLoop(false);

                event.replyEmbeds(builder2.build()).setEphemeral(true).queue();
            }

            if (AudioEventAdapter.isShuffling()) {
                builder1 = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("You can't enable /shuffle and /queue at the same time!");
            }
            else if (!AudioEventAdapter.isLooping()) {
                AudioEventAdapter.setLoop(true);
                builder1 = new EmbedBuilder()
                        .setColor(Color.green)
                        .setDescription("Song is now looping!");
            } else {
                AudioEventAdapter.setLoop(false);
                builder1 = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Song is no longer looping!");
            }

            event.replyEmbeds(builder1.build()).queue();
        }
    }
}
