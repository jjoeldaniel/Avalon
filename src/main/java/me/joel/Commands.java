package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.AudioManager;

public class Commands extends ListenerAdapter {

    String prefix = "paw";
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
                        event.getMessage().delete().queue();
                        // DMs user
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Color.PINK)
                                .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot)")
                                .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/e1e13fc10a86846545c1aa02ec102e40.png?size=4096")
                                .addField("General Commands", """
                                        `paw ping` : Pings bot
                                        `paw truth` : Requests truth
                                        `paw dare` : Requests dare
                                        `paw afk` : Sets AFK status
                                        `paw av (input)` : Retrieves author (or target) profile picture
                                        `paw 8ball (input)` : Asks the magic 8ball a question
                                        `paw play (YT Link)` : Plays music""", false)
                                .addField("Moderation Commands", """
                                        `paw kick (user) (reason)` : Kicks user with required reason\s
                                        `paw ban (user) (reason)` : Bans user with required reason
                                        `paw timeout (user)` : Times out user (Default: 1hr)
                                        `paw broadcast (channelID) (text)` : Sends message as PawBot""", false);

                        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(builder.build())).queue();
                    }

                    // Music
                    if (botInput[1].equalsIgnoreCase("play")) {

                        // Checks requester voice state
                        if (!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) {
                            event.getTextChannel().sendMessage("You need to be in a voice channel to use `paw play`").queue();
                            return;
                        }

                        final AudioManager audioManager = event.getGuild().getAudioManager();
                        final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                        String link = String.join(" ", botInput[2]);

                        // FIXME: Searches if invalid link
                        if (!isURL(link)) {
                            link = "ytsearch:" + link + " audio";
                        }

                        // Joins VC
                        audioManager.openAudioConnection(memberChannel);
                        Member bot = event.getMember().getGuild().getMemberById("971239438892019743");
                        assert bot != null;
                        event.getGuild().deafen(bot, true).queue();

                        // Plays song
                        PlayerManager.getINSTANCE().loadAndPlay(event.getTextChannel(), link);
                    }

                    // Ping
                    if (botInput[1].equalsIgnoreCase("ping")) {
                        String user = event.getAuthor().getName();
                        String server = event.getGuild().getName();
                        EmbedBuilder builder = new EmbedBuilder()
                                .setTitle("Pong!")
                                .setColor(Color.PINK);

                        event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
                    }

                    // Gets user profile picture

                    if (botInput[1].equalsIgnoreCase("av")) {
                        try {
                            if (!botInput[2].isEmpty()) {
                                Member target = event.getMessage().getMentionedMembers().get(0);
                                String targetName = target.getEffectiveName();
                                String targetPFP = target.getEffectiveAvatarUrl();
                                EmbedBuilder builder = new EmbedBuilder()
                                        .setTitle(targetName)
                                        .setImage(targetPFP)
                                        .setColor(Color.PINK);
                                event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
                                return;
                            }
                        }
                        catch (Exception ignored) {}
                    } // Target
                    if (botInput[1].equalsIgnoreCase("av")) {
                        try {
                            String targetPFP = Objects.requireNonNull(event.getMember()).getEffectiveAvatarUrl();
                            Member target = event.getMember();
                            String targetName = target.getEffectiveName();
                            EmbedBuilder builder = new EmbedBuilder()
                                    .setTitle(targetName)
                                    .setImage(targetPFP)
                                    .setColor(Color.PINK);
                            event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
                        }
                        catch (Exception ignored) {}
                    } // Author

                }

                catch (Exception ignore) {}
            }

        }
    }

    // Validates link
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
