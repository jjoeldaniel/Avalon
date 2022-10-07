package me.joel.commands.music;

import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Activity extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member bot = event.getGuild().getSelfMember();
        if (!bot.getVoiceState().inAudioChannel()) return;

        // Wait 3 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        // Empty VC
        Runnable task1 = () -> {

            // If bot is NOT in VC or bot IS in VC and not alone
            if (!event.getChannelLeft().getMembers().contains(bot) || event.getChannelLeft().getMembers().size() > 1) return;

            // Clear queue
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.destroy();
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.queue.clear();

            // Disable shuffle/loop
            AudioEventAdapter.setLoop(false);
            AudioEventAdapter.setShuffle(false);

            // Send message
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.red)
                    .setTitle("Leaving inactive channel..")
                    .setDescription("Music queue is cleared");

            // 3 attempts at closing connection
            for (int i = 0; i < 3; i++) {
                if (!bot.getVoiceState().inAudioChannel()) break;
                event.getGuild().getAudioManager().closeAudioConnection();
            }

            event.getChannelLeft().asVoiceChannel().sendMessageEmbeds(builder.build()).queue();
        };

        // Timeout
        Runnable task2 = () -> {

            // If bot is playing
            if (PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack() != null) return;

            // Clear queue
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.destroy();
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.queue.clear();

            // Disable shuffle/loop
            AudioEventAdapter.setLoop(false);
            AudioEventAdapter.setShuffle(false);

            // Send message
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.red)
                    .setTitle("Leaving inactive channel..")
                    .setDescription("Music queue is cleared");

            // 3 attempts at closing connection
            for (int i = 0; i < 3; i++) {
                if (!bot.getVoiceState().inAudioChannel()) break;
                event.getGuild().getAudioManager().closeAudioConnection();
            }

            event.getChannelJoined().asVoiceChannel().sendMessageEmbeds(builder.build()).queue();
        };

        // If bot in VC and channel only contains bot
        if (event.getChannelLeft() != null && event.getChannelLeft().getMembers().contains(bot) && event.getChannelLeft().getMembers().size() == 1) {
            executor.schedule(task1, 3, TimeUnit.MINUTES);
            executor.shutdown();
        }
        // If bot IS in VC and not playing music
        else if (event.getChannelJoined().getMembers().contains(bot) && PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack() == null) {
            var queue = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.queue;

            // Gives extra time if queue is not empty
            if (queue.isEmpty()) {
                executor.schedule(task2, 3, TimeUnit.MINUTES);
                executor.shutdown();
            }
            else {
                executor.schedule(task2, 5, TimeUnit.MINUTES);
                executor.shutdown();
            }
        }
    }

    @Override
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            if (member.getVoiceState().inAudioChannel()) {
                member.deafen(true).queue();
            }
        }
    }
}
