package me.joel;

import javax.security.auth.login.LoginException;

import me.joel.commands.music.Play;
import me.joel.commands.music.Resume;
import me.joel.commands.music.Skip;
import me.joel.commands.music.Volume;
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
                .addEventListeners(new Commands(), new MusicCommands())
                .addEventListeners(new GuildEvents())
                .addEventListeners(new Play(),new Resume(), new Skip(), new Volume())
                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        jda.getPresence().setActivity(Activity.listening(" " + (jda.getGuilds().size()) + " servers!"));
    }

}