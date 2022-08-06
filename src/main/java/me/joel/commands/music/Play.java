package me.joel.commands.music;

import me.joel.PlayerManager;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static me.joel.MusicCommands.isURL;
import static me.joel.MusicCommands.returnChannel;

public class Play extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Command name
        var invoke = event.getName();

        // Avalon
        Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
        assert bot != null;

        // JDA AudioManager
        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        if (invoke.equals("play")) {
            event.deferReply().queue();

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                event.getHook().sendMessageEmbeds(Embed.VCRequirement.build()).setEphemeral(true).queue();
                return;
            }

            // Check jda voice state and compare with member voice state
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                assert memberChannel != null;
                long memberVC = memberChannel.getIdLong();
                long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                if (!(botVC == memberVC)) {
                    event.getHook().sendMessageEmbeds(Embed.sameVCRequirement.build()).setEphemeral(true).queue();
                    return;
                }
            }

            EmbedBuilder error = new EmbedBuilder()
                    .setDescription("Loading song(s)...")
                    .setColor(Util.randColor())
                    .setFooter("Use /help for a list of music commands!");
            event.getHook().sendMessageEmbeds(error.build()).setEphemeral(true).queue();

            // Joins VC
            audioManager.openAudioConnection(memberChannel);
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.setVolume(50);

            String link = Objects.requireNonNull(event.getOption("song")).getAsString();

            // Valid links
            if (isURL(link)) {
                PlayerManager.getINSTANCE().loadAndPlay(returnChannel(),link);
            }

            // Invalid links
            else {
                link = ("ytsearch:" + link + " audio");
                // Plays song
                PlayerManager.getINSTANCE().loadAndPlay(returnChannel(), link);
            }

            event.getHook().deleteOriginal().queueAfter(1000, TimeUnit.MILLISECONDS);
        }
    }
}
