package me.joel;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Mentions;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AFK extends ListenerAdapter {

    String prefix = "paw";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            try {
                // Grabs user input
                String messageSent = event.getMessage().getContentRaw();
                String[] botInput = messageSent.split(" ", 3);

                // Checks for valid command
                if (botInput[0].equalsIgnoreCase(prefix)) {
                        if (botInput[1].equalsIgnoreCase("afk")) {
                            try {
                                String userName = Objects.requireNonNull(event.getMember()).getEffectiveName();
                                event.getMember().modifyNickname("(AFK) " + userName).queue();
                                System.out.println(userName + " is now AFK");
                            } catch (Exception except) {
                                System.out.println("Can't rename owner/equal role!");
                            }
                        }
                }

                // Return from AFK
                if (Objects.requireNonNull(event.getMember()).getEffectiveName().startsWith("(AFK) ")) {
                    String user = event.getMember().getEffectiveName();
                    System.out.println(user + " returned from AFK");
                    String[] userName = user.split(" ", 2);
                    event.getMember().modifyNickname(userName[1]).queue();
                    event.getTextChannel().sendMessage("Welcome back, " + event.getMember().getAsMention() + "!").queue();
                }

                // Mentioning AFK users
                String userID = (event.getMessage().getMentions().getUsers().get(0).getId());
                Member member = event.getGuild().getMemberById(userID);
                assert member != null;
                if (member.getEffectiveName().contains("(AFK)")) {
                    event.getTextChannel().sendMessage("Mentioned member is AFK, " + Objects.requireNonNull(event.getMember()).getAsMention() + "!").queue();
                }

            }
            catch (Exception ignore) {}
        }
    }
}
