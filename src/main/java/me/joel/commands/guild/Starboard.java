package me.joel.commands.guild;

import me.joel.Console;
import me.joel.Database;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class Starboard extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("star_self")) {
            boolean can_star = event.getOption("can_star").getAsBoolean();

            if (can_star) {
                String sql = "UPDATE starboard_settings SET star_self=1 WHERE guild_id=" + event.getGuild().getId();

                try {
                    Database.getConnect().createStatement().execute(sql);
                } catch (SQLException e) {
                    Console.warn("Failed to configure starboard settings");
                    e.printStackTrace();
                }

                event.reply("Users can now star their own messages").queue();
            }
            else {
                String sql = "UPDATE starboard_settings SET star_self=0 WHERE guild_id=" + event.getGuild().getId();

                try {
                    Database.getConnect().createStatement().execute(sql);
                } catch (SQLException e) {
                    Console.warn("Failed to configure starboard settings");
                    e.printStackTrace();
                }

                event.reply("Users can no longer star their own messages").queue();
            }
        }

        if (event.getName().equals("star_limit")) {
            int num = event.getOption("num").getAsInt();
            if (num <= 0) num = 1;

            String sql = "UPDATE starboard_settings SET star_limit= " + num + " WHERE guild_id=" + event.getGuild().getId();

            try {
                Database.getConnect().createStatement().execute(sql);
            } catch (SQLException e) {
                Console.warn("Failed to configure starboard settings");
                e.printStackTrace();
            }

            event.reply("Star limit set to: `" + num + "`").queue();
        }
    }
}
