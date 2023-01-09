package me.joel;

import me.joel.commands.guild_config.GuildSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class GuildEvents extends ListenerAdapter
{

    final Logger log = LoggerFactory.getLogger( GuildEvents.class );

    public static HashMap<Guild, HashMap<Integer, Member>> confession_record = new HashMap<>();
    public static HashMap<Guild, HashMap<Integer, String>> message_record = new HashMap<>();

    @Override
    public void onReady( @NotNull ReadyEvent event )
    {
        log.info( "Active Bot: " + event.getJDA().getSelfUser().getName() );
    }

    @Override
    public void onGuildLeave( @NotNull GuildLeaveEvent event )
    {
        log.info( "Left server: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")" );
    }

    @Override
    public void onGuildJoin( @NotNull GuildJoinEvent event )
    {
        log.info( "Joined server: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")" );

        final String inviteLink =
                "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot";

        EmbedBuilder builder = new EmbedBuilder()
                .setThumbnail( event.getJDA().getSelfUser().getAvatarUrl() )
                .setTitle( "Thank you for inviting Avalon to " + event.getGuild().getName() + "!" )
                .setColor( Util.randColor() )
                .setDescription( "Make sure to use /help to get the full commands list!" )
                .addBlankField( false )
                .addField( "Need to contact us?", "Add joel#0005 on Discord for questions!", false )
                .addField( "Want to invite Avalon to another server?",
                        "Click on my profile and click \" Add to Server\" to invite Avalon!", false );

        TextChannel channel = event.getGuild().getSystemChannel();

        // Default to "general" channel if no system channel
        if ( channel == null )
        {
            String generalID = Util.findChannel( "general", event.getGuild() );

            if ( generalID != null )
            {
                event.getGuild().getTextChannelById( generalID ).sendMessageEmbeds( builder.build() ).setActionRow(
                        Button.link( inviteLink, "Invite" ) ).queue( null, null );
            }
        }
        else
        {
            channel.sendMessageEmbeds( builder.build() ).setActionRow(
                    Button.link( inviteLink, "Invite" ) ).queue( null, null );
        }

        // Initializes guild settings
        try
        {
            Connection conn = Database.getConnect();
            String sql = "INSERT INTO \"public\".\"guild_settings\"(guild_id, insults, gm_gn, now_playing) VALUES (" + event.getGuild()
                    .getId() + ", 1, 1, 1)";
            String sql2 = "INSERT INTO \"public\".\"starboard_settings\"(guild_id, star_limit, star_self) VALUES (" + event.getGuild()
                    .getId() + "), 3, 0";

            conn.createStatement().execute( sql );
            conn.createStatement().execute( sql2 );
        }
        catch ( SQLException e )
        {
            log.error(
                    "Failed to first-time-initialize guild settings for guild: " + event.getGuild().getName() + " ("
                            + event.getGuild().getId() + ")" );
        }
    }

    @Override
    public void onGuildReady( @NotNull GuildReadyEvent event )
    {
        Guild guild = event.getGuild();

        // Initialize confessions
        HashMap<Integer, Member> map = new HashMap<>();
        confession_record.put( guild, map );

        HashMap<Integer, String> map2 = new HashMap<>();
        message_record.put( guild, map2 );

        // Initializes guild settings if nothing found
        try
        {
            String sql = "SELECT * FROM \"public\".\"guild_settings\" WHERE guild_id=" +guild.getId();
            ResultSet set = Database.getConnect().createStatement().executeQuery( sql );

            sql = "SELECT * FROM \"public\".\"starboard_settings\" WHERE guild_id=" +guild.getId();
            ResultSet set2 = Database.getConnect().createStatement().executeQuery( sql );

            if ( !set.next() )
            {
                String sql2 =
                        "INSERT INTO \"public\".\"guild_settings\"(guild_id, insults, gm_gn, now_playing) VALUES (" + guild
                                .getId() + ", 1, 1, 1)";
                Database.getConnect().createStatement().execute( sql2 );
            }
            if ( !set2.next() )
            {
                String sql2 =
                        "INSERT INTO \"public\".\"starboard_settings\"(guild_id, star_limit, star_self) VALUES (" + guild
                                .getId() + ", 3, 0)";
                Database.getConnect().createStatement().execute( sql2 );
            }
            // Syncs settings here
            else
            {
                var confession_channel = set.getLong(2);
                var join_channel = set.getLong(3);
                var leave_channel = set.getLong(4);
                var mod_channel = set.getLong(5);

                // confession channel
                if (set.getLong(2) != 0) {
                    GuildSettings.confession_channel.put(guild, confession_channel);
                }
                // join channel
                if (set.getLong(3) != 0) {
                    GuildSettings.join_channel.put(guild, join_channel);
                }
                // leave channel
                if (set.getLong(4) != 0) {
                    GuildSettings.leave_channel.put(guild, leave_channel);
                }
                // mod channel
                if (set.getLong(5) != 0) {
                    GuildSettings.mod_channel.put(guild, mod_channel);
                }

                // insults
                GuildSettings.insults.put(guild, set.getBoolean(6));
                // gm gn
                GuildSettings.gm_gn.put(guild, set.getBoolean(7));
                // now playing
                GuildSettings.now_playing.put(guild, set.getBoolean(8));

                // starboard channel
                set2.next();

                var starboard_channel = set2.getLong(2);
                
                if (set2.getLong(2) != 0) {
                    GuildSettings.starboard_channel.put(guild, starboard_channel);
                }

                // star limit
                GuildSettings.starboard_limit.put(guild, set2.getInt(3));
                // star self
                GuildSettings.starboard_self.put(guild, set2.getBoolean(4));
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            log.error( "Failed to initialize guild settings for guild: " + event.getGuild().getName() + " ("
                    + event.getGuild().getId() + ")" );
        }
    }

    @Override
    public void onGuildMemberJoin( @NotNull GuildMemberJoinEvent event )
    {
        Member member = event.getMember();

        var channelID = GuildSettings.join_channel.get( event.getGuild() );
        if ( channelID == null ) return;

        TextChannel channel = event.getGuild().getTextChannelById( channelID );

        EmbedBuilder memberJoin = new EmbedBuilder()
                .setColor( Util.randColor() )
                .setTitle( "A new member has joined!" )
                .setDescription
                        (
                                "Welcome " + member.getAsMention() + " to " + event.getGuild().getName() +
                                        "! There are now " + event.getGuild().getMemberCount() + " members in "
                                        + event.getGuild().getName() + "."
                        )
                .setThumbnail( member.getEffectiveAvatarUrl() )
                .setFooter( "User: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " ID: "
                        + member.getId() );

        if ( event.getGuild().getSelfMember().hasPermission( channel, Permission.MESSAGE_SEND ) )
        {
            channel.sendMessageEmbeds( memberJoin.build() ).queue( ( null ), ( null ) );
        }
    }

    @Override
    public void onGuildMemberRemove( @NotNull GuildMemberRemoveEvent event )
    {

        var channelID = GuildSettings.leave_channel.get( event.getGuild() );
        if ( channelID == null ) return;

        TextChannel channel = event.getGuild().getTextChannelById( channelID );

        User user = event.getUser();
        EmbedBuilder memberLeave = new EmbedBuilder()
                .setColor( Util.randColor() )
                .setTitle( "A member has left!" )
                .setDescription
                        (
                                user.getAsMention() + " has left " + event.getGuild().getName() +
                                        "! There are now " + event.getGuild().getMemberCount() + " members in "
                                        + event.getGuild().getName() + "."
                        )
                .setThumbnail( user.getEffectiveAvatarUrl() )
                .setFooter( "User: " + user.getName() + "#" + user.getDiscriminator() + " ID: " + user.getId() );

        event.getGuild().retrieveAuditLogs().queueAfter( 1, TimeUnit.SECONDS, ( logs ) -> {
            boolean isBan = false, isKick = false;

            for ( AuditLogEntry log : logs )
            {
                if ( log.getTargetIdLong() == user.getIdLong() )
                {
                    isBan = log.getType() == ActionType.BAN;
                    isKick = log.getType() == ActionType.KICK;
                    break;
                }
            }

            if ( event.getGuild().getSelfMember().hasPermission( channel, Permission.MESSAGE_SEND ) )
            {
                if ( isBan )
                {
                    memberLeave.setTitle( "A member has been banned!" );
                    channel.sendMessageEmbeds( memberLeave.build() ).queue( ( null ), ( null ) );
                }
                else if ( isKick )
                {
                    memberLeave.setTitle( "A member has been kicked!" );
                    channel.sendMessageEmbeds( memberLeave.build() ).queue( ( null ), ( null ) );
                }
                else
                {
                    channel.sendMessageEmbeds( memberLeave.build() ).queue( ( null ), ( null ) );
                }
            }
        } );
    }
}
