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
        jda.upsertCommand("help", "Command list").queue();
        jda.upsertCommand("8ball", "Asks the magic 8ball a question")
                .addOption(OptionType.STRING, "question", "Your question to the 8ball", true)
                .queue();
        jda.upsertCommand("truth", "Generates a random truth/dare question").queue();
        jda.upsertCommand("dare", "Generates a random truth/dare question").queue();
        jda.upsertCommand("ping", "Sends pong").queue();
        jda.upsertCommand("bark", "Barks").queue();
        jda.upsertCommand("meow", "Meows").queue();
        jda.upsertCommand("avatar", "Sends user avatar")
                .addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true)
                .queue();

        // Loops through guilds and registers commands
        for (int i = 0; i < guildNum; ++i) {
            Guild guild = jda.getGuilds().get(i);
            String guildID = guild.getId();

            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("kick", "Kicks selected user")
                    .addOption(OptionType.MENTIONABLE, "user", "Kicks selected user", true)
                    .queue();
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("ban", "Bans selected user")
                    .addOption(OptionType.MENTIONABLE, "user", "Bans selected user", true)
                    .queue();
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("timeout", "Time-outs selected user")
                    .addOption(OptionType.MENTIONABLE, "user", "Times out selected user", true)
                    .queue();
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("whois", "Provides user information")
                    .addOption(OptionType.MENTIONABLE, "user", "Sends user info", true)
                    .queue();
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("afk", "Sets user AFK").queue();
            Objects.requireNonNull(jda.getGuildById(guildID)).upsertCommand("broadcast", "Broadcasts message in selected channel")
                    .addOption(OptionType.CHANNEL, "channel", "Channel message is broadcast in").addOption(OptionType.STRING, "message", "Broadcast message", true)
                    .queue();
        }

    }

}