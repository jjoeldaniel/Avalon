package me.joel;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter
{

    public final AudioPlayer audioPlayer;
    public final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer audioPlayer)
    {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track)
    {
        if (!this.audioPlayer.startTrack(track, true))
        {
            this.queue.offer(track);
        }
    }

    public void nextTrack()
    {
        this.audioPlayer.startTrack(this.queue.poll(), false);
        if (audioPlayer.getPlayingTrack() == null) return;
        MusicCommands.setSendNowPlaying(true);
        MusicCommands.sendNowPlaying(audioPlayer.getPlayingTrack(), MusicCommands.returnChannel());
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            if (me.joel.AudioEventAdapter.isLooping())
            {
                AudioTrack loop = track.makeClone();
                this.audioPlayer.startTrack(loop, false);
            } else
            {
                nextTrack();
            }
        }
    }

}
