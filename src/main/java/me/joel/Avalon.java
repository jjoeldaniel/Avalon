package me.joel;

import java.sql.SQLException;

import me.joel.commands.Register;
import me.joel.commands.global.*;
import me.joel.commands.global.TruthOrDare;
import me.joel.commands.guild.Join;
import me.joel.commands.guild.Leave;
import me.joel.commands.guild.Mock;
import me.joel.commands.guild.Translate;
import me.joel.commands.guild.WhoIs;
import me.joel.commands.mod.*;
import me.joel.commands.music.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Avalon
{
    public static void main( String[] args ) throws InterruptedException
    {

        // SL4FJ Logger
        final Logger log = LoggerFactory.getLogger( Avalon.class );

        try
        {
            Database.initialize();
            me.joel.commands.reminders.Database.initialize();
        }
        catch ( SQLException e )
        {
            log.error( "Error connecting/initializing database", e );
        }

        final String token = System.getenv( "DISCORD_TOKEN" );

        JDA jda = JDABuilder.createDefault( token )
                .setStatus( OnlineStatus.ONLINE )

                // Events
                .addEventListeners(
                        new GuildEvents(),
                        new Register(),
                        new ReactMessages()
                )
                // Global
                .addEventListeners(
                        new Avatar(),
                        new Ball8(),
                        new Help(),
                        new Ping(),
                        new TruthOrDare(),
                        new CoinFlip()
                )
                // Mod
                .addEventListeners(
                        new Purge(),
                        new Poll(),
                        new Broadcast()
                )
                // Guild
                .addEventListeners(
                        new WhoIs(),
                        new Join(),
                        new Leave(),
                        new Translate(),
                        new Toggle(),
                        new Mock(),
                        new GuildSettings()
                )
                // Music
                .addEventListeners(
                        new me.joel.commands.music.Activity(),
                        new Play(),
                        new Resume(), new Pause(),
                        new Skip(),
                        new Shuffle(),
                        new Volume(),
                        new Queue(),
                        new Clear(),
                        new Seek(),
                        new Playing(),
                        new Loop(),
                        new me.joel.commands.music.Util(),
                        new Lyrics()
                )
                // Reminders
                .addEventListeners(
                        new me.joel.commands.reminders.Reminder()
                )

                .enableCache( CacheFlag.VOICE_STATE )
                .enableIntents( GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS )
                .setMemberCachePolicy( MemberCachePolicy.ALL )

                .build()
                .awaitReady();

        // Status = # of members for all guilds
        int numOfMembers = 0;
        for ( Guild guild : jda.getGuilds() )
        {
            numOfMembers += guild.getMemberCount();
        }

        jda.getPresence().setActivity( Activity.listening( " " + numOfMembers + " members!" ) );
    }

}