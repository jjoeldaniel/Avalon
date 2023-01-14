package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.joel.commands.mod.GuildSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GuildEvents extends ListenerAdapter
{

    final Logger log = LoggerFactory.getLogger( GuildEvents.class );

    public static HashMap<Guild, HashMap<Integer, Member>> confession_record = new HashMap<>();
    public static HashMap<Guild, HashMap<Integer, String>> message_record = new HashMap<>();
    
    final private static String URL = System.getenv( "DATABASE_URL" );
    final private static String USER = System.getenv( "DATABASE_USER" );
    final private static String PASSWORD = System.getenv( "DATABASE_PASSWORD" );

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
        Guild guild = event.getGuild();

        log.info( "Joined server: " + guild.getName() + " (" + guild.getId() + ")" );

        final String inviteLink =
                "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot";

        EmbedBuilder builder = new EmbedBuilder()
                .setThumbnail( event.getJDA().getSelfUser().getAvatarUrl() )
                .setTitle( "Thank you for inviting Avalon to " + guild.getName() + "!" )
                .setColor( Util.randColor() )
                .setDescription( "Make sure to use /help to get the full commands list!" )
                .addBlankField( false )
                .addField( "Need to contact us?", "Add joel#0005 on Discord for questions!", false )
                .addField( "Want to invite Avalon to another server?",
                        "Click on my profile and click \" Add to Server\" to invite Avalon!", false );

        TextChannel channel = guild.getSystemChannel();

        // Default to "general" channel if no system channel
        if ( channel == null )
        {
            String generalID = Util.findChannel( "general", event.getGuild() );

            if ( generalID != null )
            {
                guild.getTextChannelById( generalID ).sendMessageEmbeds( builder.build() ).setActionRow(
                        Button.link( inviteLink, "Invite" ) ).queue( null, null );
            }
        }
        else
        {
            channel.sendMessageEmbeds( builder.build() ).setActionRow(
                    Button.link( inviteLink, "Invite" ) ).queue( null, null );
        }

        // Initializes guild settings if nothing found
        try
        {
            String sql = "SELECT * FROM \"public\".\"guild_settings\" WHERE guild_id=" +guild.getId();

            try ( Connection conn = DriverManager.getConnection( URL, USER, PASSWORD ) ) {
                ResultSet set = conn.createStatement().executeQuery( sql );

                if ( !set.next() )
                {
                    String sql2 =
                            "INSERT INTO \"public\".\"guild_settings\" VALUES (" + guild
                                    .getId() + ", TRUE, TRUE, TRUE)";
                    conn.createStatement().execute( sql2 );

                    // insults
                    GuildSettings.insults.put(guild, true);
                    // gm gn
                    GuildSettings.gm_gn.put(guild, true);
                    // now playing
                    GuildSettings.now_playing.put(guild, true);
                }
                // Syncs settings here
                else
                {

                    // insults
                    GuildSettings.insults.put(guild, set.getBoolean(2));
                    // gm gn
                    GuildSettings.gm_gn.put(guild, set.getBoolean(3));
                    // now playing
                    GuildSettings.now_playing.put(guild, set.getBoolean(4));
                }
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
    public void onGuildReady( @NotNull GuildReadyEvent event )
    {
        Guild guild = event.getGuild();

        // Initializes guild settings if nothing found
        try
        {
            String sql = "SELECT * FROM \"public\".\"guild_settings\" WHERE guild_id=" +guild.getId();

            try ( Connection conn = DriverManager.getConnection( URL, USER, PASSWORD ) ) {
                ResultSet set = conn.createStatement().executeQuery( sql );

                if ( !set.next() )
                {
                    String sql2 =
                            "INSERT INTO \"public\".\"guild_settings\" VALUES (" + guild
                                    .getId() + ", TRUE, TRUE, TRUE)";
                    conn.createStatement().execute( sql2 );

                    // insults
                    GuildSettings.insults.put(guild, true);
                    // gm gn
                    GuildSettings.gm_gn.put(guild, true);
                    // now playing
                    GuildSettings.now_playing.put(guild, true);
                }
                // Syncs settings here
                else
                {

                    // insults
                    GuildSettings.insults.put(guild, set.getBoolean(2));
                    // gm gn
                    GuildSettings.gm_gn.put(guild, set.getBoolean(3));
                    // now playing
                    GuildSettings.now_playing.put(guild, set.getBoolean(4));
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            log.error( "Failed to initialize guild settings for guild: " + event.getGuild().getName() + " ("
                    + event.getGuild().getId() + ")" );
        }
    }
}
