package me.joel.commands.reminders;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter {

    @Override
    public void onGenericEvent( @NotNull GenericEvent event )
    {

        // Register commands (#updateCommands will CLEAR all commands, don't do this more than once per startup)
        updateCommands( event );
    }

    /**
     * Updates bot commands in guild
     *
     * @param event GuildReadyEvent or GuildJoinEvent
     */
    private void updateCommands( GenericEvent event )
    {

        Guild guild;

        if ( event instanceof GuildReadyEvent guildReadyEvent )
        {
            guild = guildReadyEvent.getGuild();
        }
        else if ( event instanceof GuildJoinEvent guildJoinEvent )
        {
            guild = guildJoinEvent.getGuild();
        }
        else
        {
            return;
        }

        // Registers guild commands
        guild.updateCommands().addCommands( guildCommands() )
                .queue( ( null ), ( ( error ) -> LoggerFactory.getLogger( Register.class )
                        .info( "Failed to update commands for " + guild.getName() + " (" + guild.getId()
                                + ")" ) ) );
    }

    /**
     * Guild Commands List
     * <p>
     * All commands intended ONLY for guild usage are returned in a List
     * </p>
     *
     * @return List containing bot commands
     */
    private List<CommandData> guildCommands()
    {

        // List holding all guild commands
        List<CommandData> guildCommandData = new ArrayList<>();

        // Notifi command + subcommands
        SubcommandData reset = new SubcommandData( me.joel.commands.reminders.Commands.REMINDER_RESET,
                me.joel.commands.reminders.Commands.REMINDER_RESET_DESCRIPTION );
        SubcommandData list = new SubcommandData( me.joel.commands.reminders.Commands.REMINDER_LIST,
                me.joel.commands.reminders.Commands.REMINDER_LIST_DESCRIPTION );
        SubcommandData toggle =
                new SubcommandData( me.joel.commands.reminders.Commands.REMINDER_TOGGLE,
                        me.joel.commands.reminders.Commands.REMINDER_TOGGLE_DESCRIPTION )
                        .addOption( OptionType.BOOLEAN, me.joel.commands.reminders.Commands.REMINDER_TOGGLE_OPTION_NAME,
                                me.joel.commands.reminders.Commands.REMINDER_TOGGLE_OPTION_DESCRIPTION, true );
        SubcommandData newReminder =
                new SubcommandData( me.joel.commands.reminders.Commands.REMINDER_NEW,
                        me.joel.commands.reminders.Commands.REMINDER_NEW_DESCRIPTION )
                        .addOption( OptionType.STRING, me.joel.commands.reminders.Commands.REMINDER_NEW_OPTION_NAME,
                                me.joel.commands.reminders.Commands.REMINDER_NEW_OPTION_DESCRIPTION, true );
        SubcommandData delete =
                new SubcommandData( me.joel.commands.reminders.Commands.REMINDER_DELETE,
                        me.joel.commands.reminders.Commands.REMINDER_DELETE_DESCRIPTION )
                        .addOption( OptionType.STRING, me.joel.commands.reminders.Commands.REMINDER_DELETE_OPTION_NAME,
                                me.joel.commands.reminders.Commands.REMINDER_DELETE_OPTION_DESCRIPTION, true, true );

        guildCommandData.add(
                Commands.slash( me.joel.commands.reminders.Commands.REMINDER,
                                me.joel.commands.reminders.Commands.REMINDER_DESCRIPTION )
                        .addSubcommands( reset, list, toggle, newReminder, delete ) );

        return guildCommandData;
    }
}
