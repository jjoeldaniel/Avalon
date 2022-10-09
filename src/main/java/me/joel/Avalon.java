package me.joel;

import javax.security.auth.login.LoginException;

import me.joel.commands.Register;
import me.joel.commands.global.*;
import me.joel.commands.global.TruthOrDare;
import me.joel.commands.guild.*;
import me.joel.commands.mod.*;
import me.joel.commands.music.*;
import me.joel.games.Bank;
import me.joel.games.Blackjack.Blackjack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Avalon {
    public static void main(String[] args) throws LoginException, InterruptedException {

        // Grab from properties
        final String token = Util.loadProperty("DISCORD_TOKEN");

        JDA jda = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)

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
                // Games
                .addEventListeners(
                        new Blackjack(),
                        new Bank()
                )
                // Mod
                .addEventListeners(
                        new Purge(),
                        new Poll(),
                        new Broadcast()
                )
                // Guild
                .addEventListeners(
                        new AFK(),
                        new Confess(),
                        new WhoIs(),
                        new Join(),
                        new Leave(),
                        new Translate(),
                        new Toggle()
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

                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)

                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        jda.getPresence().setActivity(Activity.listening(" " + (jda.getGuilds().size()) + " servers!"));
        Database.connect();
    }

}