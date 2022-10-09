package me.joel.games;

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
                bal = Database.getWallet(id);
            } catch (SQLException ignore) {}

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.green)
                    .setTitle("Bank Balance")
                    .setDescription("$" + String.valueOf(bal));

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
