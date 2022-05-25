package me.joel;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Random;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.AudioManager;

public class Commands extends ListenerAdapter {

    String prefix = "paw";

    static String printCommands() {
        return "General Commands\n\n`paw ping` : Pings bot\n`paw truth` : Requests truth\n`paw dare` : Requests dare\n`paw afk` : Sets AFK status\n`paw av (input)` : Retrieves author (or target) profile picture\n`paw 8ball (input)` : Asks the magic 8ball a question\n\nModeration Commands\n\n`paw kick (user) (reason)` : Kicks user with required reason \n`paw ban (user) (reason)` : Bans user with required reason\n`paw timeout (user)` : Times out user (Default: 1hr)\n`paw broadcast (channelID) (text)` : Sends message as PawBot";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // Checks if user
        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            // Grabs user input
            String messageSent = event.getMessage().getContentRaw();
            String[]botInput = messageSent.split(" ", 3);

            // Checks for valid command
            if (botInput[0].equalsIgnoreCase(prefix)) {

                try {

                    // Commands list
                    if (botInput[1].equalsIgnoreCase("help")) {
                        User user = event.getAuthor();
                        // DM's user
                        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(printCommands())).queue();
                    }

                    // Music
                    if (botInput[1].equalsIgnoreCase("play")) {
                        if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                            event.getTextChannel().sendMessage("You need to be in a voice channel to use `paw play`");
                            return;
                        }

                        final AudioManager audioManager = event.getGuild().getAudioManager();
                        final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                        audioManager.openAudioConnection(memberChannel);

                        String link = String.join(" ", botInput[2]);

                        if (!isURL(link)) {
                            link = "ytsearch:" + link + " audio";
                        }

                        PlayerManager.getINSTANCE().loadAndPlay(event.getTextChannel(), link);

                    }

                    // Ping
                    if (botInput[1].equalsIgnoreCase("ping")) {
                        String user = event.getAuthor().getName();
                        String server = event.getGuild().getName();
                        event.getTextChannel().sendMessage("Pong!").queue();
                    }

                    // Gets user profile picture

                    if (botInput[1].equalsIgnoreCase("av")) {
                        try {
                            if (!botInput[2].isEmpty()) {
                                //User target = event.getMessage().getMentionedUsers().get(0);
                                Member target = event.getMessage().getMentionedMembers().get(0);
                                String targetPFP = target.getEffectiveAvatarUrl();
                                event.getTextChannel().sendMessage(targetPFP).queue();
                                return;
                            }
                        }
                        catch (Exception ignored) {}
                    } // Target
                    if (botInput[1].equalsIgnoreCase("av")) {
                        try {
                            String userPFP = Objects.requireNonNull(event.getMember()).getEffectiveAvatarUrl();
                            //String userPFP = event.getAuthor().getEffectiveAvatarUrl();
                            event.getTextChannel().sendMessage(userPFP).queue();
                        }
                        catch (Exception ignored) {}
                    } // Author

                }

                catch (Exception ignore) {}
            }

        }
    }

    public boolean isURL(String url) {
        try {
            new URI(url);
            return true;
        }
        catch (URISyntaxException e) {
            return false;
        }
    }
}
