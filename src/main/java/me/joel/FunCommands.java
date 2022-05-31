package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

public class FunCommands extends ListenerAdapter {

    String prefix = "paw";
    String userMessage = "";

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
                                    case 1 -> userMessage = "It is certain.";
                                    case 2 -> userMessage = ("It is decidedly so.");
                                    case 3 -> userMessage = ("Without a doubt.");
                                    case 4 -> userMessage = ("Yes definitely.");
                                    case 5 -> userMessage = ("You may rely on it.");
                                    case 6 -> userMessage = ("As I see it, yes.");
                                    case 7 -> userMessage = ("Outlook good.");
                                    case 8 -> userMessage = ("Yes.");
                                    case 9 -> userMessage = ("Signs point to yes.");
                                    case 10 -> userMessage = ("Reply hazy, try again.");
                                    case 11 -> userMessage = ("Ask again later.");
                                    case 12 -> userMessage = ("Better not tell you now.");
                                    case 13 -> userMessage = ("Cannot predict now.");
                                    case 14 -> userMessage = ("Concentrate and ask again.");
                                    case 15 -> userMessage = ("Don't count on it.");
                                    case 16 -> userMessage = ("My reply is no.");
                                    case 17 -> userMessage = ("My sources say no.");
                                    case 18 -> userMessage = ("Outlook not so good.");
                                    case 19 -> userMessage = ("Very doubtful.");
                                }
                            }
                        } catch (Exception exception) { userMessage = ("Try entering something.");}

                        EmbedBuilder builder = new EmbedBuilder()
                                .setTitle("8Ball")
                                .setColor(Color.PINK)
                                .setThumbnail("https://cdn.discordapp.com/attachments/810456406620241931/981063293428957244/unknown.png?size=4096")
                                .addField(userMessage, "", false);

                        event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
                    }

                }
                catch (Exception ignore) {}
            }
        }
    }
}