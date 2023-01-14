package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import me.joel.commands.mod.GuildSettings;

public class ReactMessages extends ListenerAdapter {

    final int REPLY_RATE = 25;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (event.isFromGuild()) {

            if (event.getChannel().getType() == ChannelType.TEXT) {
                if (event.getChannel().asTextChannel().isNSFW()) return;
            }

            // Grabs user input
            String messageSent =  event.getMessage().getContentRaw().toLowerCase();

            // Check for bot
            if (event.getMember() == null) return;
            if ((event.getMember()).getUser().isBot()) return;

            if (messageSent.equalsIgnoreCase( "<@" + event.getJDA().getSelfUser().getId() + ">" ) )
            {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor( Util.randColor() )
                        .setDescription( "Use /help for a list of my commands!" );
    
                event.getChannel().sendMessageEmbeds( builder.build() ).queue();
            }

            if (GuildSettings.gm_gn.get(event.getGuild())) {
                // Goodnight
                if (messageSent.contains("goodnight") || messageSent.contains("good night") || messageSent.equalsIgnoreCase("gn")) {
                    int r = Util.randomWithRange(0, 100);
                    if (r >= REPLY_RATE) return;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("goodnight sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
                // Good morning
                if (messageSent.contains("goodmorning") || messageSent.contains("good morning") || messageSent.equalsIgnoreCase("gm")) {
                    int r = Util.randomWithRange(0, 100);
                    if (r >= REPLY_RATE) return;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("good morning sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
            }

            // Insult
            if (GuildSettings.insults.get(event.getGuild())) {
                if (isInsult(messageSent)) {
                    int r = Util.randomWithRange(0, 100);
                    if (r >= REPLY_RATE) return;

                    String[] insults = {
                        "No you",
                        "Shut the fuck up, literally no one is paying attention",
                        "Fuck you",
                        "Your mom",
                        "Stfu",
                        "Fuck around and find out",
                        "Bruh",
                        "Dickhead",
                        "Asshole",
                        "Idiot",
                        "You can do better",
                        "Stfu inbred",
                        "Bitch pls",
                        "Shut your mouth",
                        "You disgust me",
                        "Fuck off",
                        "Dumbfuck",
                        "Dumbass",
                        "You're dumb",
                        "Fuck off midget",
                        "I'll fucking roundhouse kick you in the teeth, dumbfuck"};
                    
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

        String[] insults = {
            "fuck",
            "cunt",
            "slag",
            "prick",
            "slut",
            "asshole",
            "bastard",
            "twat",
            "bitch",
            "dick",
            "whore"
        };

        for ( var x : insults ) {
            if ( message.contains(x) ) return true;
        }
        return false;
    }
}
