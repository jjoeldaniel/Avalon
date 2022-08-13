package me.joel.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.joel.Util;
import me.joel.commands.music.Play;
import me.joel.commands.music.Playing;
import me.joel.commands.music.Skip;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        nextTrack();
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        nextTrack();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        if (queue.size() == 0 || !Skip.sendNowPlaying()) return;

        EmbedBuilder builder = Playing.nowPlaying(track);
        Play.playing.get(player).sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {

            if (me.joel.lavaplayer.AudioEventAdapter.isLooping()) {
                AudioTrack loop = track.makeClone();
                this.player.startTrack(loop, false);
            }

            else if (me.joel.lavaplayer.AudioEventAdapter.isShuffling()) {
                List<AudioTrack> playlist = queue.stream().toList();

                if (playlist.size() < 1) {
                    nextTrack();
                    return;
                }

                AudioTrack randomTrack = playlist.get(Util.randomWithRange(0, playlist.size()));
                AudioTrack cloneTrack = playlist.get(Util.randomWithRange(0, playlist.size())).makeClone();

                this.player.startTrack(cloneTrack, false);
                queue.remove(randomTrack);
            }
            else {
                nextTrack();
            }
        }
    }
}
