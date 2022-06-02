package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {

    String prefix = "paw";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Help
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
                                        `/afk` : Sets AFK status
                                        `/avatar (user)` : Retrieves user (or target) profile picture
                                        `/8ball (question)` : Asks the magic 8ball a question
                                        `/bark` : Self explanatory
                                        `/meow` : ^^^
                                        `paw play (YT Link)` : Plays music""", false)
                    .addField("Moderation Commands", """
                                        `/kick (user)` : Kicks user
                                        `/ban (user)` : Bans user
                                        `/timeout (user)` : Times out user (Default: 1hr)
                                        `/broadcast (channelID) (text)` : Sends message as PawBot""", false);
            event.replyEmbeds(builder.build()).setEphemeral(true)
                    .addActionRow(
                            Button.link("https://github.com/joelrico/PawBot", "Github")).queue();
        }

        // Ping
        if (event.getName().equals("ping")) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Pong!")
                    .setColor(Color.PINK);
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // AFK
        if (event.getName().equals("afk")) {

            try {
                String userName = Objects.requireNonNull(event.getMember()).getEffectiveName();
                event.getMember().modifyNickname("(AFK) " + userName).queue();
                System.out.println(userName + " is now AFK");
            } catch (Exception except) {
                EmbedBuilder builder = new EmbedBuilder()
                        .addField("", "Can't rename owner/equal role", false)
                        .setColor(Color.PINK);
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .addField("", "You have now been set to AFK!", false)
                    .setColor(Color.PINK);

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        // 8Ball
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

        // Truth
        if (event.getName().equals("truth")) {
            String truth = truthordare.truth();

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Truth or Dare")
                    .addField("Truth: ", truth, false)
                    .setColor(Color.PINK);

            event.replyEmbeds(builder.build()).queue();
        }

        // Dare
        if (event.getName().equals("dare")) {
            String dare = truthordare.dare();

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Truth or Dare")
                    .addField("Dare: ", dare, false)
                    .setColor(Color.PINK);

            event.replyEmbeds(builder.build()).queue();
        }

        // Furry
        if (event.getName().equals("bark")) {
            String bark = ("BARK BARK BARK GRRRRRRR GRRRRRRR GRRRRR GROWLS BARK BARK BARK WOOF WOOF GRRRR GRRRR RAWRRRRR BARK BARK BARK ARF ARF GRRRR RAH RAH RAH GRRRRRRRR SNARLS GROWLS BARK BARK BARK SNARLS GRRR GRRR GRRRRRR AWO AWO AWOOOOOOOOOOOO GRRRRR BARK BARK WOOF WOOF WOOF BARK BARK AWOOOOOO GRR GRR GRRRR");
            event.reply(bark).queue();
        }
        if (event.getName().equals("meow")) {
            String meow = ("MEOWW HISSSSSSSSSSSS PURRRRRRR MEOWWWW MEOOOOOOOOOOOOOWWWWWWWW FEED ME BITCH PURRRR MEOWWWWW HISSSSSSSSSSSS MEOWW MEOWWWW MEOOOOOOOOOOOOOWWWWWWWW PURR MEOW MEOW MEOW MEOW MEOW YOU FAT FUCK FEED ME MEOWWW");
            event.reply(meow).queue();
        }

        // Avatar
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

        // Confess
        if (event.getName().equals("confess")) {
            String message = Objects.requireNonNull(event.getOption("message")).getAsString();
            String channelID = "";

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Anonymous Confession")
                    .setDescription("\"" + message + "\"")
                    .setColor(Util.randColor());

            // Find confessions channel
            int channelNum = Objects.requireNonNull(event.getGuild()).getTextChannels().size();
            for (int i = 0; i < channelNum; ++i) {
                if (event.getGuild().getTextChannels().get(i).getName().contains("confessions")) {
                    channelID = event.getGuild().getTextChannels().get(i).getId();
                }
            }

            Objects.requireNonNull(event.getGuild().getTextChannelById(channelID)).sendMessageEmbeds(builder.build()).queue();

            EmbedBuilder builder2 = new EmbedBuilder()
                    .setTitle("Confession Submitted")
                    .setDescription("\"" + message + "\"")
                    .setColor(Util.randColor());

            event.replyEmbeds(builder2.build()).setEphemeral(true).queue();
        }

        // Whois Command
        if (event.getName().equals("whois")) {
            Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();
            assert member != null;
            User user = member.getUser();
            LocalDateTime joinTime = member.getTimeJoined().toLocalDateTime();
            LocalDateTime creationDate = user.getTimeCreated().toLocalDateTime();
            int numRoles = member.getRoles().size();
            StringBuilder roles = new StringBuilder();
            for (int i = 0; i < numRoles; ++i) {
                roles.append("<@&").append(member.getRoles().get(i).getId()).append("> ");
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription(user.getAsMention())
                    .setAuthor(user.getName() + "#" + user.getDiscriminator(), user.getAvatarUrl(), user.getAvatarUrl())
                    .setThumbnail(user.getEffectiveAvatarUrl())
                    .addField("Joined", joinTime.getMonthValue() + "/" + joinTime.getDayOfMonth() + "/" + joinTime.getYear() , true)
                    .addField("Created", creationDate.getMonthValue() + "/" + creationDate.getDayOfMonth() + "/" + creationDate.getYear() , true)
                    .addField("Roles [" + numRoles + "]", roles.toString(), false)
                    .setFooter("ID: " + user.getId())
                    .setColor(Color.PINK);

            event.replyEmbeds(builder.build()).queue();
        }

        // Kick
        if (event.getName().equals("kick")) {
            if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) return;
            Member target = Objects.requireNonNull(event.getOption("user")).getAsMember();
            try {
                assert target != null;
                target.kick().queue();

                if (target.isOwner()) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("You can't kick this person!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Color.PINK)
                            .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(target.getEffectiveName() + " has been kicked")
                        .setImage(target.getEffectiveAvatarUrl())
                        .setColor(Color.PINK);
                event.replyEmbeds(builder.build()).queue();
            }
            catch (Exception e) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("You can't kick this person!")
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.PINK)
                        .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
        }

        // Ban
        if (event.getName().equals("ban")) {
            if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS)) return;
            Member target = Objects.requireNonNull(event.getOption("user")).getAsMember();
            try {
                assert target != null;
                target.ban(0).queue();

                if (target.isOwner()) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("You can't ban this person!")
                            .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Color.PINK)
                            .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(target.getEffectiveName() + " has been banned")
                        .setImage(target.getEffectiveAvatarUrl())
                        .setColor(Color.PINK);

                event.replyEmbeds(builder.build()).queue();
            }
            catch (Exception e) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("You can't ban this person!")
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.PINK)
                        .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
        }

        // Timeout
        if (event.getName().equals("timeout")) {
            if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MODERATE_MEMBERS)) return;
            Member target = Objects.requireNonNull(event.getOption("user")).getAsMember();
            try {
                assert target != null;
                target.timeoutFor(1, TimeUnit.HOURS).queue();

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(target.getEffectiveName() + " has been timed out")
                        .setImage(target.getEffectiveAvatarUrl())
                        .setColor(Color.PINK);

                event.replyEmbeds(builder.build()).queue();
            }
            catch (Exception e) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("You can't time out this person!")
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.PINK)
                        .addField("Think this is an error?", "Try contacting your local server administrator/moderator!", false);
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
        }

        // Broadcast
        if (event.getName().equals("broadcast")) {
            if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) return;
            Channel channel = Objects.requireNonNull(event.getOption("channel")).getAsTextChannel();
            assert channel != null;
            String channelID = channel.getId();
            String message = Objects.requireNonNull(event.getOption("message")).getAsString();

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Message sent!")
                    .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.PINK)
                    .addField("Message", message, false);

            Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(channelID)).sendMessage(message).queue();
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }
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
                            System.out.println(link);
                        }

                        // Joins VC
                        audioManager.openAudioConnection(memberChannel);
                        Member bot = event.getMember().getGuild().getMemberById("971239438892019743");
                        assert bot != null;
                        event.getGuild().deafen(bot, true).queue();

                        // Plays song
                        PlayerManager.getINSTANCE().loadAndPlay(event.getTextChannel(), link);
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
