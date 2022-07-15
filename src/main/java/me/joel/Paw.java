package me.joel;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import java.util.Objects;

public class Paw {
    public static void main(String[] args) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault("token")

                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new Commands())
                .addEventListeners(new ReactMessages())
                .addEventListeners(new AFK())
                .addEventListeners(new MusicCommands())
                .addEventListeners(new ModCommands())
                .addEventListeners(new AFKReturn())
                .enableCache(CacheFlag.VOICE_STATE)
                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        int guildNum = jda.getGuilds().size();
        jda.getPresence().setActivity(Activity.listening(" " + (guildNum) + " servers!" ));
        //jda.updateCommands().queue();

        // Commands List
            // Help
            jda.upsertCommand("help", "Lists commands").queue();
            // 8Ball
            jda.upsertCommand("8ball", "Asks the magic 8ball a question")
                    .addOption(OptionType.STRING, "question", "Your question to the 8ball", true)
                    .queue();
            // Coin Flip
            jda.upsertCommand("coinflip", "Flips a coin for heads or tails").queue();
            // Invite
            jda.upsertCommand("invite", "Returns bot invite link").queue();
            // Truth or Dare
            SubcommandData truth = new SubcommandData("truth", "Generates a random truth question");
            SubcommandData dare = new SubcommandData("dare", "Generates a random dare question");
            SubcommandData random = new SubcommandData("random", "Generates a random question");
            jda.upsertCommand("truthordare", "Generates a random truth/dare question")
                    .addSubcommands(truth)
                    .addSubcommands(dare)
                    .addSubcommands(random)
                    .queue();
            // Ping
            jda.upsertCommand("ping", "Sends pong").queue();
            // Avatar
            jda.upsertCommand("avatar", "Sends user avatar")
                    .addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true)
                    .queue();

        // Loops through guilds and registers commands
        for (int i = 0; i < guildNum; ++i) {
            Guild guild = jda.getGuilds().get(i);
            String guildID = guild.getId();

            // Mod Commands
                // Kick
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("kick", "Kicks selected user")
                        .addOption(OptionType.MENTIONABLE, "user", "Kicks selected user", true).addOption(OptionType.STRING, "reason", "Optional kick reason", false)
                        .queue();
                // Ban
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("ban", "Bans selected user")
                        .addOption(OptionType.MENTIONABLE, "user", "Bans selected user", true).addOption(OptionType.STRING, "reason", "Optional ban reason", false)
                        .queue();
                // Timeout
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("timeout", "Time-outs selected user")
                        .addOption(OptionType.MENTIONABLE, "user", "Times out selected user", true).addOption(OptionType.INTEGER, "length", "Time in hours", false)
                        .queue();
                // Broadcast
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("broadcast", "Broadcasts message in selected channel")
                        .addOption(OptionType.CHANNEL, "channel", "Channel message is broadcast in", true).addOption(OptionType.STRING, "message", "Broadcast message", true)
                        .queue();
                // Purge
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("purge", "Purges up to 100 messages")
                        .addOption(OptionType.INTEGER, "number", "Number of messages to purge", true)
                        .queue();
            // General Commands
                // Whois
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("whois", "Provides user information")
                        .addOption(OptionType.MENTIONABLE, "user", "Sends user info", true)
                        .queue();
                // Confess
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("confess", "Posts an anonymous confession")
                        .addOption(OptionType.STRING, "message", "Confession message", true)
                        .queue();
                // AFK
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("afk", "Enables/disables AFK status")
                        .queue();
            // Music Commands
                // Play
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("play", "Requests a song")
                        .addOption(OptionType.STRING, "song", "Accepts youtube links or song names", true)
                        .queue();
                // Pause
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("pause", "Pause playback")
                        .queue();
                // Resume
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("resume", "Resume playback")
                        .queue();
                // Clear
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("clear", "Clears queue")
                        .queue();
                // Skip
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("skip", "Skips song")
                        .queue();
                // Queue
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("queue", "Displays music queue")
                        .queue();
                // Playing
                Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("playing", "Displays currently playing song")
                        .queue();
        }

    }

}