package me.joel.games;

import me.joel.Console;
import me.joel.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;

public class Bank extends ListenerAdapter {

    private final Button refresh = Button.primary("refresh", "Refresh balance");
    private String id = "";
    private int bal = 0;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("bank")) {
            id = event.getMember().getId();

            try {
                Database.getWallet(id);
                bal = Database.getWallet(id);
            } catch (SQLException e) {
                Console.warn("Failed to retrieve user balance");
                e.printStackTrace();
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.green)
                    .setTitle("Bank Balance")
                    .setDescription("$" + bal);

            event.replyEmbeds(builder.build()).setActionRow(refresh).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (event.getComponentId().equals("refresh")) {
            if (!event.getMember().getId().equals(id)) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("You can't refresh another persons bank account, silly!");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }

            try {
                Database.getWallet(id);
                bal = Database.getWallet(id);
            } catch (SQLException e) {
                Console.warn("Failed to retrieve user balance");
                e.printStackTrace();
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.green)
                    .setTitle("Bank Balance")
                    .setDescription("$" + bal);

            event.editMessageEmbeds(builder.build()).queue();
        }
    }
}
