package me.joel;

import javax.security.auth.login.LoginException;

import me.joel.commands.Register;
import me.joel.commands.global.*;
import me.joel.commands.global.TruthOrDare;
import me.joel.commands.guild.*;
import me.joel.commands.mod.*;
import me.joel.commands.music.*;
import me.joel.games.Blackjack.Blackjack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Avalon {
    public static void main(String[] args) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault("OTcxMjM5NDM4ODkyMDE5NzQz.G0_CMv.0gpt8AWNd9V1RdU97Fub9d2tJFrzmYpCXZf-u8")
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
                        new Blackjack()
                )
                // Mod
                .addEventListeners(
                        new Purge(),
                        new Broadcast()
                )
                // Guild
                .addEventListeners(
                        new AFK(),
                        new Confess(),
                        new WhoIs(),
                        new Join(),
                        new Leave(),
                        new Translate()
                )
                // Music
                .addEventListeners(
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
                        new me.joel.commands.music.Util()
                )

                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)

                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        jda.getPresence().setActivity(Activity.listening(" " + (jda.getGuilds().size()) + " servers!"));
    }

}