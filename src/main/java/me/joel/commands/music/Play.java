package me.joel.commands.music;

import me.joel.lavaplayer.PlayerManager;
import me.joel.lavaplayer.Spotify;
import net.dv8tion.jda.api.EmbedBuilder;
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

            // JDA AudioManager
            final AudioManager audioManager = event.getGuild().getAudioManager();

            EmbedBuilder builder;
            builder = Util.compareVoice(event.getMember());

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            event.deferReply().queue();

            // Check jda voice state and compare with member voice state
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

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
            if (isURL(link) && !link.contains("/track/")) {
                PlayerManager.getINSTANCE().loadAndPlay(event.getChannel(),link);
            }

            // Spotify tracks (can't get thumbnails otherwise)
            else if (link.contains("/track")) {
                link = Spotify.searchSpotify(link);
                PlayerManager.getINSTANCE().loadAndPlay(event.getChannel(),link);
            }

            // Invalid links
            else {
                link = ("ytsearch:" + link + " audio");
                // Plays song
                PlayerManager.getINSTANCE().loadAndPlay(event.getChannel(), link);
            }

            event.getHook().deleteOriginal().queueAfter(250, TimeUnit.MILLISECONDS);
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
