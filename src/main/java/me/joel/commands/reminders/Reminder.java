package me.joel.commands.reminders;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reminder extends ListenerAdapter
{

    // Discord member ID : Set of reminder phrases
    HashMap<String, LinkedHashSet<String>> reminderMap = new HashMap<>();

    // Discord member ID : Notification Activation (true = activated, false = deactivated)
    HashMap<String, Boolean> notificationToggle = new HashMap<>();

    int min = 0;
    int max = 5;

    final int MAX_REMINDERS = 50;

    // SLF4J Logger
    final Logger log = LoggerFactory.getLogger( Reminder.class );

    @Override
    public void onGuildReady( @NotNull GuildReadyEvent event )
    {
        // Loads reminders from database
        try
        {
            Database.syncData( reminderMap, notificationToggle );
        }
        catch ( SQLException e )
        {
            log.error( "Failed to sync database", e );
        }
    }

    @Override
    public void onSlashCommandInteraction( @NotNull SlashCommandInteractionEvent event )
    {

        if (!isValidInteraction( event ) || !( event.getName().equals( Commands.REMINDER ) ) )
        {
            return;
        }

        switch ( event.getSubcommandName() )
        {
            case ( Commands.REMINDER_NEW ) ->
            {
                // Takes string result of option ID matching "word"
                String triggerPhrase = event.getOption( "word" ).getAsString().toLowerCase();

                if ( reminderMap.containsKey( event.getMember().getId() ) )
                {
                    if ( inSet( triggerPhrase, reminderMap.get( event.getMember().getId() ) ) )
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor( Color.red )
                                .setTitle( "Error" )
                                .setDescription( "Duplicate reminder!" );

                        event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                        return;
                    }
                    if ( reminderMap.get( event.getMember().getId() ).size() >= MAX_REMINDERS )
                    {
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor( Color.red )
                                .setTitle( "Error" )
                                .setDescription( "Max reminders reached!" );

                        event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                        return;
                    }
                }

                try
                {
                    Database.initializeIfNotExistsAndAppend( event.getMember(), triggerPhrase, reminderMap,
                            notificationToggle );
                }
                catch ( SQLException e )
                {
                    log.error( "Failed to append reminder", e );
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor( Color.red )
                            .setTitle( "Error" )
                            .setDescription( "An error occurred while adding your reminder. Please try again later." );

                    event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor( Color.green )
                        .setDescription( "Reminder added: \"" + triggerPhrase + "\"" );

                event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
            }
            case ( Commands.REMINDER_RESET ) ->
            {
                if ( reminderMap.get( event.getMember().getId() ).isEmpty() )
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor( Color.red )
                            .setTitle( "Error" )
                            .setDescription( "No reminders to remove!" );

                    event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor( Color.red )
                        .setTitle( "Are you sure you want to reset all reminders?" );

                event.replyEmbeds( builder.build() ).setActionRow(
                        Button.danger( "reset", "Yes" )
                ).setEphemeral( true ).queue();
            }
            case ( Commands.REMINDER_LIST ) ->
            {
                // If no reminders are found
                if ( !reminderMap.containsKey( event.getMember().getId() ) || reminderMap.get( event.getMember().getId() )
                        .isEmpty() || reminderMap.get( event.getMember().getId() ) == null )
                {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor( Color.red )
                            .setTitle( "No reminders found" );

                    event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                }
                else
                {
                    List<String> list = new ArrayList<>( reminderMap.get( event.getMember().getId() ) );
                    EmbedBuilder builder = reminderList( 0, 5, list );

                    // If next page does not exist
                    if ( list.size() <= 5 )
                    {
                        event.replyEmbeds( builder.build() ).setActionRow(
                                Button.secondary( "previous", "Previous" ).asDisabled(),
                                Button.secondary( "next", "Next" ).asDisabled()
                        ).setEphemeral( true ).queue();
                    }
                    else
                    {
                        event.replyEmbeds( builder.build() ).setActionRow(
                                Button.secondary( "previous", "Previous" ).asDisabled(),
                                Button.secondary( "next", "Next" ).asEnabled()
                        ).setEphemeral( true ).queue();
                    }
                }
            }
            case ( Commands.REMINDER_DELETE ) ->
            {
                // Takes string result of option ID matching "word"
                String query = event.getOption( "word" ).getAsString().toLowerCase();

                // Check if query is stored
                if ( reminderMap.get( event.getMember().getId() ) != null && inSet( query,
                        reminderMap.get( event.getMember().getId() ) ) )
                {

                    reminderMap.get( event.getMember().getId() ).remove( query );
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor( Color.green )
                            .setDescription( "Reminder deleted: \"" + query + "\"" );

                    try
                    {
                        Database.deletePhrase( event.getMember(), query, reminderMap, notificationToggle );
                    }
                    catch ( SQLException e )
                    {
                        log.error( "Failed to delete reminder or sync database", e );
                        EmbedBuilder error = new EmbedBuilder()
                                .setColor( Color.red )
                                .setTitle( "Error" )
                                .setDescription( "An error occurred while deleting your reminder. Please try again later." );

                        event.replyEmbeds( error.build() ).setEphemeral( true ).queue();
                        return;
                    }

                    event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                }
                else
                {

                    String similarPhrase = null;

                    for ( String str : reminderMap.get( event.getMember().getId() ) )
                    {
                        if ( FuzzySearch.ratio( str, query ) > 80 )
                        {
                            similarPhrase = str;
                            break;
                        }
                    }

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor( Color.red )
                            .setTitle( "Error" )
                            .setDescription( "Reminder not found!" );

                    if ( similarPhrase != null )
                    {
                        builder.setDescription( "Reminder not found! Did you mean \"" + similarPhrase + "\"?" );
                    }

                    event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                }
            }
            case ( Commands.REMINDER_TOGGLE ) ->
            {
                try
                {
                    Database.initializeIfNotExists( event.getMember() );

                    boolean toggle = event.getOption( "switch" ).getAsBoolean();
                    Database.toggleTrigger( event.getMember(), toggle );
                    notificationToggle.put( event.getMember().getId(), toggle );

                    EmbedBuilder builder = new EmbedBuilder();

                    if ( toggle )
                    {
                        builder.setTitle( "Notification features are now enabled" );
                        builder.setColor( Color.green );
                    }
                    else
                    {
                        builder.setTitle( "Notification features are now disabled" );
                        builder.setColor( Color.red );
                    }

                    event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
                }
                catch ( SQLException e )
                {
                    log.error( "Failed to check for stored user", e );
                    EmbedBuilder error = new EmbedBuilder()
                            .setColor( Color.red )
                            .setTitle( "Error" )
                            .setDescription( "A database error has occurred. Please try again later." );

                    event.replyEmbeds( error.build() ).setEphemeral( true ).queue();
                    return;
                }
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction( @NotNull CommandAutoCompleteInteractionEvent event )
    {
        if ( event.getName().equals( Commands.REMINDER ) && event.getFocusedOption().getName().equals( "word" ) )
        {

            if ( reminderMap.get( event.getMember().getId() ) == null )
            {
                return;
            }

            String[] words = new String[reminderMap.get( event.getMember().getId() ).size()];
            words = reminderMap.get( event.getMember().getId() ).toArray( words );

            List<Command.Choice> options = Stream.of( words )
                    .filter( word -> word.startsWith( event.getFocusedOption().getValue() ) )
                    .map( word -> new Command.Choice( word, word ) )
                    .collect( Collectors.toList() );

            event.replyChoices( options ).queue();
        }
    }

    @Override
    public void onMessageReceived( @NotNull MessageReceivedEvent event )
    {

        // Only listen to guild messages from live users
        if ( !isValidInteraction( event ) )
        {
            return;
        }

        String messageContent = event.getMessage().getContentRaw().toLowerCase();

        // Loop through HashMap keySet
        for ( String id : reminderMap.keySet() )
        {

            // If members value contains messageContent
            if ( inSet( messageContent, reminderMap.get( id ) ) )
            {

                // Retrieve triggered member
                RestAction<Member> action = event.getGuild().retrieveMemberById( id );
                action.queue( ( null ),

                        // Handle failure if the member does not exist (or another issue appeared)
                        ( error ) -> log.error( error.toString() )
                );
                Member member = event.getGuild().getMemberById( id );

                // Skip if message is self-triggered or member is missing view permissions
                if ( member == null || event.getMember() == member || !member.hasPermission( event.getGuildChannel(),
                        Permission.VIEW_CHANNEL ) )
                {
                    continue;
                }

                // If no toggle setting exists
                if ( !notificationToggle.containsKey( event.getMember().getId() ) )
                {
                    notificationToggle.put( event.getMember().getId(), true );
                }
                // If toggle == false, skip to next ID
                else if ( !notificationToggle.get( event.getMember().getId() ) )
                {
                    continue;
                }

                // Embed
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle( "Message Reminder" )
                        .setColor( Color.green )
                        .setFooter( "All timestamps are formatted in PST / UTC+7 !" );

                // Retrieve last 4 messages in channel message history
                MessageHistory history = event.getChannel().getHistoryBefore( event.getMessageId(), 4 ).complete();
                List<String> messages = new ArrayList<>();

                // Add messages to list and reverse messages in order of least -> most recent
                for ( Message message : history.getRetrievedHistory() )
                {
                    String memberName = message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator();
                    messages.add(
                            "**[" + TimeFormat.TIME_LONG.atTimestamp( message.getTimeCreated().toEpochSecond() * 1000 )
                                    + "] " + memberName + ":** " + message.getContentRaw() + "\n" );
                }
                Collections.reverse( messages );

                // Add trigger message
                String triggerMember = event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor()
                        .getDiscriminator();
                builder.addField( "",
                        "**[" + TimeFormat.TIME_LONG.now() + "] " + triggerMember + ":** " + event.getMessage()
                                .getContentRaw(), false );

                // Finish embed
                builder.setDescription( String.join( "", messages ) );
                builder.addField( "**Source Message**", "[Jump to](" + event.getJumpUrl() + ")", false );

                // DM triggered member
                member.getUser().openPrivateChannel()
                        .flatMap( channel -> channel.sendMessageEmbeds( builder.build() ).addActionRow(
                                Button.secondary( "server-id", "Server: " + event.getGuild().getName() ).asDisabled()
                        ) )
                        .queue();
            }
        }
    }

    @Override
    public void onButtonInteraction( @NotNull ButtonInteractionEvent event )
    {

        if ( !isValidInteraction( event ) )
        {
            return;
        }

        if ( reminderMap.get(event.getMember().getId()).isEmpty() )
        {
            event.replyEmbeds( new EmbedBuilder()
                    .setColor( Color.red )
                    .setTitle( "Error" )
                    .setDescription( "You have no reminders set." )
                    .build() )
                    .setEphemeral( true )
                    .queue();
            return;
        }

        List<String> list = new ArrayList<>( reminderMap.get( event.getMember().getId() ) );

        switch ( event.getComponentId() )
        {
            // List Command
            case "previous" ->
            {

                // If not on first page
                if ( min != 0 )
                {

                    min -= 5;
                    max -= 5;

                    EmbedBuilder builder = reminderList( min, max, list );

                    // If new previous page is first page
                    if ( min == 0 )
                    {
                        event.editMessageEmbeds( builder.build() ).setActionRow(
                                Button.secondary( "previous", "Previous" ).asDisabled(),
                                Button.secondary( "next", "Next" ).asEnabled()
                        ).queue();
                    }
                    else
                    {
                        event.editMessageEmbeds( builder.build() ).setActionRow(
                                Button.secondary( "previous", "Previous" ).asEnabled(),
                                Button.secondary( "next", "Next" ).asEnabled()
                        ).queue();
                    }
                }
                // If on first page
                else
                {

                    EmbedBuilder builder = reminderList( min, max, list );

                    // If there is a next page
                    if ( list.size() <= 5 )
                    {

                        event.editMessageEmbeds( builder.build() ).setActionRow(
                                Button.secondary( "previous", "Previous" ).asDisabled(),
                                Button.secondary( "next", "Next" ).asEnabled()
                        ).queue();
                    }
                    else
                    {

                        event.editMessageEmbeds( builder.build() ).setActionRow(
                                Button.secondary( "previous", "Previous" ).asDisabled(),
                                Button.secondary( "next", "Next" ).asDisabled()
                        ).queue();
                    }
                }
            }
            case "next" ->
            {

                min += 5;
                max += 5;

                EmbedBuilder builder = reminderList( min, max, list );

                if ( max >= list.size() )
                {
                    event.editMessageEmbeds( builder.build() ).setActionRow(
                            Button.secondary( "previous", "Previous" ).asEnabled(),
                            Button.secondary( "next", "Next" ).asDisabled()
                    ).queue();
                }
                else
                {
                    event.editMessageEmbeds( builder.build() ).setActionRow(
                            Button.secondary( "previous", "Previous" ).asEnabled(),
                            Button.secondary( "next", "Next" ).asEnabled()
                    ).queue();
                }

            }

            // Confirmation
            case "reset" ->
            {

                event.deferEdit().queue();
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor( Color.green )
                        .setTitle( "Reminders reset" );

                reminderMap.get( event.getMember().getId() ).clear();

                try
                {
                    Database.resetTriggers( event.getMember(), reminderMap, notificationToggle );
                }
                catch ( SQLException e )
                {
                    log.error( "Failed to reset user's reminders", e );
                    EmbedBuilder error = new EmbedBuilder()
                            .setColor( Color.red )
                            .setTitle( "Error" )
                            .setDescription( "An error occurred while resetting your reminders. Please try again later." );

                    event.getHook().editOriginalEmbeds( error.build() ).queue();
                    return;
                }

                event.getHook().editOriginalEmbeds( builder.build() ).setActionRow(
                        Button.danger( "reset", "Yes" ).asDisabled()
                ).queue();
            }
        }
    }

    /**
     * Checks if Set contains String
     *
     * @param str String
     * @param set Containing Set
     * @return True if set contains String
     */
    boolean inSet( String str, LinkedHashSet<String> set )
    {
        for ( String string : set )
        {
            if ( str.equals( string ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if interaction is valid
     *
     * @param event Interaction/MessageReceived event
     * @return True if interaction is valid
     */
    boolean isValidInteraction( GenericEvent event )
    {

        if ( event instanceof SlashCommandInteractionEvent slashCommandInteractionEvent )
        {
            return slashCommandInteractionEvent.getMember() != null && slashCommandInteractionEvent.isGuildCommand();
        }
        else if ( event instanceof MessageReceivedEvent messageReceivedEvent )
        {

            return messageReceivedEvent.getMember() != null
                    && messageReceivedEvent.isFromGuild()
                    && !messageReceivedEvent.getMember().getUser().isBot();
        }
        else if ( event instanceof ButtonInteractionEvent buttonInteractionEvent )
        {
            return buttonInteractionEvent.getMember() != null && buttonInteractionEvent.isFromGuild();
        }
        else
        {
            return false;
        }
    }

    /**
     * Template Embed for /notifi list
     *
     * <p>
     * Formats EmbedBuilder containing reminders from range1 to range2
     * </p>
     *
     * @param range1 Beginning list index
     * @param range2 Ending list index
     * @param list   List of member triggers
     * @return Embed Message
     */
    EmbedBuilder reminderList( int range1, int range2, List<String> list )
    {

        EmbedBuilder builder = new EmbedBuilder()
                .setColor( Color.green )
                .setTitle( "Reminder List" )
                .setFooter( "Size: #" + list.size() );

        for ( int i = range1; i < list.size() && i < range2; ++i )
        {
            builder.addField( "Reminder #" + ( i + 1 ), list.get( i ), false );
        }

        return builder;
    }

}