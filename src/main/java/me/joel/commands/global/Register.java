package me.joel.commands.global;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        List<CommandData> globalCommandData = new ArrayList<>();

        SubcommandData truth = new SubcommandData("truth", "Generates a random truth question");
        SubcommandData dare = new SubcommandData("dare", "Generates a random dare question");
        SubcommandData random = new SubcommandData("random", "Generates a random question");

        globalCommandData.add(Commands.slash("help", "Lists commands"));
        globalCommandData.add(Commands.slash("8ball", "Asks the magic 8ball a question").addOption(OptionType.STRING, "question", "Your question to the 8ball", true));
        globalCommandData.add(Commands.slash("coinflip", "Flips a coin for heads or tails"));
        globalCommandData.add(Commands.slash("truthordare", "Generates a random truth/dare question").addSubcommands(truth, dare, random));
        globalCommandData.add(Commands.slash("ping", "Sends pong"));
        globalCommandData.add(Commands.slash("avatar", "Sends user avatar").addOption(OptionType.MENTIONABLE, "user", "Sends mentioned users avatar", true));
        globalCommandData.add(Commands.slash("blackjack", "Blackjack card game").addOption(OptionType.INTEGER, "bet", "Set bet amount, default to 100 otherwise"));

        event.getJDA().addEventListener(new Avatar(), new Ball8(), new CoinFlip(), new Help(), new Ping(), new TruthOrDare());
        event.getJDA().updateCommands().addCommands(globalCommandData).complete();
    }
}
