package me.joel.games;

import me.joel.Console;
import me.joel.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;

public class Bank extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("bank")) {
            String id = event.getMember().getId();
            int bal = 0;

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

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
