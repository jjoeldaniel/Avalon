package me.joel.commands.music;

import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class Volume extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("volume")) {

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            int num = event.getOption("num").getAsInt();

            if (num <= 0 || num > 100) {
                builder = new EmbedBuilder()
                    .setColor(me.joel.Util.randColor())
                    .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setTitle("Error! You can't set the volume to 0 or above 100.")
                    .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            int prevVolume = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getVolume();
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.setVolume(num / 2);

            builder = new EmbedBuilder()
                .setColor(me.joel.Util.randColor())
                .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setTitle("Volume is now set to " + num + "%. (Prev: " + prevVolume * 2 + "%)")
                .setFooter("Use /help for a list of music commands!");

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
