package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GuildEvents extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            member.deafen(true).queue();
        }
    }

    @Override
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            if (Objects.requireNonNull(member.getVoiceState()).inAudioChannel()) {
                member.deafen(true).queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        // paw patrol and cat club welcome messages
        if (event.getGuild().getId().equals("645471751316307998") || event.getGuild().getId().equals("971225319153479790")) {

            Member member = event.getMember();
            EmbedBuilder memberJoin = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setTitle("A new member has joined!")
                    .setDescription
                            (
                                    "Welcome " + member.getAsMention() + " to " + event.getGuild().getName() +
                                            "! There are now " + event.getGuild().getMemberCount() + " members in " + event.getGuild().getName() + "."
                            )
                    .setThumbnail(member.getEffectiveAvatarUrl())
                    .setFooter("User: " + member.getUser().getName() +"#" + member.getUser().getDiscriminator() + " ID: " + member.getId());

            // find welcome channel
            try {

                int channelNum = Objects.requireNonNull(event.getGuild()).getTextChannels().size();
                for (int i = 0; i < channelNum; ++i) {

                    if (event.getGuild().getTextChannels().get(i).getName().contains("welcome")) {

                        event.getGuild().getTextChannels().get(i).sendMessageEmbeds(memberJoin.build()).queue();
                        return;
                    }
                }
            }
            // If no welcomeChannel found
            catch (Exception ignore) {}
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        //event.getJDA().updateCommands().queue();

        SubcommandData truth = new SubcommandData("truth", "Generates a random truth question");
        SubcommandData dare = new SubcommandData("dare", "Generates a random dare question");
        SubcommandData random = new SubcommandData("random", "Generates a random question");

        // Global Commands
        event.getJDA().updateCommands().addCommands(

                // Slash
                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("help", "Lists commands"),
                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("8ball", "Asks the magic 8ball a question")
                        .addOption(OptionType.STRING, "question", "Your question to the 8ball", true),
                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("coinflip", "Flips a coin for heads or tails"),
                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("truthordare", "Generates a random truth/dare question")
                        .addSubcommands(truth)
                        .addSubcommands(dare)
                        .addSubcommands(random),
                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("ping", "Sends pong"),
                Commands.slash("avatar", "Sends user avatar")
                        .addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true)

        ).queue();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        registerCommands(event.getGuild());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (event.isFromType(ChannelType.TEXT)) {

            if (!event.getAuthor().isBot() && event.isFromGuild() && !event.getChannel().asTextChannel().isNSFW()) {

                // Grabs user input
                String messageSent = event.getMessage().getContentRaw().toLowerCase();

                // Goodnight
                if (messageSent.contains("goodnight") || messageSent.contains("good night") && Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("goodnight sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
                // Good morning
                if (messageSent.contains("goodmorning") || messageSent.contains("good morning") && Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("good morning sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
                // Insult
                if (isInsult(messageSent) && Util.randomWithRange(0, 100) > 75) {
                    event.getMessage().replyEmbeds(randomInsult().build()).queue();
                }

            }
        }
    }

    /**
     * Registers commands on guild
     */
    void registerCommands(Guild guild) {
        guild.updateCommands().addCommands(
                // General
                Commands.slash("whois", "Provides user information")
                        .addOption(OptionType.MENTIONABLE, "user", "Sends user info", true),
                Commands.slash("afk", "Sets AFK status"),
                Commands.slash("confess", "Sends anonymous confession")
                        .addOption(OptionType.STRING, "message", "Confession message", true),
                Commands.context(Command.Type.USER, "Get member avatar"),
                Commands.context(Command.Type.USER, "Get member info"),

                // Mod
                Commands.slash("broadcast", "Broadcasts message in selected channel")
                        .addOption(OptionType.CHANNEL, "channel", "Channel message is broadcast in", true).addOption(OptionType.STRING, "message", "Broadcast message", true),
                Commands.slash("purge", "Purges up to 100 messages")
                        .addOption(OptionType.INTEGER, "number", "Number of messages to purge", true),

                // Music
                Commands.slash("play", "Requests a song")
                        .addOption(OptionType.STRING, "song", "Accepts youtube links or song names", true),
                Commands.slash("pause", "Pause playback"),
                Commands.slash("volume", "Requests a song")
                        .addOption(OptionType.STRING, "num", "Sets volume (between 1 and 100)", true),
                Commands.slash("resume", "Resume playback"),
                Commands.slash("clear", "Clears queue"),
                Commands.slash("skip", "Skips song")
                        .addOption(OptionType.INTEGER, "song_num", "Removes selected song from queue", false)
                        .addOption(OptionType.INTEGER, "songs_to_skip", "Removes \"x\" number of songs", false),
                Commands.slash("queue", "Displays music queue"),
                Commands.slash("playing", "Displays currently playing song"),
                Commands.slash("loop", "Loops currently playing song")
        ).queue();
    }

    /**
     * @return True if message contains insult
     */
    boolean isInsult(String message) {
            return message.contains("fuck") || (message.contains("cunt")) || (message.contains("prick") || (message.contains("slut")) || (message.contains("asshole")) || (message.contains("bastard")) || (message.contains("twat")) || (message.contains("bitch")) || (message.contains("dick")));
        }

    /**
     * @return Insult embed reply
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
