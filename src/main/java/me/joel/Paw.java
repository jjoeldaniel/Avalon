package me.joel;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Paw {
    public static void main(String[] args) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault("OTcxMjM5NDM4ODkyMDE5NzQz.GHpHqT.uOTGGrIGK2fFj2RFQE9GllzgLGMQ8EjMyzzL1Q")
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new Commands(), new ModCommands(), new MusicCommands())
                .addEventListeners(new ReactMessages())
                .addEventListeners(new CommandsRegister())
                .addEventListeners(new TestServer())
                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        jda.getPresence().setActivity(Activity.listening(" " + (jda.getGuilds().size()) + " servers!"));
    }

}