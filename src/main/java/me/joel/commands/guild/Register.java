package me.joel.commands.guild;

import me.joel.commands.mod.Toggle;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> guildCommandData = new ArrayList<>();

        guildCommandData.add(Commands.slash("whois", "Provides user information").addOption(OptionType.MENTIONABLE, "user", "Sends user info", true));
        guildCommandData.add(Commands.slash("afk", "Sets AFK status"));
        guildCommandData.add(Commands.slash("confess", "Sends anonymous confession").addOption(OptionType.STRING, "message", "Confession message", true));
        guildCommandData.add(Commands.slash("join", "Request for bot to join VC"));
        guildCommandData.add(Commands.slash("leave", "Request for bot to leave VC"));
        guildCommandData.add(Commands.context(Command.Type.USER, "Get member avatar"));
        guildCommandData.add(Commands.context(Command.Type.USER, "Get member info"));
        guildCommandData.add(Commands.context(Command.Type.MESSAGE, "Translate message"));

        event.getJDA().addEventListener(new AFK(), new Confess(), new Join(), new Leave(), new Toggle(), new Translate(), new WhoIs());
        event.getGuild().updateCommands().addCommands(guildCommandData).complete();
    }
}
