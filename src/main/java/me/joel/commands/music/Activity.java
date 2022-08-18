package me.joel.commands.music;

import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Activity extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {

        Member bot = event.getGuild().getSelfMember();
        AudioChannel channel = event.getChannelLeft();

        // On channel only containing bot
        if (channel.getMembers().contains(bot) && channel.getMembers().size() == 1) {

            // Wait 3 minutes
            // TODO: Add 3 minute delay

            // Clear queue
            if (PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack() != null) PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.destroy();
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.queue.clear();

            // Disable shuffle/loop
            AudioEventAdapter.setLoop(false);
            AudioEventAdapter.setShuffle(false);

            // Close connection
            event.getGuild().getAudioManager().closeAudioConnection();

            // Send message
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.red)
                    .setTitle("Leaving inactive channel..")
                    .setDescription("Music queue is cleared\nShuffle/Loop are disabled");

            ((VoiceChannel) channel).sendMessageEmbeds(builder.build()).queue();
        }
    }
}
