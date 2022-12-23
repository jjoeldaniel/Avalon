package periodicallyprogramming.avalon.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import periodicallyprogramming.avalon.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Seek extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("seek")) {

            EmbedBuilder check = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (check != null) {
                event.replyEmbeds(check.build()).setEphemeral(true).queue();
                return;
            }

            AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack();

            if (track == null) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("No song is playing!")
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            /* Checks for long overflow

            See Playing.nowPlaying() for full explanation
             */
            try {
                Math.addExact(track.getInfo().length, 1);
            }
            catch (ArithmeticException e) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("/seek is unavailable for YouTube streams");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }

            long seconds = 0;
            long minutes = 0;
            long hours = 0;

            if (event.getOption("seconds") != null) seconds = event.getOption("seconds").getAsLong() * 1000;
            if (event.getOption("minutes") != null) minutes = event.getOption("minutes").getAsLong() * 60000;
            if (event.getOption("hours") != null) hours = event.getOption("hours").getAsLong() * 3600000;
            long seek = seconds + minutes + hours;

            if (seek >= track.getDuration() || seek == 0) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("That isn't a valid number!")
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            track.setPosition(seek);

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.green)
                    .setTitle("Seeking at");

            builder.appendDescription("\n`SECONDS:" + seconds/1000 + "`");
            builder.appendDescription("\n`MINUTES:" + minutes/60000 + "`");
            builder.appendDescription("\n`HOURS:" + hours/3600000 + "`");


            event.replyEmbeds(builder.build()).queue();
        }
    }
}
