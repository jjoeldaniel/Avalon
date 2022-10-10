package me.joel.games.Blackjack;

import me.joel.Console;
import me.joel.Database;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Blackjack extends ListenerAdapter {

    User user;
    String messageID;
    int total = 0;
    int bet = 100;
    int profit = 125;

    Button rules = Button.primary("rules", "See Rules");
    Button play = Button.success("play", "Play");
    Button menu = Button.primary("menu", "Menu");
    Button hit = Button.success("hit", "Hit");
    Button stand = Button.danger("stand", "Stand");

    public EmbedBuilder menu() {
        return new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("Blackjack Menu")
                .setDescription("Press one of the buttons below to start playing!")
                .setColor(Util.randColor());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equalsIgnoreCase("blackjack")) {
            user = event.getUser();

            // Check wallet
            try {
                Database.getWallet(user.getId());
            } catch (SQLException e) {
                Console.warn("Failed to retrieve user balance");
                e.printStackTrace();
            }

            // get bet option
            if (event.getOption("bet") != null) {
                bet = event.getOption("bet").getAsInt();

                // resets to 100
                if (bet <= 0) bet = 100;

                // profit is 125% of bet
                profit = (int) (bet * 1.25);
            }

            EmbedBuilder builder = menu();
            event.replyEmbeds(builder.build())
                    .setActionRow(rules, menu.asDisabled(), play)
                    .queue(null, (exception) -> {
                        Console.warn(exception.getMessage());
                    }
            );
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        var invoke = event.getComponentId();
        messageID = event.getMessageId();


        if (invoke.equals("rules") || invoke.equals("play") || invoke.equals("stand") || invoke.equals("menu")) {
            // If interaction member != /blackjack member
            if (event.getUser() != user) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("You can't interact with another members game!")
                        .setColor(Color.red);

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }
        }

        switch (invoke) {
            case ("rules") -> {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Rules")
                        .addField("The premise of the game is simple..", """
                                The first player to 21 wins.
                                After receiving your cards, you can decide to hit or stand.\s
                                Watch out though, if you go over 21, you lose!""", false)
                        .addField("Special card values are as follows:", "Ace = 1/11, Jack, King, and Queen are 10", false)
                        .setColor(Util.randColor());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(rules.asDisabled(), menu.asEnabled(), play.asEnabled())
                        .queue();
            }
            case ("play") -> {
                String card1 = Deck.randomCard();
                total = Deck.deck.get(card1);

                String card2 = Deck.randomCard();
                // check for second ace
                if (card2.equalsIgnoreCase("ace")) {
                    if (total + Deck.deck.get(card2) > 21) {
                        total += 1;
                    }
                }
                else {
                    total += Deck.deck.get(card2);
                }

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("You start with 2 cards")
                        .setDescription("Your total is " + total)
                        .addField("Card 1:", card1, true)
                        .addField("Card 2:", card2, true)
                        .setColor(Util.randColor());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(hit.asEnabled(), stand.asEnabled(), menu.asDisabled())
                        .queue(null, (exception) -> {
                            Console.warn(exception.getMessage());
                        });

                // blackjack
                if (total == 21) {
                    event.getHook().editMessageEmbedsById(messageID, builder.build())
                            .setActionRow(hit.asDisabled(), stand.asDisabled(), menu.asDisabled())
                            .queue();

                    EmbedBuilder hit21 = new EmbedBuilder()
                            .setTitle("Blackjack!")
                            .setDescription("You have won `" + (profit*1.1) + "` credits")
                            .setColor(Color.green);

                    profit = (int) (profit*1.1);

                    try {
                        Database.modifyWallet(event.getUser().getId(), profit);
                        Console.log("Player wins");
                    } catch (SQLException e) {
                        Console.warn("Failed to modify user balance");
                    }

                    event.editMessageEmbeds(hit21.build())
                            .setActionRow(rules.asEnabled(), menu.asEnabled(), play.asEnabled())
                            .queueAfter(3, TimeUnit.SECONDS);
                }
            }
            case ("hit") -> {
                String card = Deck.randomCard();

                if (card.equalsIgnoreCase("ace")) {
                    if (total + Deck.deck.get(card) > 21) {
                        total += 1;
                    }
                }
                else {
                    total += Deck.deck.get(card);
                }

                EmbedBuilder builder = new EmbedBuilder();

                // bust
                if (total > 21) {
                    builder
                            .setTitle("Result: You bust!")
                            .setDescription("You have lost `" + bet + "` credits.")
                            .addField("Total", "Your total was `" + total + "`.", false)
                            .addField("You received:", card, false)
                            .setColor(Color.red);

                    event.editMessageEmbeds(builder.build())
                            .setActionRow(menu.asEnabled())
                            .queue(null, (exception) -> {
                                Console.warn(exception.getMessage());
                            });

                    try {
                        Database.modifyWallet(event.getUser().getId(), -bet);
                        Console.log("Player loses");
                    } catch (SQLException e) {
                        Console.warn("Failed to modify user balance");
                    }

                    return;
                }
                else if (total == 21) {
                    builder
                            .setTitle("Result: You won!")
                            .setDescription("You have won `" + profit + "` credits")
                            .addField("You received:", card, false)
                            .setColor(Color.green);

                    event.editMessageEmbeds(builder.build())
                            .setActionRow(menu.asEnabled())
                            .queue(null, (exception) -> {
                                Console.warn(exception.getMessage());
                            });

                    try {
                        Database.modifyWallet(event.getUser().getId(), profit);
                        Console.log("Player wins");
                    } catch (SQLException e) {
                        Console.warn("Failed to modify user balance");
                    }

                    return;
                }

                builder = new EmbedBuilder()
                        .setTitle("Hit")
                        .setDescription("Your new total is `" + total + "`.")
                        .addField("You received:", card, false)
                        .setColor(Util.randColor());

                event.editMessageEmbeds(builder.build())
                        .setActionRow(hit.asEnabled(), stand.asEnabled(), menu.asEnabled())
                        .queue(null, (exception) -> {
                            Console.warn(exception.getMessage());
                        });

            }
            case ("stand") -> {
                int dealerTotal = Deck.deck.get(Deck.randomCard()) + Deck.deck.get(Deck.randomCard());

                // hit if 11 or under
                if (dealerTotal <= 11) {
                    dealerTotal += Deck.deck.get(Deck.randomCard());
                }

                // hit if <= 16
                else if (dealerTotal <= 16) {
                    int r = Util.randomWithRange(0,100);
                    dealerTotal += Deck.deck.get(Deck.randomCard());
                }

                // decide if dealer wil hit/stand on 16+
                if (dealerTotal > 16 && dealerTotal < 20) {
                    int r = Util.randomWithRange(0, 100);

                    // hit, else stand
                    if (r > 75) {
                        dealerTotal += Deck.deck.get(Deck.randomCard());
                    }
                }

                EmbedBuilder builder = new EmbedBuilder()
                        .addField("Dealer", "The dealer had `" + dealerTotal + "`.", false)
                        .addField("Your total", "`" + total + "`" , false)
                        .setDescription("You have won `" + profit + "` credits.")
                        .setTitle("Result: You won!")
                        .setColor(Color.green);

                if (dealerTotal > total && dealerTotal <= 21) {
                    builder.setTitle("Result: You lost!");
                    builder.setDescription("You have lost `" + bet + "` credits.");
                    builder.setColor(Color.red);

                    try {
                        Database.modifyWallet(event.getUser().getId(), -bet);
                        Console.log("Player loses");
                    } catch (SQLException e) {
                        Console.warn("Failed to modify user balance");
                    }
                }
                else if (dealerTotal > 21 && total <= 21) {
                    builder.clearFields().addField("Dealer bust!", "The dealer had `" + dealerTotal + "`.", false);
                    builder.setDescription("You have won `" + profit + "` credits");

                    try {
                        Database.modifyWallet(event.getUser().getId(), profit);
                        Console.log("Player wins");
                    } catch (SQLException e) {
                        Console.warn("Failed to modify user balance");
                    }
                }
                else if (dealerTotal == total) {
                    builder.setTitle("Result: You tied!");
                    builder.setColor(Color.gray);
                    builder.setDescription("Your bet of `" + bet + "` has been returned to you");

                    event.editMessageEmbeds(builder.build())
                            .setActionRow(menu)
                            .queue(null, (exception) -> {
                                Console.warn(exception.getMessage());
                            });

                    return;
                }

                event.editMessageEmbeds(builder.build())
                        .setActionRow(menu)
                        .queue(null, (exception) -> {
                            Console.warn(exception.getMessage());
                        });

            }
            case ("menu") -> {
                EmbedBuilder builder = menu();
                event.editMessageEmbeds(builder.build())
                        .setActionRow(rules, menu.asDisabled(), play)
                        .queue(null, (exception) -> {
                            Console.warn(exception.getMessage());
                        });
            }
        }
    }
}
