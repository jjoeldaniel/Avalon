package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {

    String prefix = "paw";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Help Command
        if (event.getName().equals("help")) {
            event.getInteraction().getUser();
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.PINK)
                    .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot)")
                    .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/e1e13fc10a86846545c1aa02ec102e40.png?size=4096")
                    .addField("General Commands", """
                                        `/help` : Lists commands
                                        `/ping` : Pings bot
                                        `/truth` : Requests truth
                                        `/dare` : Requests dare
                                        `paw afk` : Sets AFK status
                                        `paw av (input)` : Retrieves author (or target) profile picture
                                        `/8ball` : Asks the magic 8ball a question
                                        `/bark` : Self explanatory
                                        `/meow` : ^^^
                                        `paw play (YT Link)` : Plays music""", false)
                    .addField("Moderation Commands", """
                                        `paw kick (user) (reason)` : Kicks user with required reason\s
                                        `paw ban (user) (reason)` : Bans user with required reason
                                        `paw timeout (user)` : Times out user (Default: 1hr)
                                        `paw broadcast (channelID) (text)` : Sends message as PawBot""", false);
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // Ping Command
        if (event.getName().equals("ping")) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Pong!")
                    .setColor(Color.PINK);
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // 8Ball Command
        if (event.getName().equals("8ball")) {
            int randomResult = Util.randomWithRange(1, 19);
            String output = "null";
            switch (randomResult) {
                case 1 -> output = "It is certain.";
                case 2 -> output = ("It is decidedly so.");
                case 3 -> output = ("Without a doubt.");
                case 4 -> output = ("Yes definitely.");
                case 5 -> output = ("You may rely on it.");
                case 6 -> output = ("As I see it, yes.");
                case 7 -> output = ("Outlook good.");
                case 8 -> output = ("Yes.");
                case 9 -> output = ("Signs point to yes.");
                case 10 -> output = ("Reply hazy, try again.");
                case 11 -> output = ("Ask again later.");
                case 12 -> output = ("Better not tell you now.");
                case 13 -> output = ("Cannot predict now.");
                case 14 -> output = ("Concentrate and ask again.");
                case 15 -> output = ("Don't count on it.");
                case 16 -> output = ("My reply is no.");
                case 17 -> output = ("My sources say no.");
                case 18 -> output = ("Outlook not so good.");
                case 19 -> output = ("Very doubtful.");
            }
            String question = Objects.requireNonNull(event.getOption("question")).getAsString();

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("8Ball")
                    .setColor(Color.PINK)
                    .setThumbnail("https://cdn.discordapp.com/attachments/810456406620241931/981063293428957244/unknown.png?size=4096")
                    .addField("Your question:", question, false)
                    .addField(output, "", false);

            event.replyEmbeds(builder.build()).queue();
        }

        // Truth Command
        if (event.getName().equals("truth")) {
            int num = Util.randomWithRange(0, 100);
            String[] truth = new String[101];

            truth[0] = "What’s the last lie you told?";
            truth[1] = "What was the most embarrassing thing you’ve ever done on a date?";
            truth[2] = "Have you ever accidentally hit something (or someone!) with your car?";
            truth[3] = "Name someone you’ve pretended to like but actually couldn't stand.";
            truth[4] = "What’s been your most physically painful experience?";
            truth[5] = "What bridges are you glad that you burned?";
            truth[6] = "If you met a genie, what would your three wishes be?";
            truth[7] = "What’s the meanest thing you’ve ever said to someone else?";
            truth[8] = "Who was your worst kiss ever?";
            truth[9] = "What’s one thing you’d do if you knew there no consequences?";
            truth[10] = "What’s the meanest thing you’ve ever said about someone else?";
            truth[11] = "Who are you most jealous of?";
            truth[12] = "Would you date your high school crush today?";
            truth[13] = "Do you believe in any superstitions? If so, which ones?";
            truth[14] = "Have you ever considered cheating on a partner?";
            truth[15] = "Have you ever cheated on a partner?";
            truth[16] = "What app do you waste the most time on?";
            truth[17] = "What is the youngest age partner you’d date?";
            truth[18] = "Have you ever lied about your age?";
            truth[19] = "Have you ever used a fake ID?";
            truth[20] = "What's your favorite food?";
            truth[21] = "What is your guilty pleasure?";
            truth[22] = "What is your biggest pet peeve?";
            truth[23] = "What was your most embarrassing moment in public in front of everyone?";
            truth[24] = "Is there anything in your life that you would change?";
            truth[25] = "Do you currently have a crush on anyone?";
            truth[26] = "Do you What’s the most childish thing you still do? have a crush on anyone?";
            truth[27] = "What’s the most childish thing you still do?";
            truth[28] = "When’s the last time you dumped someone?";
            truth[29] = "Have you ever lied for a friend?";
            truth[30] = "Would you date someone shorter than you?";
            truth[31] = "If you could become invisible, what’s the worst thing you’d do?";
            truth[32] = "After you’ve dropped a piece of food, what’s the longest time you’ve left it on the floor before eating it?";
            truth[33] = "What’s something that overwhelms you?";
            truth[34] = "What was the greatest day of your life?";
            truth[35] = "How many people have you kissed?";
            truth[36] = "What’s your most absurd deal-breaker?";
            truth[37] = "What’s the scariest thing that’s ever happened to you?";
            truth[38] = "What’s the last purchase you regretted?";
            truth[39] = "Who was your first love?";
            truth[40] = "How often do you wash your sheets?";
            truth[41] = "Have you ever pretended to not get a text to get out of doing something?";
            truth[42] = "What’s your most embarrassing childhood memory?";
            truth[43] = "Have you ever been in a fight?";
            truth[44] = "What’s the worst advice you’ve ever given someone else?";
            truth[45] = "What’s the worst advice someone else has ever given you?";
            truth[46] = "Is there an ex with whom you’d consider reconciling?";
            truth[47] = "What’s the weirdest thing you do while driving?";
            truth[48] = "When’s the last time you wanted to hit somebody?";
            truth[49] = "Who’s the last person who called you?";
            truth[50] = "When was the last time you were really angry? Why?";
            truth[51] = " What’s your favorite guilty pleasure song?";
            truth[52] = "Would you ever get plastic surgery?";
            truth[53] = "Have you ever intentionally sabotaged a friend?";
            truth[54] = "What’s a skill you wish you had?";
            truth[55] = "Have you ever compromised your morals for money? How?";
            truth[56] = "Who was the last person you said, “I love you” to?";
            truth[57] = "What was your first heartbreak?";
            truth[58] = "Have you ever broken someone’s heart?";
            truth[59] = "When and where was your first kiss? Who was it with?";
            truth[60] = "When’s the last time you got rejected?";
            truth[61] = "When did you stop believing in Santa Claus?";
            truth[62] = "What’s the most bogus rumor you’ve ever heard about yourself?";
            truth[63] = "When and where was your first kiss? Who was it with?";
            truth[64] = "Do you think cheating can ever be justified? How?";
            truth[65] = "Have you ever seriously injured another person?";
            truth[66] = "What’s the scariest thing you’ve ever done?";
            truth[67] = "Have you ever had a paranormal experience?";
            truth[68] = "Have you ever gotten blackout drunk?";
            truth[69] = "Do you believe in aliens?";
            truth[70] = "What’s the pettiest thing you’ve ever done?";
            truth[71] = "Have you ever gone skinny dipping?";
            truth[72] = "What’s something you’ve done that you still feel guilty about?";
            truth[73] = "What is the worst date you’ve ever been on?";
            truth[74] = "What’s the weirdest thing you’ve ever collected?";
            truth[75] = "What’s the weirdest thing you’ve ever said to a stranger?";
            truth[76] = "Who in this server do you trust the least?";
            truth[77] = "What’s the most inappropriate time you’ve ever laughed?";
            truth[78] = "What’s your best pickup line?";
            truth[79] = "What’s the weirdest place you’ve ever given or gotten someone’s number?";
            truth[80] = "What’s the dumbest thing you’ve ever lied about?";
            truth[81] = "Have you ever said, “I love you” and not really meant it? To whom?";
            truth[82] = "What’s your least favorite memory from high school?";
            truth[83] = "What’s the worst present someone has ever given you?";
            truth[84] = "What’s the worst present you’ve ever given someone else?";
            truth[85] = "What’s the cruelest thing you’ve ever done or said to a romantic partner?";
            truth[86] = "What’s something you’re embarrassed that you’re good at?";
            truth[87] = "What was your most humbling moment?";
            truth[88] = "Have you ever let someone take the blame for something you did?";
            truth[89] = "What’s the most ridiculous thing you have an emotional attachment to?";
            truth[90] = "What’s something you’ve done that you’d judge someone else for doing?";
            truth[91] = "What’s something weird you do in your sleep?";
            truth[92] = "Who’s your hero?";
            truth[93] = "What’s something you know you need to do but aren’t looking forward to at all?";
            truth[94] = "What’s the biggest secret you’ve kept from your parents?";
            truth[95] = "What are you most proud of in your life?";
            truth[96] = "If you were rescuing people from a burning building and you had to leave one person behind from this server, who would it be?";
            truth[97] = "What’s the most offensive joke you’ve found funny?";
            truth[98] = "When’s the last time you lurked an ex on social media?";
            truth[99] = "Who would you bring with you on a deserted island?";
            truth[100] = "What was your most humbling moment?";

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Truth or Dare")
                    .addField("Truth: ", truth[num], false)
                    .setColor(Color.PINK);

            event.replyEmbeds(builder.build()).queue();
        }

        // Dare Command
        if (event.getName().equals("dare")) {
            int num = Util.randomWithRange(0, 23);
            String[] dare = new String[24];

            dare[0] = "Describe the most attractive quality of every online person.";
            dare[1] = "Do 25 sit ups without stopping.";
            dare[2] = "Send a screenshot of your search history from the past two days.";
            dare[3] = "Let the person above you pick your profile picture for the next 24 hours.";
            dare[4] = "Send the 11th picture in your photo gallery.";
            dare[5] = "Let the person above you pick your discord status for the next 24 hours.";
            dare[6] = "Show everyone a screenshot of your most recent DMs.";
            dare[7] = "Say two honest things about everyone else in the group.";
            dare[8] = "Tell everyone an embarrassing story about yourself.";
            dare[9] = "Pretend to be the person above for 10 minutes.";
            dare[10] = "Send the most recent photo in your camera roll.";
            dare[11] = "Send your most played Spotify playlist.";
            dare[12] = "Seduce a member of the same gender in the group.";
            dare[13] = "Commit arson...";
            dare[14] = "Start a conversation with a random person on your discord friends list (AND SHARE IT).";
            dare[15] = "End each sentence with the word “not” until your next turn.";
            dare[16] = "Name a famous person that looks like each player.";
            dare[17] = "Lick a bar of soap.";
            dare[18] = "Go to sleep.";
            dare[19] = "Beg and plead the person above you not to leave you for the person above them.";
            dare[20] = "Pretend to be a food. Don’t pretend to eat the food, pretend to be the food. Keep pretending until someone in the group guesses the food you are.";
            dare[21] = "Add someone to the server.";
            dare[22] = "Send a true and embarrassing confession in confessions before today ends.";
            dare[23] = "Do one thing the person above you tells you to do.";

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Truth or Dare")
                    .addField("Dare: ", dare[num], false)
                    .setColor(Color.PINK);

            event.replyEmbeds(builder.build()).queue();
        }

        // Furry Commands
        if (event.getName().equals("bark")) {
            String bark = ("BARK BARK BARK GRRRRRRR GRRRRRRR GRRRRR GROWLS BARK BARK BARK WOOF WOOF GRRRR GRRRR RAWRRRRR BARK BARK BARK ARF ARF GRRRR RAH RAH RAH GRRRRRRRR SNARLS GROWLS BARK BARK BARK SNARLS GRRR GRRR GRRRRRR AWO AWO AWOOOOOOOOOOOO GRRRRR BARK BARK WOOF WOOF WOOF BARK BARK AWOOOOOO GRR GRR GRRRR");
            event.reply(bark).queue();
        }
        if (event.getName().equals("meow")) {
            String meow = ("MEOWW HISSSSSSSSSSSS PURRRRRRR MEOWWWW MEOOOOOOOOOOOOOWWWWWWWW FEED ME BITCH PURRRR MEOWWWWW HISSSSSSSSSSSS MEOWW MEOWWWW MEOOOOOOOOOOOOOWWWWWWWW PURR MEOW MEOW MEOW MEOW MEOW YOU FAT FUCK FEED ME MEOWWW");
            event.reply(meow).queue();
        }

        // Avatar Commands
        if (event.getName().equals("avatar")) {
            String targetName;
            String targetPFP;

            // DMs
            if (!event.isFromGuild()) {
                User user = Objects.requireNonNull(event.getOption("user")).getAsUser();
                targetName = user.getName() + "#" + user.getDiscriminator();
                targetPFP = user.getEffectiveAvatarUrl();
            }
            // Server
            else {
                Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();
                assert member != null;
                targetName = member.getEffectiveName() + "#" + member.getUser().getDiscriminator();
                targetPFP = member.getEffectiveAvatarUrl();
            }
            // Embed
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(targetName)
                    .setImage(targetPFP)
                    .setColor(Color.PINK);
            event.replyEmbeds(builder.build()).queue();
        }

        // AFK Command
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
                    }
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
