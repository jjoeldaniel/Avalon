package me.joel;

import javax.security.auth.login.LoginException;

import me.joel.commands.guild.*;
import me.joel.commands.mod.*;
import me.joel.commands.music.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Avalon {
    public static void main(String[] args) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault("OTcxMjM5NDM4ODkyMDE5NzQz.GHpHqT.uOTGGrIGK2fFj2RFQE9GllzgLGMQ8EjMyzzL1Q")
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new Commands())
                .addEventListeners(new GuildEvents())
                .addEventListeners(new Purge(), new Broadcast())
                .addEventListeners(new AFK(), new Confess(), new WhoIs())
                .addEventListeners(new Play(),new Resume(), new Pause(), new Skip(), new Volume(), new Queue(), new Clear(), new Playing(), new Loop(), new me.joel.commands.music.Util())
                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        jda.getPresence().setActivity(Activity.listening(" " + (jda.getGuilds().size()) + " servers!"));
    }

}