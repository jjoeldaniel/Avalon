package me.joel.commands.music;

import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Play extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Command name
        var invoke = event.getName();

        if (invoke.equals("play")) {
            event.deferReply().queue();

            // Avalon
            Member bot = Objects.requireNonNull(event.getGuild()).getMemberById("971239438892019743");
            assert bot != null;

            // JDA AudioManager
            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            // Checks requester voice state
            if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                event.getHook().sendMessageEmbeds(Util.VCRequirement.build()).setEphemeral(true).queue();
                return;
            }

            // Check jda voice state and compare with member voice state
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel()) {
                assert memberChannel != null;
                long memberVC = memberChannel.getIdLong();
                long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                if (!(botVC == memberVC)) {
                    event.getHook().sendMessageEmbeds(Util.sameVCRequirement.build()).setEphemeral(true).queue();
                    return;
                }
            }

            EmbedBuilder error = new EmbedBuilder()
                    .setDescription("Loading song(s)...")
                    .setColor(me.joel.Util.randColor())
                    .setFooter("Use /help for a list of music commands!");
            event.getHook().sendMessageEmbeds(error.build()).setEphemeral(true).queue();

            // Joins VC
            audioManager.openAudioConnection(memberChannel);
            PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.setVolume(50);

            String link = Objects.requireNonNull(event.getOption("song")).getAsString();

            // Valid links
            if (isURL(link)) {
                PlayerManager.getINSTANCE().loadAndPlay(event.getChannel(),link);
            }

            // Invalid links
            else {
                link = ("ytsearch:" + link + " audio");
                // Plays song
                PlayerManager.getINSTANCE().loadAndPlay(event.getChannel(), link);
            }

            event.getHook().deleteOriginal().queueAfter(1000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * @return True if url is valid
     */
    public static boolean isURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
