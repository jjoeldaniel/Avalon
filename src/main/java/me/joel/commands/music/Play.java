package me.joel.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.joel.lavaplayer.GuildMusicManager;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Play extends ListenerAdapter {

    public static HashMap<AudioPlayer, MessageChannelUnion> playing = new HashMap<>();
    private static Member member;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Command name
        var invoke = event.getName();

        if (event.getGuild() == null) return;

        if (invoke.equals("play")) {
            member = event.getMember();

            // Managers
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

            EmbedBuilder builder = Util.compareVoice(event.getMember(), Util.getAvalon(event.getGuild()));

            if (builder != null) {
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            event.deferReply().queue();

            // Check jda voice state and compare with member voice state
            final VoiceChannel memberChannel = event.getMember().getVoiceState().getChannel().asVoiceChannel();

            EmbedBuilder error = new EmbedBuilder()
                    .setDescription("Loading song(s)...")
                    .setColor(me.joel.Util.randColor())
                    .setFooter("Use /help for a list of music commands!");
            event.getHook().sendMessageEmbeds(error.build()).setEphemeral(true).queue();

            // Joins VC
            audioManager.openAudioConnection(memberChannel);

            String trackURL = event.getOption("song").getAsString();

            // Invalid links
            if (!isURL(trackURL) || trackURL.contains("/track/")) {
                trackURL = ("ytsearch:" + trackURL + " audio");
            }

            PlayerManager.getINSTANCE().loadAndPlay(event.getChannel(),trackURL);

            // Store in Map
            AudioPlayer player = guildMusicManager.player;
            playing.put(player, event.getChannel());

            event.getHook().deleteOriginal().queueAfter(500, TimeUnit.MILLISECONDS);
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

    public static Member getMember() {
        return member;
    }

    public static HashMap<AudioPlayer, MessageChannelUnion> getPlaying() {
        return playing;
    }
}
