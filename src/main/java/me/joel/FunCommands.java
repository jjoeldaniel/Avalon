package me.joel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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

                }
                catch (Exception ignore) {}
            }
        }
    }
}