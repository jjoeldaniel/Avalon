package me.joel;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Avalon {
    public static void main(String[] args) throws LoginException, InterruptedException {

        // Grab from properties
        final String token = Util.loadProperty("DISCORD_TOKEN");

        JDA jda = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)

                // Registers
                .addEventListeners(
                        new me.joel.commands.mod.Register(),
                        new me.joel.commands.guild.Register(),
                        new me.joel.commands.music.Register(),
                        new me.joel.commands.global.Register()
                )

                // Events
                .addEventListeners(
                        new GuildEvents(),
                        new ReactMessages()
                )

                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)

                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        jda.getPresence().setActivity(Activity.listening(" " + (jda.getGuilds().size()) + " servers!"));
    }

}