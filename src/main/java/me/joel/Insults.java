package me.joel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Insults extends ListenerAdapter {

    // List
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

    // Check input
    private static boolean isInsult(String message) {

        return message.contains("fuck") || (message.contains("cunt")) || (message.contains("prick") || (message.contains("slut")) || (message.contains("asshole")) || (message.contains("bastard")) || (message.contains("twat")) || (message.contains("bitch")) || (message.contains("dick")));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && event.isFromGuild() && !event.getTextChannel().isNSFW()) {
            String message = (event.getMessage().getContentRaw()).toLowerCase();

            // Trigger words
            try {
                if (isInsult(message)) {
                    String insult = randomInsult();
                    int num = Util.randomWithRange(0, 100);
                    if (num > 50) event.getTextChannel().sendMessage(insult).queue();
                }
            }
            catch (Exception except) {
                System.out.println("Unknown error occurred during Insult");
            }
        }
    }
}
