package me.joel;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.joel.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GuildEvents extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            member.deafen(true).queue();
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot";

        EmbedBuilder builder = new EmbedBuilder()
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .setTitle("Thank you for inviting Avalon to " + event.getGuild().getName() + "!")
                .setColor(Util.randColor())
                .setDescription("Make sure to use /help to get the full commands list!")
                .addBlankField(false)
                .addField("Need to contact us?", "Add joel#0005 on Discord for questions!", false)
                .addField("Want to invite Avalon to another server?", "Click on my profile and click \" Add to Server\" to invite Avalon!", false);

        Objects.requireNonNull(event.getGuild().getSystemChannel()).sendMessageEmbeds(builder.build()).setActionRow(
                        Button.link(inviteLink, "Invite"))
                .queue();
    }

    @Override
    public void onGuildVoiceSelfMute(@NotNull GuildVoiceSelfMuteEvent event) {
        // Only takes Avalon as input
        if (!(event.getMember().getId().equals("971239438892019743"))) return;

        // Only if not muted
        if (Objects.requireNonNull(event.getMember().getVoiceState()).isSelfMuted()) return;

        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        AudioTrack track = PlayerManager.getINSTANCE().getMusicManager(audioManager.getGuild()).player.getPlayingTrack();

        if (track == null) return;

        // Time from ms to m:s
        long trackLength = track.getInfo().length;
        long minutes = (trackLength / 1000) / 60;
        long seconds = ((trackLength / 1000) % 60);

        long hours = 0;
        if (minutes >= 60) {
            while (minutes > 60) {
                hours++;
                minutes -= 60;
            }
        }

        String songHours = String.valueOf(hours);
        if (hours < 10) songHours = "0" + minutes;

        String songMinutes = String.valueOf(minutes);
        if (minutes < 10) songMinutes = "0" + minutes;

        String songSeconds = String.valueOf(seconds);
        if (seconds < 10) songSeconds = "0" + seconds;

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setAuthor("Now Playing")
                .setTitle(track.getInfo().title, track.getInfo().uri)
                .setDescription("`[0:00 / [" + songMinutes + ":" + songSeconds + "]`");

        if (hours > 0) {
            builder.setDescription("`[0:00 / [" + songHours + ":" + songMinutes + ":" + songSeconds + "]`");
        }

        if (track.getInfo().uri.contains("youtube.com")) {
            builder.setThumbnail(PlayerManager.getThumbnail(track.getInfo().uri));
        }

        VoiceChannel channel = event.getGuild().getVoiceChannelById(Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId());
        if (channel == null) return;

        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            if (Objects.requireNonNull(member.getVoiceState()).inAudioChannel()) {
                member.deafen(true).queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        // paw patrol and cat club welcome messages
        if (event.getGuild().getId().equals("645471751316307998") || event.getGuild().getId().equals("971225319153479790")) {

            Member member = event.getMember();
            EmbedBuilder memberJoin = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setTitle("A new member has joined!")
                    .setDescription
                            (
                                    "Welcome " + member.getAsMention() + " to " + event.getGuild().getName() +
                                    "! There are now " + event.getGuild().getMemberCount() + " members in " + event.getGuild().getName() + "."
                            )
                    .setThumbnail(member.getEffectiveAvatarUrl())
                    .setFooter("User: " + member.getUser().getName() +"#" + member.getUser().getDiscriminator() + " ID: " + member.getId());

            // find welcome channel
            try {

                int channelNum = Objects.requireNonNull(event.getGuild()).getTextChannels().size();
                for (int i = 0; i < channelNum; ++i) {

                    if (event.getGuild().getTextChannels().get(i).getName().contains("welcome")) {

                        event.getGuild().getTextChannels().get(i).sendMessageEmbeds(memberJoin.build()).queue();
                        return;
                    }
                }
            }
            // If no welcomeChannel found
            catch (Exception ignore) {}
        }
    }

}
