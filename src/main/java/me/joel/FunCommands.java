package me.joel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FunCommands extends ListenerAdapter {

    String prefix = "paw";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            // Grabs user input
            String messageSent = event.getMessage().getContentRaw();
            String[] botInput = messageSent.split(" ", 3);

            // Checks for valid command
            if (botInput[0].equalsIgnoreCase(prefix)) {

                try {

                    // Bark
                    if (botInput[1].equalsIgnoreCase("bark")) {
                        String bark = ("BARK BARK BARK GRRRRRRR GRRRRRRR GRRRRR GROWLS BARK BARK BARK WOOF WOOF GRRRR GRRRR RAWRRRRR BARK BARK BARK ARF ARF GRRRR RAH RAH RAH GRRRRRRRR SNARLS GROWLS BARK BARK BARK SNARLS GRRR GRRR GRRRRRR AWO AWO AWOOOOOOOOOOOO GRRRRR BARK BARK WOOF WOOF WOOF BARK BARK AWOOOOOO GRR GRR GRRRR");
                        event.getTextChannel().sendMessage(bark).queue();
                    }

                    // Meow
                    if (botInput[1].equalsIgnoreCase("meow")) {
                        String meow = ("MEOWW HISSSSSSSSSSSS PURRRRRRR MEOWWWW MEOOOOOOOOOOOOOWWWWWWWW FEED ME BITCH PURRRR MEOWWWWW HISSSSSSSSSSSS MEOWW MEOWWWW MEOOOOOOOOOOOOOWWWWWWWW PURR MEOW MEOW MEOW MEOW MEOW YOU FAT FUCK FEED ME MEOWWW");
                        event.getTextChannel().sendMessage(meow).queue();
                    }

                    // 8 Ball
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