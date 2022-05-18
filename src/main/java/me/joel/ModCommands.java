package me.joel;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;

public class ModCommands extends ListenerAdapter {

    String prefix = "paw";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        try {

            String messageSent = event.getMessage().getContentRaw().toLowerCase();
            String[]botInput = messageSent.split(" ", 4);

            if (!event.getAuthor().isBot() && messageSent.startsWith(prefix) && event.isFromGuild()) {

                // Kick
                try {
                    if (botInput[1].equals("kick") && Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                        event.getGuildChannel().sendTyping().queue();
                        User target = event.getMessage().getMentionedUsers().get(0);
                        String targetName = target.getName();
                        String server = event.getGuild().getName();

                        event.getGuild().kick(target, botInput[3]).queue();
                        event.getChannel().sendMessage(targetName + " has been kicked.").queue();
                        System.out.println("[SUCCESS] " + targetName + " has been kicked with reason " + botInput[3] + " in  " + server);

                    }
                } catch (Exception ignore) {}

                // Owner broadcast
                try {
                    if (botInput[1].equals("broadcast") && event.getAuthor().getId().equals("205862976689799168")) {
                        String channelID = botInput[2];
                        String message = botInput[3];

                        Objects.requireNonNull(event.getGuild().getTextChannelById(channelID)).sendTyping().queue();
                        Objects.requireNonNull(event.getGuild().getTextChannelById(channelID)).sendMessage(message).queue();
                    }
                }
                catch (Exception ignore) {}

                // Ban
                try {
                    if (botInput[1].equals("ban") && Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS)) {
                        event.getGuildChannel().sendTyping().queue();
                        User target = event.getMessage().getMentionedUsers().get(0);
                        String targetName = target.getName();
                        String server = event.getGuild().getName();

                        event.getGuild().ban(target, 0, botInput[3]).queue();
                        event.getChannel().sendMessage(targetName + " has been banned.").queue();
                        System.out.println("[SUCCESS] " + targetName + " has been banned with reason '" + botInput[3] + "' in  " + server);

                    }
                }
                catch (Exception ignore) {}

                // Timeout (Default: 1 hr)
                try {
                    if (botInput[1].equals("timeout") && Objects.requireNonNull(event.getMember()).hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE, Permission.VOICE_MUTE_OTHERS, Permission.MODERATE_MEMBERS)) {
                        Member target = event.getMessage().getMentionedMembers().get(0);
                        String targetName = target.getEffectiveName();
                        String server = event.getGuild().getName();

                        // Checks if muted
                        if (!target.isTimedOut()) {
                            event.getGuildChannel().sendTyping().queue();
                            target.timeoutFor(Duration.ofHours(1)).queue();
                            System.out.println("[SUCCESS] " + target.getEffectiveName() + " is timed out by " + Objects.requireNonNull(event.getMember()).getEffectiveName() + " in  " + server);
                        } else if (target.isTimedOut()) {
                            event.getGuildChannel().sendTyping().queue();
                            target.removeTimeout().queue();
                            System.out.println("[SUCCESS] " + target.getEffectiveName() + " is no longer timed out in  " + server);
                        }
                    }
                }
                catch (Exception ignore) {}

            }
        }
        catch (Exception ignore) {}
    }
}
