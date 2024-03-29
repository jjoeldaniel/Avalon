package me.joel.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers slash commands
 */
public class Register extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(guildCommands()).queue();
    }
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        event.getGuild().updateCommands().addCommands(guildCommands()).queue();
    }

    @Override
    public void onReady(ReadyEvent event) {
        SubcommandData truth = new SubcommandData("truth", "Generates a random truth question");
        SubcommandData dare = new SubcommandData("dare", "Generates a random dare question");
        SubcommandData random = new SubcommandData("random", "Generates a random question");

        List<CommandData> globalCommandData = new ArrayList<>();
        globalCommandData.add(Commands.slash("help", "Lists commands"));
        globalCommandData.add(Commands.slash("8ball", "Asks the magic 8ball a question").addOption(OptionType.STRING, "question", "Your question to the 8ball", true));
        globalCommandData.add(Commands.slash("coinflip", "Flips a coin for heads or tails"));
        globalCommandData.add(Commands.slash("truthordare", "Generates a random truth/dare question").addSubcommands(truth, dare, random));
        globalCommandData.add(Commands.slash("ping", "Sends pong"));
        globalCommandData.add(Commands.slash("avatar", "Sends user avatar").addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true));

        event.getJDA().updateCommands().addCommands(globalCommandData).queue();
    }

    private static List<CommandData> guildCommands() {
        List<CommandData> guildCommandData = new ArrayList<>();

        // general
        guildCommandData.add(Commands.slash("whois", "Provides user information").addOption(OptionType.MENTIONABLE, "user", "Sends user info", true));
        guildCommandData.add(Commands.slash("afk", "Sets AFK status"));
        guildCommandData.add(Commands.slash("join", "Request for bot to join VC"));
        guildCommandData.add(Commands.slash("leave", "Request for bot to leave VC"));
        guildCommandData.add(Commands.context(Command.Type.USER, "Get member avatar"));
        guildCommandData.add(Commands.context(Command.Type.USER, "Get member info"));
        guildCommandData.add(Commands.context(Command.Type.USER, "Get user avatar"));
        guildCommandData.add(Commands.context(Command.Type.MESSAGE, "Translate message"));
        guildCommandData.add(Commands.context(Command.Type.MESSAGE, "Mock"));

        // mod
        guildCommandData.add(Commands.slash("broadcast", "Broadcasts message in selected channel").addOption(OptionType.CHANNEL, "channel", "Broadcast channel", true).addOption(OptionType.STRING, "message", "Broadcast Message", true));
        guildCommandData.add(Commands.slash("poll", "Submits poll message").addOption(OptionType.STRING, "message", "Question that will be voted on", true).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
        guildCommandData.add(Commands.slash("purge", "Purges up to 100 messages").addOption(OptionType.INTEGER, "number", "Number of messages to purge", true).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));

        // server config
        SubcommandData insults = new SubcommandData("insults", "Toggles insults");
        SubcommandData view = new SubcommandData("view", "View server config");
        SubcommandData gmgn = new SubcommandData("goodmorning_goodnight", "Toggles good morning and goodnight messages");
        SubcommandData nowPlaying = new SubcommandData("now_playing", "Toggles Now Playing messages");
        guildCommandData.add(Commands.slash("config", "Modify server config").addSubcommands(view, insults, gmgn, nowPlaying).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));

        // music
        guildCommandData.add(Commands.slash("play", "Requests a song").addOption(OptionType.STRING, "song", "Accepts Spotify, YouTube, or Apple Music", true));
        guildCommandData.add(Commands.slash("pause", "Pause playback"));
        guildCommandData.add(Commands.slash("resume", "Resume playback"));
        guildCommandData.add(Commands.slash("clear", "Clears queue"));
        guildCommandData.add(Commands.slash("queue", "Displays music queue"));
        guildCommandData.add(Commands.slash("playing", "Displays currently playing song"));
        guildCommandData.add(Commands.slash("loop", "Loops currently playing song"));
        guildCommandData.add(Commands.slash("shuffle", "Shuffles music queue"));
        guildCommandData.add(Commands.slash("volume", "Sets bot volume").addOption(OptionType.STRING, "num", "Sets volume (between 1 and 100)", true));
        guildCommandData.add(Commands.slash("skip", "Skips song").addOption(OptionType.INTEGER, "target", "Removes selected song from queue", false).addOption(OptionType.INTEGER, "num", "Removes \"x\" number of songs", false));
        guildCommandData.add(Commands.slash("seek", "Sets song position").addOption(OptionType.INTEGER, "seconds", "Sets seconds", false).addOption(OptionType.INTEGER, "minutes", "Sets minutes", false).addOption(OptionType.INTEGER, "hours", "Sets hours", false));
        guildCommandData.add(Commands.slash("lyrics", "Fetches Spotify/Apple Music song lyrics"));

        guildCommandData.addAll(me.joel.commands.reminders.Commands.guildCommands());

        return guildCommandData;
    }
}
