package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReactMessages extends ListenerAdapter {

    private static boolean isInsult(String message) {

        return message.contains("fuck") || (message.contains("cunt")) || (message.contains("prick") || (message.contains("slut")) || (message.contains("asshole")) || (message.contains("bastard")) || (message.contains("twat")) || (message.contains("bitch")) || (message.contains("dick")));
    }

    private static EmbedBuilder randomInsult () {
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

        return new EmbedBuilder()
                .setColor(Util.randColor())
                .setDescription(insultList.get(num));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild() && !event.getTextChannel().isNSFW() && (event.isFromType(ChannelType.TEXT))) {

            // Grabs user input
            String messageSent = event.getMessage().getContentRaw().toLowerCase();

            // Goodnight
            if (messageSent.contains("goodnight") || messageSent.contains("good night")) {
                if (Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("goodnight sweetie!");
                    event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
                }
            }
            // Good morning
            if (messageSent.contains("goodmorning") || messageSent.contains("good morning")) {
                if (Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("good morning sweetie!");
                    event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
                }
            }
            // Insult
            if (isInsult(messageSent)) {
                if (Util.randomWithRange(0, 100) > 50) event.getTextChannel().sendMessageEmbeds(randomInsult().build()).queue();
            }

        }
    }
}
