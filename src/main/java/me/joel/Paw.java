package me.joel;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import java.util.Objects;

public class Paw {
    public static void main(String[] args) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault("OTcxMjM5NDM4ODkyMDE5NzQz.Gamtz3.a7DObfsPstEU4B4g5GBWi4wYOQH-kwpg1j8fa0")

                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new Commands())
                .addEventListeners(new Insults())
                .addEventListeners(new ReactMessages())
                .addEventListeners(new AFK())
                .enableCache(CacheFlag.VOICE_STATE)
                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        int guildNum = jda.getGuilds().size();
        jda.getPresence().setActivity(Activity.listening(" " + (guildNum) + " servers!" ));

        // Commands List

            // Help
            jda.upsertCommand("help", "Command list").queue();
            // 8Ball
            jda.upsertCommand("8ball", "Asks the magic 8ball a question")
                    .addOption(OptionType.STRING, "question", "Your question to the 8ball", true)
                    .queue();
            // Truth
            jda.upsertCommand("truth", "Generates a random truth/dare question").queue();
            // Dare
            jda.upsertCommand("dare", "Generates a random truth/dare question").queue();
            // Ping
            jda.upsertCommand("ping", "Sends pong").queue();
            // Bark
            jda.upsertCommand("bark", "Barks").queue();
            // Meow
            jda.upsertCommand("meow", "Meows").queue();
            // Avatar
            jda.upsertCommand("avatar", "Sends user avatar")
                    .addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true)
                    .queue();

        // Loops through guilds and registers commands
        for (int i = 0; i < guildNum; ++i) {
            Guild guild = jda.getGuilds().get(i);
            String guildID = guild.getId();

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
            // Whois
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("whois", "Provides user information")
                    .addOption(OptionType.MENTIONABLE, "user", "Sends user info", true)
                    .queue();
            // AFK
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("afk", "Sets user AFK").queue();
            // Broadcast
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("broadcast", "Broadcasts message in selected channel")
                    .addOption(OptionType.CHANNEL, "channel", "Channel message is broadcast in", true).addOption(OptionType.STRING, "message", "Broadcast message", true)
                    .queue();
            // Confess
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("confess", "Posts an anonymous confession")
                    .addOption(OptionType.STRING, "message", "Confession message", true)
                    .queue();
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
        }

    }

}