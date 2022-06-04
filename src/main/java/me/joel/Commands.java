package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        // Help
        if (event.getName().equals("help")) {
            if (event.getName().equals("mod")) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.PINK)
                        .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot)")
                        .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/e1e13fc10a86846545c1aa02ec102e40.png?size=4096")
                        .addField("Moderation Commands", """
                                `/kick (user) (reason)` : Kicks user with optional reason
                                `/ban (user) (reason)` : Bans user with optional reason
                                `/timeout (user) (length)` : Times out user (Default: 1hr)
                                `/broadcast (channel) (message)` : Sends message as PawBot""", false);
                event.replyEmbeds(builder.build()).setEphemeral(true)
                        .addActionRow(
                                Button.link("https://github.com/joelrico/PawBot", "Github")).queue();
            }
            if (event.getName().equals("music")) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.PINK)
                        .setTitle("PawBot Commands", "https://github.com/joelrico/PawBot)")
                        .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/e1e13fc10a86846545c1aa02ec102e40.png?size=4096")
                        .addField("Music Commands", """
                                `/play (song)` : Accepts names and YT links
                                `/pause` : Pauses playback
                                `/resume` : Resumes playback
                                `/clear` : Clears queue
                                `/queue` : Displays song queue
                                `/skip` : Skips song""", false);
                event.replyEmbeds(builder.build()).setEphemeral(true)
                        .addActionRow(
                                Button.link("https://github.com/joelrico/PawBot", "Github")).queue();
            }
            if (event.getName().equals("general")) {
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
                                `/8ball (message)` : Asks the magic 8ball a question
                                `/bark` : Self explanatory
                                `/meow` : ^^^
                                `/confess` : Sends anonymous confession""", false);
                event.replyEmbeds(builder.build()).setEphemeral(true)
                        .addActionRow(
                                Button.link("https://github.com/joelrico/PawBot", "Github")).queue();
            }
            else {
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
                                        `/skip` : Skips song""", false);

                event.replyEmbeds(builder.build()).setEphemeral(true)
                        .addActionRow(
                                Button.link("https://github.com/joelrico/PawBot", "Github")).queue();
            }
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

            if (!event.isFromGuild()) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription(user.getAsMention())
                        .setAuthor(user.getName() + "#" + user.getDiscriminator(), user.getAvatarUrl(), user.getAvatarUrl())
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .addField("Joined", joinTime.getMonthValue() + "/" + joinTime.getDayOfMonth() + "/" + joinTime.getYear(), true)
                        .addField("Created", creationDate.getMonthValue() + "/" + creationDate.getDayOfMonth() + "/" + creationDate.getYear(), true)
                        .addField("Roles [" + numRoles + "]", roles.toString(), false)
                        .setFooter("ID: " + user.getId())
                        .setColor(Color.PINK);

                event.replyEmbeds(builder.build()).queue();
                return;
            }
            if (event.isFromGuild()) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription(member.getAsMention())
                        .setAuthor(user.getName() + "#" + user.getDiscriminator(), user.getAvatarUrl(), user.getAvatarUrl())
                        .setThumbnail(member.getEffectiveAvatarUrl())
                        .addField("Joined", joinTime.getMonthValue() + "/" + joinTime.getDayOfMonth() + "/" + joinTime.getYear(), true)
                        .addField("Created", creationDate.getMonthValue() + "/" + creationDate.getDayOfMonth() + "/" + creationDate.getYear(), true)
                        .addField("Roles [" + numRoles + "]", roles.toString(), false)
                        .setFooter("ID: " + user.getId())
                        .setColor(Color.PINK);

                event.replyEmbeds(builder.build()).queue();
            }

        }

    }

}
