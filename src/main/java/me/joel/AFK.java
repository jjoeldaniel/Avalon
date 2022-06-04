package me.joel;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AFK extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            try {

                Member member = event.getMessage().getMember();
                assert member != null;

                // Return from AFK
                if (member.getEffectiveName().startsWith("(AFK)")) {
                    System.out.println("AFK Member returned");
                    String user = Objects.requireNonNull(event.getMember()).getEffectiveName();
                    StringBuilder username = new StringBuilder()
                            .append(user)
                            .delete(0, 5);
                    member.modifyNickname(username.toString()).queue();
                    event.getTextChannel().sendMessage("Welcome back, " + event.getMember().getAsMention() + "!").queue();
                }

            }
            catch (Exception ignore) {}
        }
        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            try {
                Member member = event.getMessage().getMentions().getMembers().get(0);

                // Mentioning AFK users
                if (member.getEffectiveName().contains("(AFK)")) {
                    event.getTextChannel().sendMessage("Mentioned member is AFK, " + Objects.requireNonNull(event.getMember()).getAsMention() + "!").queue();
                }
            }
            catch (Exception ignore) {}
        }
    }
}
