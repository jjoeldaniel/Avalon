package me.joel;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

public class CommandsRegister extends ListenerAdapter
{


    @Override
    public void onReady(@NotNull ReadyEvent event)
    {

        //event.getJDA().updateCommands().queue();

        SubcommandData truth = new SubcommandData("truth", "Generates a random truth question");
        SubcommandData dare = new SubcommandData("dare", "Generates a random dare question");
        SubcommandData random = new SubcommandData("random", "Generates a random question");

        // Global Commands
        event.getJDA().updateCommands().addCommands(

                Commands.slash("help", "Lists commands"),
                Commands.slash("8ball", "Asks the magic 8ball a question")
                        .addOption(OptionType.STRING, "question", "Your question to the 8ball", true),
                Commands.slash("coinflip", "Flips a coin for heads or tails"),
                Commands.slash("truthordare", "Generates a random truth/dare question")
                        .addSubcommands(truth)
                        .addSubcommands(dare)
                        .addSubcommands(random),
                Commands.slash("ping", "Sends pong"),
                Commands.slash("avatar", "Sends user avatar")
                        .addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true),
                Commands.slash("reload_commands", "Reloads bot commands (in case of commands not appearing)")

        ).queue();
    }

    // Guild Commands
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event)
    {

        event.getGuild().updateCommands().addCommands(

                // General
                Commands.slash("whois", "Provides user information")
                        .addOption(OptionType.MENTIONABLE, "user", "Sends user info", true),
                Commands.slash("afk", "Sets AFK status"),
                Commands.slash("confess", "Sends anonymous confession"),

                // Mod
                Commands.slash("kick", "Kicks selected user")
                        .addOption(OptionType.MENTIONABLE, "user", "Kicks selected user", true).addOption(OptionType.STRING, "reason", "Optional kick reason", false),
                Commands.slash("ban", "Bans selected user")
                        .addOption(OptionType.MENTIONABLE, "user", "Bans selected user", true).addOption(OptionType.STRING, "reason", "Optional ban reason", false),
                Commands.slash("timeout", "Time-outs selected user")
                        .addOption(OptionType.MENTIONABLE, "user", "Times out selected user", true).addOption(OptionType.INTEGER, "length", "Time in hours", false),
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
                Commands.slash("skip", "Skips song"),
                Commands.slash("queue", "Displays music queue"),
                Commands.slash("playing", "Displays currently playing song"),
                Commands.slash("loop", "Loops currently playing song")

        ).queue();
    }
}
