package me.joel.games.Blackjack;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Blackjack extends ListenerAdapter {

    User user;
    String messageID;

    Button rules = Button.primary("rules", "See Rules");
    Button play = Button.success("play", "Play");
    Button menu = Button.primary("menu", "Menu");

    public EmbedBuilder menu() {
        return new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("Blackjack Menu (WIP)")
                .setDescription("Press one of the buttons below to start playing!")
                .setColor(Util.randColor());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();
        user = event.getUser();

        if (invoke.equalsIgnoreCase("blackjack")) {

            EmbedBuilder builder = menu();
            event.replyEmbeds(builder.build()).addActionRow(rules, menu.asDisabled(), play).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        var invoke = event.getComponentId();
        messageID = event.getMessageId();
        System.out.println(invoke);

        // If interaction member != /blackjack member
        if (event.getUser() != user) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("You can't interact with another members game!")
                    .setColor(Util.randColor());

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        switch (invoke) {
            case ("rules") -> {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Rules")
                        .addField("The premise of the game is simple..", "The first player to 21 wins.\nWatch out though, if you go over 21, you lose!", false)
                        .addField("Special card values are as follows:", "Ace = 1/11, Jack, King, and Queen are 10", false)
                        .setColor(Util.randColor());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(rules.asDisabled(), menu.asEnabled(), play.asEnabled())
                        .queue();
            }
            case ("play") -> {
                String card1 = Deck.randomCard();
                String card2 = Deck.randomCard();
                int total = Deck.deck.get(card1) + Deck.deck.get(card2);

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("You start with 2 cards")
                        .setDescription("Your total is " + total)
                        .addField("Card 1: " + card1, "", true)
                        .addField("Card 2: " + card2, "", true)
                        .setColor(Util.randColor());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(rules.asDisabled(), play.asDisabled(), menu.asEnabled())
                        .queue();

                if (total == 21) {
                    event.getHook().editMessageEmbedsById(messageID, builder.build())
                            .setActionRow(rules.asDisabled(), play.asDisabled(), menu.asDisabled())
                            .queue();

                    EmbedBuilder hit21 = new EmbedBuilder()
                            .setTitle("Blackjack!")
                            .setDescription("You have won 100 credits")
                            .setColor(Util.randColor());

                    event.getHook().editMessageEmbedsById(messageID, hit21.build())
                            .setActionRow(rules.asEnabled(), menu.asEnabled(), play.asEnabled())
                            .queueAfter(3, TimeUnit.SECONDS);
                }
            }
            case ("menu") -> {
                EmbedBuilder builder = menu();
                event.editMessageEmbeds(builder.build()).queue();
            }
        }
    }
}
