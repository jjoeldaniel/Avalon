package me.joel.commands.global;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TruthOrDare extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("truthordare")) {

            if (Objects.equals(event.getSubcommandName(), "truth")) {

                EmbedBuilder builder = me.joel.TruthOrDare.getTruth();

                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.success("truth", "Truth"),
                                Button.success("dare", "Dare"),
                                Button.danger("randomtruthordare", "Random")
                        )
                        .queue();
            } else if (Objects.equals(event.getSubcommandName(), "dare")) {

                EmbedBuilder builder = me.joel.TruthOrDare.getDare();

                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.success("truth", "Truth"),
                                Button.success("dare", "Dare"),
                                Button.danger("randomtruthordare", "Random")
                        )
                        .queue();
            } else if (Objects.equals(event.getSubcommandName(), "random")) {

                if (Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = me.joel.TruthOrDare.getDare();

                    event.replyEmbeds(builder.build())
                            .addActionRow(
                                    Button.success("truth", "Truth"),
                                    Button.success("dare", "Dare"),
                                    Button.danger("randomtruthordare", "Random")
                            )
                            .queue();
                } else {
                    EmbedBuilder builder = me.joel.TruthOrDare.getTruth();

                    event.replyEmbeds(builder.build())
                            .addActionRow(
                                    Button.success("truth", "Truth"),
                                    Button.success("dare", "Dare"),
                                    Button.danger("randomtruthordare", "Random")
                            )
                            .queue();
                }
            }
        }
    }
}
