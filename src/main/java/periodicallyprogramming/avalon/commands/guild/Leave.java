package periodicallyprogramming.avalon.commands.guild;

import periodicallyprogramming.avalon.commands.music.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Leave extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("leave")) {
            final Member bot = event.getGuild().retrieveMemberById("971239438892019743").complete();

            if (event.getMember().hasPermission(Permission.VOICE_MOVE_OTHERS) && bot.getVoiceState().inAudioChannel()) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.green)
                        .setDescription("Left " + bot.getVoiceState().getChannel().getName() + "!");

                // 3 attempts at closing connection
                for (int i = 0; i < 3; i++) {
                    if (!bot.getVoiceState().inAudioChannel()) break;
                    event.getGuild().getAudioManager().closeAudioConnection();
                }

                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                return;
            }

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (!bot.getVoiceState().inAudioChannel()) {
                EmbedBuilder noVC = new EmbedBuilder()
                        .setDescription("The bot is not in a VC!")
                        .setColor(Color.red);

                event.replyEmbeds(noVC.build()).setEphemeral(true).queue();
            }

            final VoiceChannel memberChannel = event.getMember().getVoiceState().getChannel().asVoiceChannel();

            // 3 attempts at closing connection
            for (int i = 0; i < 3; i++) {
                if (!bot.getVoiceState().inAudioChannel()) break;
                event.getGuild().getAudioManager().closeAudioConnection();
            }

            builder = new EmbedBuilder()
                .setColor(Color.green)
                .setDescription("Left " + memberChannel.getName() + "!");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
        }
    }
}
