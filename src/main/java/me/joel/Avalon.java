package me.joel;

import io.github.cdimascio.dotenv.Dotenv;
import me.joel.commands.Register;
import me.joel.commands.global.*;
import me.joel.commands.global.TruthOrDare;
import me.joel.commands.guild.Confess;
import me.joel.commands.guild.Join;
import me.joel.commands.guild.Leave;
import me.joel.commands.guild.Mock;
import me.joel.commands.guild_config.Starboard;
import me.joel.commands.guild.Translate;
import me.joel.commands.guild.WhoIs;
import me.joel.commands.guild_config.GuildSettings;
import me.joel.commands.guild_config.Toggle;
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

public class Avalon
{
    public static void main( String[] args ) throws InterruptedException
    {

        // Grab from .env
        Dotenv dotenv = Dotenv.configure()
                .systemProperties()
                .load();

        final String token = System.getProperty( "DISCORD_TOKEN" );

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
                        new Confess(),
                        new WhoIs(),
                        new Join(),
                        new Leave(),
                        new Translate(),
                        new Toggle(),
                        new Mock(),
                        new GuildSettings(),
                        new Starboard()
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
                // Triggers
                .addEventListeners(
                        new me.joel.commands.reminders.Reminder(),
                        new me.joel.commands.reminders.Register()
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
        Database.connect();
    }

}