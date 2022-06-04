package me.joel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private static boolean isInsult(String message) {

        return message.contains("fuck") || (message.contains("cunt")) || (message.contains("prick") || (message.contains("slut")) || (message.contains("asshole")) || (message.contains("bastard")) || (message.contains("twat")) || (message.contains("bitch")) || (message.contains("dick")));
    }

    private static String randomInsult () {
        Random rand = new Random();

        List<String> insultList = new ArrayList<>();
        insultList.add("No you");
        insultList.add("Fuck you");
        insultList.add("Your mom");
        insultList.add("Stfu");
        insultList.add("Bruh");
        insultList.add("Dickhead");
        insultList.add("Asshole");
        insultList.add("Idiot");
        insultList.add("You can do better");
        insultList.add("Stfu inbred");
        insultList.add("Bitch pls");
        insultList.add("Shut your mouth");
        insultList.add("You disgust me");
        insultList.add("Fuck off");
        insultList.add("Dumbfuck");
        insultList.add("Dumbass");
        insultList.add("You're dumb");
        insultList.add("Fuck off midget");
        insultList.add("I'll fucking roundhouse kick you in the teeth, dumbfuck");
        insultList.add("Shut the fuck up, literally no one is paying attention");
        insultList.add("Minorly whore");

        int num = rand.nextInt(insultList.size());

        return insultList.get(num);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild() && !event.getTextChannel().isNSFW()) {

            // Grabs user input
            String messageSent = event.getMessage().getContentRaw().toLowerCase();

            // Kae
            if (messageSent.contains("kae")) {
                int num = Util.randomWithRange(0, 100);
                if (num > 50) event.getTextChannel().sendMessage(kaeResponse()).queue();

            }
            // Goodnight
            if (messageSent.contains("goodnight")) event.getTextChannel().sendMessage("goodnight sweetie!").queue();
            // Spectrum
            if (messageSent.contains("spectrum")) event.getTextChannel().sendMessage(spectrumResponse()).queue();
            // Insult
            if (isInsult(messageSent)) {
                String insult = randomInsult();
                int num = Util.randomWithRange(0, 100);
                if (num > 50) event.getTextChannel().sendMessage(insult).queue();
            }

        }
    }
}
