package me.joel.commands.reminders;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public final class Commands
{

    // Command List
    public static final String REMINDER = "reminder";
    public static final String REMINDER_DESCRIPTION = "Receive a DM when reminder is sent in mutual servers";

    public static final String REMINDER_RESET = "reset";
    public static final String REMINDER_RESET_DESCRIPTION = "Resets all stored reminders";

    public static final String REMINDER_LIST = "list";
    public static final String REMINDER_LIST_DESCRIPTION = "Lists all stored reminders";

    public static final String REMINDER_NEW = "new";
    public static final String REMINDER_NEW_DESCRIPTION = "Add a new reminder";
    public static final String REMINDER_NEW_OPTION_NAME = "word";
    public static final String REMINDER_NEW_OPTION_DESCRIPTION = "Reminder word";

    public static final String REMINDER_DELETE = "delete";
    public static final String REMINDER_DELETE_DESCRIPTION = "Delete a stored reminder";
    public static final String REMINDER_DELETE_OPTION_NAME = "word";
    public static final String REMINDER_DELETE_OPTION_DESCRIPTION = "Reminder word";

    public static final String REMINDER_TOGGLE = "toggle";
    public static final String REMINDER_TOGGLE_OPTION_NAME = "switch";
    public static final String REMINDER_TOGGLE_OPTION_DESCRIPTION = "Toggles notifications";
    public static final String REMINDER_TOGGLE_DESCRIPTION = "Toggles notifications";

    /**
     * Guild Commands List
     * <p>
     * All commands intended ONLY for guild usage are returned in a List
     * </p>
     *
     * @return List containing bot commands
     */
    public static List<CommandData> guildCommands()
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
                net.dv8tion.jda.api.interactions.commands.build.Commands.slash( me.joel.commands.reminders.Commands.REMINDER,
                                me.joel.commands.reminders.Commands.REMINDER_DESCRIPTION )
                        .addSubcommands( reset, list, toggle, newReminder, delete ) );

        return guildCommandData;
    }

}