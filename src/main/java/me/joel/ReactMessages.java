package me.joel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReactMessages extends ListenerAdapter {

    private static String kaeResponse() {

        ArrayList<String> kaeReply = new ArrayList<>();
        kaeReply.add("Fun fact: Did you know Kae is 4'11?");
        kaeReply.add("Kae's alright I suppose");
        kaeReply.add("Fun fact: Did you know Kae is 6'6?");
        kaeReply.add("Fun fact: Did you know Kae is 4'9?");
        kaeReply.add("Fun fact: Did you know Kae's height changes every second?");

        int num = Util.randomWithRange(0, kaeReply.size());

        return kaeReply.get(num);
    }

    private static String spectrumResponse() {

        ArrayList<String> spectrumResponse = new ArrayList<>();
        spectrumResponse.add("Fun fact: Did you know Spectrum servers are powered by 3 hamsters on a wheel?");
        spectrumResponse.add("Fun fact: Did you know Spectrum users love to wear wet socks to bed?");
        spectrumResponse.add("Fun fact: Did you know Spectrum users still live in their parents basement?");

        int num = Util.randomWithRange(0, spectrumResponse.size());

        return spectrumResponse.get(num);
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild() && !event.getTextChannel().isNSFW()) {

            // Grabs user input
            String messageSent = event.getMessage().getContentRaw().toLowerCase();

            // Kae
            if (messageSent.contains("kae ") || messageSent.contains("kae")) {
                int num = Util.randomWithRange(0, 100);
                if (num > 50) event.getTextChannel().sendMessage(kaeResponse()).queue();

            }
            // Goodnight
            if (messageSent.equalsIgnoreCase("goodnight")) event.getTextChannel().sendMessage("goodnight sweetie!").queue();
            // Spectrum
            if (messageSent.contains("spectrum")) event.getTextChannel().sendMessage(spectrumResponse()).queue();

        }
    }
}
