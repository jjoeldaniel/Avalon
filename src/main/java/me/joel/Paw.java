package me.joel;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.ArrayList;
import java.util.List;

public class Paw {

    static int num = 0;

    public static void main(String[] args) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault("OTcxMjM5NDM4ODkyMDE5NzQz.Gamtz3.a7DObfsPstEU4B4g5GBWi4wYOQH-kwpg1j8fa0")

                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new Commands())
                .addEventListeners(new Insults())
                .addEventListeners(new ModCommands())
                .addEventListeners(new ReactMessages())
                .addEventListeners(new AFK())
                .enableCache(CacheFlag.VOICE_STATE)
                .build();

        jda.awaitReady();
        int guildNum = jda.getGuilds().size();
        jda.getPresence().setActivity(Activity.listening(" " + (guildNum) + " servers!" ));

        // FIXME: Add command parameters
        // Commands List
        jda.upsertCommand("help", "Command list").queue();
        jda.upsertCommand("8ball", "Asks the magic 8ball a question").queue();
        jda.upsertCommand("truth", "Generates a random truth/dare question").queue();
        jda.upsertCommand("dare", "Generates a random truth/dare question").queue();
        jda.upsertCommand("ping", "Sends pong").queue();
        jda.upsertCommand("bark", "Barks").queue();
        jda.upsertCommand("meow", "Meows").queue();

    }

}