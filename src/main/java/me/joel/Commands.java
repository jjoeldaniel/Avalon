package me.joel;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Random;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;

public class Commands extends ListenerAdapter {

    String prefix = "paw";

    static String printCommands() {
        return "General Commands\n\n`paw ping` : Pings bot\n`paw truth` : Requests truth\n`paw dare` : Requests dare\n`paw afk` : Sets AFK status\n`paw av (input)` : Retrieves author (or target) profile picture\n`paw 8ball (input)` : Asks the magic 8ball a question\n\nModeration Commands\n\n`paw kick (user) (reason)` : Kicks user with required reason \n`paw ban (user) (reason)` : Bans user with required reason\n`paw timeout (user)` : Times out user (Default: 1hr)";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            // Grabs user input
            String messageSent = event.getMessage().getContentRaw();
            String[]botInput = messageSent.split(" ", 3);

            // Checks for valid command
            if (botInput[0].equalsIgnoreCase(prefix)) {

                try {

                    // Commands list
                    if (botInput[1].equalsIgnoreCase("help")) {
                        User user = event.getAuthor();
                        // DM's user
                        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(printCommands())).queue();
                    }

                    // Ping
                    if (botInput[1].equalsIgnoreCase("ping")) {
                        String user = event.getAuthor().getName();
                        String server = event.getGuild().getName();
                        event.getTextChannel().sendMessage("Pong!").queue();
                    }

                    // Gets user profile picture

                    if (botInput[1].equalsIgnoreCase("av")) {
                        try {
                            if (!botInput[2].isEmpty()) {
                                User target = event.getMessage().getMentionedUsers().get(0);
                                String targetPFP = target.getEffectiveAvatarUrl();
                                event.getTextChannel().sendMessage(targetPFP).queue();
                                return;
                            }
                        }
                        catch (Exception ignored) {}
                    } // Target
                    if (botInput[1].equalsIgnoreCase("av")) {
                        try {
                            String userPFP = event.getAuthor().getEffectiveAvatarUrl();
                            event.getTextChannel().sendMessage(userPFP).queue();
                            return;
                        }
                        catch (Exception ignored) {}
                    } // Author

                    // 8Ball
                    if (botInput[1].equalsIgnoreCase("8ball")) {
                        Random rand = new Random();
                        int randomResult = Util.randomWithRange(1, 19);

                        try {
                            if (!botInput[2].equalsIgnoreCase("")) {
                                switch (randomResult) {
                                    case 1 -> event.getTextChannel().sendMessage("It is certain.").queue();
                                    case 2 -> event.getTextChannel().sendMessage("It is decidedly so.").queue();
                                    case 3 -> event.getTextChannel().sendMessage("Without a doubt.").queue();
                                    case 4 -> event.getTextChannel().sendMessage("Yes definitely.").queue();
                                    case 5 -> event.getTextChannel().sendMessage("You may rely on it.").queue();
                                    case 6 -> event.getTextChannel().sendMessage("As I see it, yes.").queue();
                                    case 7 -> event.getTextChannel().sendMessage("Outlook good.").queue();
                                    case 8 -> event.getTextChannel().sendMessage("Yes.").queue();
                                    case 9 -> event.getTextChannel().sendMessage("Signs point to yes.").queue();
                                    case 10 -> event.getTextChannel().sendMessage("Reply hazy, try again.").queue();
                                    case 11 -> event.getTextChannel().sendMessage("Ask again later.").queue();
                                    case 12 -> event.getTextChannel().sendMessage("Better not tell you now.").queue();
                                    case 13 -> event.getTextChannel().sendMessage("Cannot predict now.").queue();
                                    case 14 -> event.getTextChannel().sendMessage("Concentrate and ask again.").queue();
                                    case 15 -> event.getTextChannel().sendMessage("Don't count on it.").queue();
                                    case 16 -> event.getTextChannel().sendMessage("My reply is no.").queue();
                                    case 17 -> event.getTextChannel().sendMessage("My sources say no.").queue();
                                    case 18 -> event.getTextChannel().sendMessage("Outlook not so good.").queue();
                                    case 19 -> event.getTextChannel().sendMessage("Very doubtful.").queue();
                                }
                            }
                        } catch (Exception exception) {
                            event.getTextChannel().sendMessage("Try entering something.").queue();
                        }
                    }

                }

                catch (Exception ignore) {}
            }

        }
    }

}
