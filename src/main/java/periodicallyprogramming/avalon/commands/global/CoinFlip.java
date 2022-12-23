package periodicallyprogramming.avalon.commands.global;

import periodicallyprogramming.avalon.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CoinFlip extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("coinflip")) {
            String flip;
            if (Util.randomWithRange(0, 100) > 50) flip = "Heads!";
            else flip = "Tails!";

            EmbedBuilder coin = new EmbedBuilder()
                    .setDescription(flip)
                    .setColor(Util.randColor());

            event.replyEmbeds(coin.build()).queue();
        }
    }
}
