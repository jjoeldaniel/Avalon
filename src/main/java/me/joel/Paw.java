package me.joel;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Paw
{
    public static void main(String[] args) throws LoginException, InterruptedException
    {

        JDA jda = JDABuilder.createDefault("token")

                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new Commands())
                .addEventListeners(new ReactMessages())
                .addEventListeners(new MusicCommands())
                .addEventListeners(new ModCommands())
                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build()
                .awaitReady();

        // Sets status as # of guilds bot is member of
        int guildNum = jda.getGuilds().size();
        jda.getPresence().setActivity(Activity.listening(" " + (guildNum) + " servers!" ));
        //jda.updateCommands().queue();

        // Help
        jda.upsertCommand("help", "Lists commands").queue();
        // 8Ball
        jda.upsertCommand("8ball", "Asks the magic 8ball a question")
                .addOption(OptionType.STRING, "question", "Your question to the 8ball", true)
                .queue();
        // Coin Flip
        jda.upsertCommand("coinflip", "Flips a coin for heads or tails").queue();
        // Invite
        jda.upsertCommand("invite", "Returns bot invite link").queue();
        // Truth or Dare
        SubcommandData truth = new SubcommandData("truth", "Generates a random truth question");
        SubcommandData dare = new SubcommandData("dare", "Generates a random dare question");
        SubcommandData random = new SubcommandData("random", "Generates a random question");
        jda.upsertCommand("truthordare", "Generates a random truth/dare question")
                .addSubcommands(truth)
                .addSubcommands(dare)
                .addSubcommands(random)
                .queue();
        // Ping
        jda.upsertCommand("ping", "Sends pong").queue();
        // Avatar
        jda.upsertCommand("avatar", "Sends user avatar")
                .addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true)
                .queue();

    }

}