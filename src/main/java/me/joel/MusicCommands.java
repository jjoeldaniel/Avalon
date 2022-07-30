package me.joel;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class MusicCommands extends ListenerAdapter
{

    static Member member;
    EmbedBuilder queue = new EmbedBuilder();
    List<AudioTrack> playlist;
    int queueSize;
    static boolean sendNowPlaying = false;
    static MessageChannelUnion messageChannelUnion;

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
    {
        if (!event.isFromGuild()) return;

        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        member = event.getMember();
        Member bot = event.getGuild().getMemberById("971239438892019743");
        assert bot != null;

        try
        {

            // Loop
            if (event.getName().equals("loop"))
            {
                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel())
                {
                    AudioEventAdapter.setLoop(true);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/loop`!")
                            .setFooter("Use /help for a list of music commands!");
                    event.getHook().sendMessageEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Compare JDA and member voice state
                if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel())
                {
                    long memberVC = Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong();
                    long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                    if (!(botVC == memberVC))
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in the same voice channel as the bot to use `/loop`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.getHook().sendMessageEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                }

                if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack() == null)
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("There is no song currently playing!");

                    event.replyEmbeds(builder.build()).queue();
                    return;
                }

                if (!AudioEventAdapter.isLooping())
                {
                    AudioEventAdapter.setLoop(true);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("Song is now looping!");

                    event.replyEmbeds(builder.build()).queue();
                }
                else
                {
                    AudioEventAdapter.setLoop(false);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("Song is no longer looping!");

                    event.replyEmbeds(builder.build()).queue();
                    return;
                }
            }

            // Play
            if (event.getName().equals("play"))
            {
                event.deferReply().queue();
                messageChannelUnion = event.getChannel();

                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel())
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/play`!")
                            .setFooter("Use /help for a list of music commands!");
                    event.getHook().sendMessageEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Check jda voice state and compare with member voice state
                if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel())
                {
                    long memberVC = Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong();
                    long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                    if (!(botVC == memberVC))
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in the same voice channel as the bot to use `/play`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.getHook().sendMessageEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                }

                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                String link = Objects.requireNonNull(event.getOption("song")).getAsString();

                // Spotify
                if (link.startsWith("https://open.spotify.com/"))
                {
                    link = ("ytsearch:" + Spotify.searchSpotify(link) + " audio");
                    // Joins VC
                    audioManager.openAudioConnection(memberChannel);

                    // Plays song
                    try
                    {
                        PlayerManager.getINSTANCE().loadAndPlayNoURI(messageChannelUnion, link);
                    }
                    catch (Exception e)
                    {
                        event.replyEmbeds(Util.genericError().build()).queue();
                        return;
                    }
                }

                // Invalid links
                else if (!isURL(link))
                {
                    link = ("ytsearch:" + link + " audio");
                    // Joins VC
                    audioManager.openAudioConnection(memberChannel);

                    // Plays song
                    try
                    {
                        PlayerManager.getINSTANCE().loadAndPlayNoURI(messageChannelUnion, link);
                    }
                    catch (Exception e)
                    {
                        event.replyEmbeds(Util.genericError().build()).queue();
                        return;
                    }
                }

                // Valid links (Basically just YouTube)
                else
                {
                    // Joins VC
                    audioManager.openAudioConnection(memberChannel);

                    // Plays song
                    try
                    {
                        PlayerManager.getINSTANCE().loadAndPlay(messageChannelUnion, link);
                    }
                    catch (Exception e)
                    {
                        event.replyEmbeds(Util.genericError().build()).queue();
                        return;
                    }
                }

                EmbedBuilder error = new EmbedBuilder()
                        .setDescription("Loading playlist...")
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");
                event.getHook().sendMessageEmbeds(error.build()).setEphemeral(true).queue();
                Util.wait(500);
                if (bot.getVoiceState().inAudioChannel())
                {
                    event.getGuild().deafen(bot, true).queue();
                }
                event.getHook().deleteOriginal().queue();
            }

            // Volume
            if (event.getName().equals("volume"))
            {
                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel())
                {
                    AudioEventAdapter.setLoop(true);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/loop`!")
                            .setFooter("Use /help for a list of music commands!");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Compare JDA and member voice state
                if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel())
                {
                    long memberVC = Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong();
                    long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                    if (!(botVC == memberVC))
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in the same voice channel as the bot to use `/loop`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                }

                int num = Objects.requireNonNull(event.getOption("num")).getAsInt();

                if (num <= 0 || num > 100)
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                            .setTitle("Error! You can't set the volume to 0 or above 100.")
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }
                int prevVolume = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getVolume();
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setVolume(num / 2);
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setTitle("Volume is now set to " + num + "%. (Prev: " + prevVolume*2 + "%)")
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).queue();
            }

            // Pause
            if (event.getName().equals("pause"))
            {
                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel())
                {
                    AudioEventAdapter.setLoop(true);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/pause`!")
                            .setFooter("Use /help for a list of music commands!");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Compare JDA and member voice state
                if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel())
                {
                    long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                    long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();
                    if (!(botVC == memberVC))
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in the same voice channel as the bot to use `/pause`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                }
                if (!PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.isPaused() && PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack() != null)
                {
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(true);

                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Playback paused")
                            .setColor(Util.randColor())
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("No song is playing or an error has occurred!")
                        .setColor(Util.randColor())
                        .setFooter("Use /help for a list of music commands!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }

            // Playing
            if (event.getName().equals("playing"))
            {

                try
                {
                    AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack();

                    // Time from ms to m:s
                    long trackLength = track.getInfo().length;
                    long minutes = (trackLength / 1000) / 60;
                    long seconds = ((trackLength / 1000) % 60);
                    String songSeconds = String.valueOf(seconds);
                    if (seconds < 10) songSeconds = "0" + seconds;

                    // Thumbnail
                    String trackThumbnail = PlayerManager.getThumbnail(track.getInfo().uri);

                    // Embed
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setAuthor("Now Playing")
                            .setTitle(track.getInfo().title, track.getInfo().uri)
                            .setDescription("`[0:00 / [" + minutes + ":" + songSeconds + "]`")
                            .setThumbnail(trackThumbnail)
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(builder.build()).queue();

                }
                catch (Exception exception)
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("No song is playing!")
                            .setFooter("Use /help for a list of music commands!");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }

            // Resume
            if (event.getName().equals("resume"))
            {
                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel())
                {
                    AudioEventAdapter.setLoop(true);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/resume`!")
                            .setFooter("Use /help for a list of music commands!");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Compare JDA and member voice state
                if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel())
                {
                    long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                    long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();
                    if (!(botVC == memberVC))
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in the same voice channel as the bot to use `/resume`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                }

                if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.isPaused() && PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack() != null)
                {
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.setPaused(false);

                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Playback resumed")
                            .setColor(Util.randColor())
                            .setFooter("Use /help for a list of music commands!");

                    event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("No song is playing or an error has occurred!")
                        .setFooter("Use /help for a list of music commands!")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }

            // Clear
            if (event.getName().equals("clear"))
            {
                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel())
                {
                    AudioEventAdapter.setLoop(true);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/clear`!")
                            .setFooter("Use /help for a list of music commands!");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Compare JDA and member voice state
                if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel())
                {
                    long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                    long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                    if (!(botVC == memberVC))
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in the same voice channel as the bot to use `/clear`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                }

                if (PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size() == 0)
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("The queue is empty or an error has occurred!")
                            .setFooter("Use /help for a list of music commands!")
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).queue();
                    return;
                }

                AudioEventAdapter.setLoop(false);
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.clear();
                PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.destroy();

                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Queue cleared")
                        .setFooter("Use /help for a list of music commands!")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).queue();
            }

            // Skip
            if (event.getName().equals("skip"))
            {
                // Checks requester voice state
                if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel())
                {
                    AudioEventAdapter.setLoop(true);
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("You need to be in a voice channel to use `/skip`!")
                            .setFooter("Use /help for a list of music commands!");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Compare JDA and member voice state
                if (Objects.requireNonNull(bot.getVoiceState()).inAudioChannel())
                {
                    long memberVC = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong();
                    long botVC = Objects.requireNonNull(bot.getVoiceState().getChannel()).getIdLong();

                    if (!(botVC == memberVC))
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Util.randColor())
                                .setDescription("You need to be in the same voice channel as the bot to use `/skip`!")
                                .setFooter("Use /help for a list of music commands!");
                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                }

                try
                {
                    AudioTrack audioTrack;
                    audioTrack = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack();

                    if (audioTrack == null)
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setDescription("No song is playing or an error has occurred!")
                                .setColor(Util.randColor())
                                .setFooter("Use /help for a list of music commands!");

                        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                        return;
                    }
                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Songs skipped")
                            .setFooter("Use /help for a list of music commands!")
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).queue();
                    AudioEventAdapter.setLoop(false);
                    PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.nextTrack();
                    return;
                }
                catch (Exception ignore) {}
            }

            // Queue
            if (event.getName().equals("queue"))
            {
                String currentSong;
                queueSize = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.size();

                if (queueSize == 0)
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("The queue is empty or an error has occurred!")
                            .setFooter("Use /help for a list of music commands!")
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }
                event.deferReply().queue();
                try
                {
                    currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
                }
                catch (Exception ignore) { return; }
                String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
                playlist = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).scheduler.queue.stream().toList();

                queue
                        .setAuthor(event.getGuild().getName(), event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                        .setTitle("Queue [" + queueSize + "]")
                        .setColor(Util.randColor())
                        .setThumbnail(Util.randomThumbnail())
                        .setFooter("Use /help for a list of music commands!");

                EmbedBuilder page1 = new EmbedBuilder();
                page1.copyFrom(queue);
                page1
                        .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                        .addBlankField(false);

                for (int i = 0; i < queueSize && i < 5; ++i)
                {
                    page1
                            .addField("[" + (i + 1) + "]", "[" + playlist.get(i).getInfo().title + "](" + playlist.get(i).getInfo().uri + ")\n", false)

                            .setFooter("Page 1");
                }
                // disable next page if next page is blank
                if (queueSize <= 5)
                {
                    event.getHook().sendMessageEmbeds(page1.build())
                            .addActionRow(
                                    Button.primary("page1", "Previous Page").asDisabled(),
                                    Button.primary("page2", "Next Page").asDisabled())
                            .queue();
                }
                else
                {
                    event.getHook().sendMessageEmbeds(page1.build())
                            .addActionRow(
                                    Button.primary("page1", "Previous Page").asDisabled(),
                                    Button.primary("page2", "Next Page"))
                            .queue();
                }
            }
        }
        catch (Exception e)
        {
            event.replyEmbeds(Util.genericError().build()).setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event)
    {
        if (!event.isFromGuild()) return;

        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        if (event.getComponentId().equals("page1"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            {
                return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page1 = new EmbedBuilder();
            page1.copyFrom(queue);
            page1
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 0; i < queueSize && i < 5; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page1
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 1");
            }

            // disable next page if next page is blank
            if (queueSize <= 5)
            {
                event.editMessageEmbeds(page1.build())
                        .setActionRow(
                                Button.primary("page1", "Previous Page").asDisabled(),
                                Button.primary("page2", "Next Page").asDisabled())
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page1.build())
                        .setActionRow(
                                Button.primary("page1", "Previous Page").asDisabled(),
                                Button.primary("page2", "Next Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page2"))
        {

            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            {
                return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page2 = new EmbedBuilder();
            page2.copyFrom(queue);
            page2
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 5; i < queueSize && i < 10; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page2
                    .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                    .setFooter("Page 2");
            }

            // disable next page if next page is blank
            if (queueSize <= 10)
            {
                event.editMessageEmbeds(page2.build())
                        .setActionRow(
                                Button.primary("page1", "Previous Page"),
                                Button.primary("page3", "Next Page").asDisabled())
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page2.build())
                        .setActionRow(
                                Button.primary("page1", "Previous Page"),
                                Button.primary("page3", "Next Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page3"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            { return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page3 = new EmbedBuilder();
            page3.copyFrom(queue);
            page3
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 10; i < queueSize && i < 15; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page3
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 3");
            }

            // disable next page if next page is blank
            if (queueSize <= 15)
            {
                event.editMessageEmbeds(page3.build())
                        .setActionRow(
                                Button.primary("page2", "Previous Page"),
                                Button.primary("page4", "Next Page").asDisabled(),
                                Button.success("page1", "First Page"))
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page3.build())
                        .setActionRow(
                                Button.primary("page2", "Previous Page"),
                                Button.primary("page4", "Next Page"),
                                Button.success("page1", "First Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page4"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            { return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page4 = new EmbedBuilder();
            page4.copyFrom(queue);
            page4
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 15; i < queueSize && i < 20; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page4
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 4");
            }

            // disable next page if next page is blank
            if (queueSize <= 20)
            {
                event.editMessageEmbeds(page4.build())
                        .setActionRow(
                                Button.primary("page3", "Previous Page"),
                                Button.primary("page5", "Next Page").asDisabled(),
                                Button.success("page1", "First Page"))
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page4.build())
                        .setActionRow(
                                Button.primary("page3", "Previous Page"),
                                Button.primary("page5", "Next Page"),
                                Button.success("page1", "First Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page5"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            { return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page5 = new EmbedBuilder();
            page5.copyFrom(queue);
            page5
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 20; i < queueSize && i < 25; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page5
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 5");
            }

            // disable next page if next page is blank
            if (queueSize <= 25)
            {
                event.editMessageEmbeds(page5.build())
                        .setActionRow(
                                Button.primary("page4", "Previous Page"),
                                Button.primary("page6", "Next Page").asDisabled(),
                                Button.success("page1", "First Page"))
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page5.build())
                        .setActionRow(
                                Button.primary("page4", "Previous Page"),
                                Button.primary("page6", "Next Page"),
                                Button.success("page1", "First Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page6"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            { return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page6 = new EmbedBuilder();
            page6.copyFrom(queue);
            page6
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 25; i < queueSize && i < 30; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page6
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 6");
            }

            // disable next page if next page is blank
            if (queueSize <= 30)
            {
                event.editMessageEmbeds(page6.build())
                        .setActionRow(
                                Button.primary("page5", "Previous Page"),
                                Button.primary("page7", "Next Page").asDisabled(),
                                Button.success("page1", "First Page"))
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page6.build())
                        .setActionRow(
                                Button.primary("page5", "Previous Page"),
                                Button.primary("page7", "Next Page"),
                                Button.success("page1", "First Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page7"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            { return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page7 = new EmbedBuilder();
            page7.copyFrom(queue);
            page7
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 30; i < queueSize && i < 35; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page7
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 7");
            }

            // disable next page if next page is blank
            if (queueSize <= 35)
            {
                event.editMessageEmbeds(page7.build())
                        .setActionRow(
                                Button.primary("page6", "Previous Page"),
                                Button.primary("page8", "Next Page").asDisabled(),
                                Button.success("page1", "First Page"))
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page7.build())
                        .setActionRow(
                                Button.primary("page6", "Previous Page"),
                                Button.primary("page8", "Next Page"),
                                Button.success("page1", "First Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page8"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            {
                return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page8 = new EmbedBuilder();
            page8.copyFrom(queue);
            page8
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 35; i < queueSize && i < 40; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page8
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 8");
            }

            // disable next page if next page is blank
            if (queueSize <= 40)
            {
                event.editMessageEmbeds(page8.build())
                        .setActionRow(
                                Button.primary("page7", "Previous Page"),
                                Button.primary("page9", "Next Page").asDisabled(),
                                Button.success("page1", "First Page"))
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page8.build())
                        .setActionRow(
                                Button.primary("page7", "Previous Page"),
                                Button.primary("page9", "Next Page"),
                                Button.success("page1", "First Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page9"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            { return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page9 = new EmbedBuilder();
            page9.copyFrom(queue);
            page9
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 40; i < queueSize && i < 45; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page9
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 9");
            }

            // disable next page if next page is blank
            if (queueSize <= 45)
            {
                event.editMessageEmbeds(page9.build())
                        .setActionRow(
                                Button.primary("page8", "Previous Page"),
                                Button.primary("page10", "Next Page").asDisabled(),
                                Button.success("page1", "First Page"))
                        .queue();
            }
            else
            {
                event.editMessageEmbeds(page9.build())
                        .setActionRow(
                                Button.primary("page8", "Previous Page"),
                                Button.primary("page10", "Next Page"),
                                Button.success("page1", "First Page"))
                        .queue();
            }
        }
        else if (event.getComponentId().equals("page10"))
        {
            String currentSong;
            try
            {
                currentSong = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
            }
            catch (Exception ignore)
            { return;
            }
            String currentURI = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;
            EmbedBuilder page10 = new EmbedBuilder();
            page10.copyFrom(queue);
            page10
                    .addField("Now playing", "[" + currentSong + "](" + currentURI + ")\n", false)
                    .addBlankField(false);

            for (int i = 45; i < queueSize && i < 50; ++i)
            {
                String songTile = playlist.get(i).getInfo().title;
                String songURI = playlist.get(i).getInfo().uri;
                page10
                        .addField("[" + (i+1) + "]", "[" + songTile + "](" + songURI + ")\n", false)
                        .setFooter("Page 10");
            }
            event.editMessageEmbeds(page10.build())
                    .setActionRow(
                            Button.primary("page9", "Previous Page"),
                            Button.success("page1", "First Page"))
                    .queue();
        }
    }

    public static void sendNowPlaying(AudioTrack currentTrack, MessageChannelUnion channel)
    {
        setSendNowPlaying(false);

        // Time from ms to m:s
        long trackLength = currentTrack.getInfo().length;
        long minutes = (trackLength / 1000) / 60;
        long seconds = ((trackLength / 1000) % 60);
        String songSeconds = String.valueOf(seconds);
        if (seconds < 10) songSeconds = "0" + seconds;
        // Thumbnail
        String trackThumbnail = PlayerManager.getThumbnail(currentTrack.getInfo().uri);

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setAuthor("Now Playing")
                .setTitle(currentTrack.getInfo().title, currentTrack.getInfo().uri)
                .setDescription("`[0:00 / [" + minutes + ":" + songSeconds + "]`")
                .setThumbnail(trackThumbnail)
                .setFooter("Use /help for a list of music commands!");

        channel.sendMessageEmbeds(builder.build()).queue();
    }

    public static MessageChannelUnion returnChannel() { return messageChannelUnion; }
    public static void setSendNowPlaying(boolean bool) { sendNowPlaying = bool; }


    // Validates links
    public boolean isURL(String url) {
        try
        {
            new URI(url);
            return true;
        }
        catch (URISyntaxException e)
        {
            return false;
        }
    }

}
