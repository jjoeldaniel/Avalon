package periodicallyprogramming.avalon;

import periodicallyprogramming.avalon.commands.mod.Toggle;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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
                if (messageSent.contains("goodnight") || messageSent.contains("good night") || messageSent.equalsIgnoreCase("gn")) {
                    int r = Util.randomWithRange(0, 100);
                    if (r >= 50) return;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("goodnight sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
                // Good morning
                if (messageSent.contains("goodmorning") || messageSent.contains("good morning") || messageSent.equalsIgnoreCase("gm")) {
                    int r = Util.randomWithRange(0, 100);
                    if (r >= 50) return;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("good morning sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
            }

            // Insult
            if (Toggle.insultsEnabled()) {
                if (isInsult(messageSent)) {
                    int r = Util.randomWithRange(0, 100);
                    if (r >= 25) return;

                    String[] insults = {"No you", "Minorly whore", "Shut the fuck up, literally no one is paying attention", "Fuck you", "Your mom", "Stfu", "Bruh", "Dickhead", "Asshole", "Idiot", "You can do better", "Stfu inbred", "Bitch pls", "Shut your mouth", "You disgust me", "Fuck off", "Dumbfuck", "Dumbass", "You're dumb", "Fuck off midget", "I'll fucking roundhouse kick you in the teeth, dumbfuck"};
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription(insults[Util.randomWithRange(0, insults.length-1)]);

                    event.getMessage().replyEmbeds(builder.build()).queue();
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
}
