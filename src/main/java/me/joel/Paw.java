package me.joel;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Paw {

    public static void main(String[] args) throws LoginException {

        JDA jda = JDABuilder.createDefault("OTcxMjM5NDM4ODkyMDE5NzQz.Gzoczt.OqNz_8_x-K5FZDX1tzy_UK8XJyUGYQgvnlud3E")
                .setActivity(Activity.playing("ur mom"))
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new Commands())
                .addEventListeners(new Insults())
                .addEventListeners(new TruthOrDare())
                .addEventListeners(new ModCommands())
                .addEventListeners(new ReactMessages())
                .addEventListeners(new AFK())
                .addEventListeners(new FunCommands())
                .build();

    }

}