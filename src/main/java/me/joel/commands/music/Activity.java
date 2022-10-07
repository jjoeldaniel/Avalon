package me.joel.commands.music;

import me.joel.lavaplayer.AudioEventAdapter;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Activity extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            member.deafen(true).queue();
        }

        Member bot = event.getGuild().getSelfMember();
        AudioChannel channel = event.getChannelJoined();

        // Wait 3 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {

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

            ((VoiceChannel) channel).sendMessageEmbeds(builder.build()).queue();
        };

        // if bot IS in VC and alone
        if (channel.getMembers().contains(bot) && PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).player.getPlayingTrack() == null) {
            executor.schedule(task, 3, TimeUnit.MINUTES);
            executor.shutdown();
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

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {

        Member bot = event.getGuild().getSelfMember();
        AudioChannel channel = event.getChannelLeft();

        // Wait 3 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {

            // If bot is NOT in VC or bot IS in VC and not alone
            if (!channel.getMembers().contains(bot) || channel.getMembers().size() > 1) return;

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

            ((VoiceChannel) channel).sendMessageEmbeds(builder.build()).queue();
        };

        // if bot IS in VC and alone
        if (channel.getMembers().contains(bot) && channel.getMembers().size() == 1) {
            executor.schedule(task, 3, TimeUnit.MINUTES);
            executor.shutdown();
        }
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        Member bot = event.getGuild().getSelfMember();

        // Get VC
        if (!bot.getVoiceState().inAudioChannel()) return;
        AudioChannel channel = bot.getVoiceState().getChannel();

        // Wait 3 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {

            // If bot is NOT in VC or bot IS in VC and not alone
            if (!channel.getMembers().contains(bot) || channel.getMembers().size() > 1) return;

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

            ((VoiceChannel) channel).sendMessageEmbeds(builder.build()).queue();
        };

        // If in VC and channel is empty aside from bot
        if (channel.getMembers().contains(bot) && channel.getMembers().size() == 1) {
            executor.schedule(task, 3, TimeUnit.MINUTES);
            executor.shutdown();
        }
    }
}
