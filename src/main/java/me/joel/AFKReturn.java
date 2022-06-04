package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AFKReturn extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            try {
                Member member = event.getMessage().getMentions().getMembers().get(0);

                // Mentioning AFK users
                if (member.getEffectiveName().contains("(AFK)")) {

                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Mentioned member is AFK, " + Objects.requireNonNull(event.getMember()).getAsMention() + "!")
                            .setColor(Util.randColor());

                    event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
                }
            }
            catch (Exception ignore) {}
        }
    }
}
