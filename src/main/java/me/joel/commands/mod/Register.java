package me.joel.commands.mod;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> modCommandData = new ArrayList<>();

        SubcommandData insults = new SubcommandData("insults", "Toggles insults");
        SubcommandData gmgn = new SubcommandData("goodmorning_goodnight", "Toggles good morning and goodnight messages");
        SubcommandData nowPlaying = new SubcommandData("now_playing", "Toggles Now Playing messages");

        modCommandData.add(Commands.slash("toggle", "Toggles bot features").addSubcommands(insults, gmgn, nowPlaying).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
        modCommandData.add(Commands.slash("broadcast", "Broadcasts message in selected channel").addOption(OptionType.CHANNEL, "channel", "Broadcast channel", true).addOption(OptionType.STRING, "message", "Broadcast Message", true));
        modCommandData.add(Commands.slash("poll", "Submits poll message").addOption(OptionType.STRING, "message", "Question that will be voted on", true).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
        modCommandData.add(Commands.slash("purge", "Purges up to 100 messages").addOption(OptionType.INTEGER, "number", "Number of messages to purge", true).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));

        event.getJDA().addEventListener(new Broadcast(), new Poll(), new Purge(), new Toggle());
        event.getGuild().updateCommands().addCommands(modCommandData).complete();
    }
}