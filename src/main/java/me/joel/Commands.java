package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Help
        if (event.getName().equals("help")) {

            // Subcommands

                // General
                if (Objects.equals(event.getSubcommandName(), "general")) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.PINK)
                            .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot")
                            .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/6931bbb87c32bf98a10d7ab9ff5f1b91.png?size=4096")
                            .addField("General Commands", """
                                    `/help (all, general, mod, music)` : Lists commands
                                    `/invite` : Returns bot invite link
                                    `/ping` : Pings bot
                                    `/coinflip` Flips a coin
                                    `/truth` : Requests truth
                                    `/dare` : Requests dare
                                    `/afk` : Sets AFK status
                                    `/avatar (user)` : Retrieves user (or target) profile picture
                                    `/8ball (message)` : Asks the magic 8ball a question
                                    `/bark` : Self explanatory
                                    `/meow` : ^^^
                                    `/confess` : Sends anonymous confession""", false);

                    event.replyEmbeds(builder.build()).setEphemeral(true)
                            .addActionRow(
                                    Button.link("https://github.com/joelrico/PawBot", "Github"),
                                    Button.link("https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot", "Invite"))
                            .queue();
                }
                // Moderation
                else if (Objects.equals(event.getSubcommandName(), "mod")) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.PINK)
                            .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot")
                            .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/6931bbb87c32bf98a10d7ab9ff5f1b91.png?size=4096")
                            .addField("Moderation Commands", """
                                    `/kick (user) (reason)` : Kicks user with optional reason
                                    `/ban (user) (reason)` : Bans user with optional reason
                                    `/timeout (user) (length)` : Times out user (Default: 1hr)
                                    `/broadcast (channel) (message)` : Sends message as PawBot""", false);

                    event.replyEmbeds(builder.build()).setEphemeral(true)
                            .addActionRow(
                                    Button.link("https://github.com/joelrico/PawBot", "Github"),
                                    Button.link("https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot", "Invite"))
                            .queue();
                }
                // Music
                else if (Objects.equals(event.getSubcommandName(), "music")) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.PINK)
                            .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot")
                            .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/6931bbb87c32bf98a10d7ab9ff5f1b91.png?size=4096")
                            .addField("Music Commands", """
                                    `/play (song)` : Accepts names and YT links
                                    `/pause` : Pauses playback
                                    `/resume` : Resumes playback
                                    `/clear` : Clears queue
                                    `/queue` : Displays song queue
                                    `/playing` : Displays currently playing song
                                    `/skip` : Skips song""", false);

                    event.replyEmbeds(builder.build()).setEphemeral(true)
                            .addActionRow(
                                    Button.link("https://github.com/joelrico/PawBot", "Github"),
                                    Button.link("https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot", "Invite"))
                            .queue();
                }
                else if (Objects.equals(event.getSubcommandName(), "all")) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.PINK)
                            .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot")
                            .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/6931bbb87c32bf98a10d7ab9ff5f1b91.png?size=4096")
                            .addField("General Commands", """
                                    `/help (all, general, mod, music)` : Lists commands
                                    `/invite` : Returns bot invite link
                                    `/ping` : Pings bot
                                    `/coinflip` Flips a coin
                                    `/truth` : Requests truth
                                    `/dare` : Requests dare
                                    `/afk` : Sets AFK status
                                    `/avatar (user)` : Retrieves user (or target) profile picture
                                    `/8ball (message)` : Asks the magic 8ball a question
                                    `/bark` : Self explanatory
                                    `/meow` : ^^^
                                    `/confess` : Sends anonymous confession""", false)
                            .addField("Moderation Commands", """
                                    `/kick (user) (reason)` : Kicks user with optional reason
                                    `/ban (user) (reason)` : Bans user with optional reason
                                    `/timeout (user) (length)` : Times out user (Default: 1hr)
                                    `/broadcast (channel) (message)` : Sends message as PawBot""", false)
                            .addField("Music Commands", """
                                    `/play (song)` : Accepts names and YT links
                                    `/pause` : Pauses playback
                                    `/resume` : Resumes playback
                                    `/clear` : Clears queue
                                    `/queue` : Displays song queue
                                    `/playing` : Displays currently playing song
                                    `/skip` : Skips song""", false);

                    event.replyEmbeds(builder.build()).setEphemeral(true)
                            .addActionRow(
                                    Button.link("https://github.com/joelrico/PawBot", "Github"),
                                    Button.link("https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot", "Invite"))
                            .queue();
                }
        }

        // Invite
        if (event.getName().equals("invite")) {
            EmbedBuilder invite = new EmbedBuilder()
                    .setTitle("Invite Link", "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot")
                    .setColor(Util.randColor());
            event.replyEmbeds(invite.build()).setEphemeral(true).queue();
        }

        // Coin Flip
        if (event.getName().equals("coinflip")) {
            String flip;
            if (Util.randomWithRange(0,100) > 50) flip = "Heads!";
            else flip = "Tails!";
            EmbedBuilder coin = new EmbedBuilder()
                    .setDescription(flip)
                    .setColor(Util.randColor());
            event.replyEmbeds(coin.build()).queue();
        }

        // Ping
        if (event.getName().equals("ping")) {
            EmbedBuilder ping = new EmbedBuilder()
                    .setTitle("Pong!")
                    .setColor(Util.randColor());
            event.replyEmbeds(ping.build()).setEphemeral(true).queue();
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

            EmbedBuilder ball = new EmbedBuilder()
                    .setTitle("8Ball")
                    .setColor(Util.randColor())
                    .setThumbnail("https://cdn.discordapp.com/attachments/810456406620241931/981063293428957244/unknown.png?size=4096")
                    .addField("Your question:", question, false)
                    .addField(output, "", false);

            event.replyEmbeds(ball.build()).queue();
        }

        // Truth or Dare
        if (event.getName().equals("truthordare")) {

            if (Objects.equals(event.getSubcommandName(), "truth")) {
                String truth = truthordare.truth();

                EmbedBuilder truthNotDare = new EmbedBuilder()
                        .setTitle("Truth or Dare")
                        .addField("Truth: ", truth, false)
                        .setColor(Util.randColor());

                event.replyEmbeds(truthNotDare.build()).queue();
                return;
            }

            if (Objects.equals(event.getSubcommandName(), "dare")) {
                String dare = truthordare.dare();

                EmbedBuilder dareNotTruth = new EmbedBuilder()
                        .setTitle("Truth or Dare")
                        .addField("Dare: ", dare, false)
                        .setColor(Util.randColor());

                event.replyEmbeds(dareNotTruth.build()).queue();
            }
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
            EmbedBuilder avatar = new EmbedBuilder()
                    .setTitle(targetName)
                    .setImage(targetPFP)
                    .setColor(Util.randColor());
            event.replyEmbeds(avatar.build()).queue();
        }

        // Confess
        if (event.getName().equals("confess")) {
            String message = Objects.requireNonNull(event.getOption("message")).getAsString();
            String channelID = "";

            EmbedBuilder confessionPost = new EmbedBuilder()
                    .setTitle("Anonymous Confession")
                    .setDescription("\"" + message + "\"")
                    .setColor(Util.randColor());

            // Find confessions channel
            try {
                int channelNum = Objects.requireNonNull(event.getGuild()).getTextChannels().size();
                for (int i = 0; i < channelNum; ++i) {
                    if (event.getGuild().getTextChannels().get(i).getName().contains("confessions")) {
                        channelID = event.getGuild().getTextChannels().get(i).getId();
                    }
                }

                Objects.requireNonNull(event.getGuild().getTextChannelById(channelID)).sendMessageEmbeds(confessionPost.build()).queue();

                EmbedBuilder confessionSubmit = new EmbedBuilder()
                        .setTitle("Confession Submitted")
                        .setDescription("\"" + message + "\"")
                        .setColor(Util.randColor());

                event.replyEmbeds(confessionSubmit.build()).setEphemeral(true).queue();
            }
            catch (Exception channelNotFound) {
                EmbedBuilder confessionError = new EmbedBuilder()
                        .setTitle("Error!")
                        .setDescription("No confession channel found!")
                        .setColor(Util.randColor());

                event.replyEmbeds(confessionError.build()).setEphemeral(true).queue();
            }
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

            if (!event.isFromGuild()) {
                EmbedBuilder whois = new EmbedBuilder()
                        .setDescription(user.getAsMention())
                        .setAuthor(user.getName() + "#" + user.getDiscriminator(), user.getAvatarUrl(), user.getAvatarUrl())
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .addField("Joined", joinTime.getMonthValue() + "/" + joinTime.getDayOfMonth() + "/" + joinTime.getYear(), true)
                        .addField("Created", creationDate.getMonthValue() + "/" + creationDate.getDayOfMonth() + "/" + creationDate.getYear(), true)
                        .addField("Roles [" + numRoles + "]", roles.toString(), false)
                        .setFooter("ID: " + user.getId())
                        .setColor(Util.randColor());

                event.replyEmbeds(whois.build()).queue();
                return;
            }
            if (event.isFromGuild()) {
                EmbedBuilder whois = new EmbedBuilder()
                        .setDescription(member.getAsMention())
                        .setAuthor(user.getName() + "#" + user.getDiscriminator(), user.getAvatarUrl(), user.getAvatarUrl())
                        .setThumbnail(member.getEffectiveAvatarUrl())
                        .addField("Joined", joinTime.getMonthValue() + "/" + joinTime.getDayOfMonth() + "/" + joinTime.getYear(), true)
                        .addField("Created", creationDate.getMonthValue() + "/" + creationDate.getDayOfMonth() + "/" + creationDate.getYear(), true)
                        .addField("Roles [" + numRoles + "]", roles.toString(), false)
                        .setFooter("ID: " + user.getId())
                        .setColor(Util.randColor());

                event.replyEmbeds(whois.build()).queue();
            }

        }

    }

}
