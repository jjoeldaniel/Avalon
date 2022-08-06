package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        var invoke = event.getComponentId();

        switch (invoke) {

            case ("truth") -> {
                EmbedBuilder builder = TruthOrDare.getTruth();

                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.success("truth", "Truth"),
                                Button.success("dare", "Dare"),
                                Button.danger("randomTruthOrDare", "Random")
                        )
                        .queue();
            }
            case ("dare") -> {
                EmbedBuilder builder = TruthOrDare.getDare();

                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.success("truth", "Truth"),
                                Button.success("dare", "Dare"),
                                Button.danger("randomTruthOrDare", "Random")
                        )
                        .queue();
            }
            case ("randomtruthordare") -> {
                if (Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = TruthOrDare.getDare();

                    event.replyEmbeds(builder.build())
                            .addActionRow(
                                    Button.success("truth", "Truth"),
                                    Button.success("dare", "Dare"),
                                    Button.danger("randomtruthordare", "Random")
                            )
                            .queue();
                }
                else {
                    EmbedBuilder builder = TruthOrDare.getTruth();

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
