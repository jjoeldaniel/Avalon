package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TestServer extends ListenerAdapter {

    // TODO: Fix onGuildMemberJoin, fails to trigger on member join
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        // paw patrol and cat club (test server)
        if (event.getGuild().getId().equals("645471751316307998") || event.getGuild().getId().equals("971225319153479790")) {
            System.out.println("new member: " + event.getMember().getUser().getName());
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
                    .setFooter("User: " + member.getUser().getName() + " ID: " + member.getId());

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
            catch (Exception ignore) {
            }
        }
    }
}
