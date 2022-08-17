package me.joel;

import me.joel.commands.guild.Toggle;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReactMessages extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (event.isFromGuild()) {

            if (event.getChannel().getType() == ChannelType.TEXT) {
                if (event.getChannel().asTextChannel().isNSFW()) return;
            }

            // Grabs user input
            String messageSent = event.getMessage().getContentRaw().toLowerCase();

            // Check for bot
            if (event.getMember() == null) return;
            if ((event.getMember()).getUser().isBot()) return;

            if (Toggle.gmgnEnabled()) {
                // Goodnight
                if (messageSent.contains("goodnight") || messageSent.contains("good night") || messageSent.equalsIgnoreCase("gn") && Util.randomWithRange(0, 100) >= 75) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("goodnight sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
                // Good morning
                if (messageSent.contains("goodmorning") || messageSent.contains("good morning") || messageSent.equalsIgnoreCase("gm") && Util.randomWithRange(0, 100) >= 75) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("good morning sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
            }

            // Insult
            if (Toggle.insultsEnabled()) {
                if (isInsult(messageSent) && Util.randomWithRange(0, 100) >= 80) {
                    event.getMessage().replyEmbeds(randomInsult().build()).queue();
                }
            }
        }
    }

    /**
     * @return True if message contains insult
     */
    boolean isInsult(String message) {
        return message.contains("fuck") || (message.contains("cunt")) || (message.contains("slag")) || (message.contains("prick") || (message.contains("slut")) || (message.contains("asshole")) || (message.contains("bastard")) || (message.contains("twat")) || (message.contains("bitch")) || (message.contains("dick")));
    }

    /**
     * Insult embed
     * @return Embed
     */
    EmbedBuilder randomInsult() {
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
}
