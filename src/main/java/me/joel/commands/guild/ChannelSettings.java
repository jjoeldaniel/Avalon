package me.joel.commands.guild;

import me.joel.Console;
import me.joel.Database;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class ChannelSettings extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("set_join")) {
            GuildChannelUnion channel = (event.getOption("channel")).getAsChannel();

            TextChannel ch = channel.asTextChannel();
            Connection conn = Database.getConnect();

            String sql = "UPDATE guild_settings SET join_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                conn.createStatement().execute(sql);
                conn.close();
            } catch (SQLException e) {
                Console.warn("Failed to configure guild leave channel");
            }

            event.reply("Join channel set to: " + ch.getAsMention()).queue();
        }

        if (event.getName().equals("set_leave")) {
            GuildChannelUnion channel = (event.getOption("channel")).getAsChannel();

            TextChannel ch = channel.asTextChannel();
            Connection conn = Database.getConnect();

            String sql = "UPDATE guild_settings SET leave_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                conn.createStatement().execute(sql);
                conn.close();
            } catch (SQLException e) {
                Console.warn("Failed to configure guild join channel");
            }

            event.reply("Leave channel set to: " + ch.getAsMention()).queue();
        }

        if (event.getName().equals("set_confess")) {
            GuildChannelUnion channel = (event.getOption("channel")).getAsChannel();

            TextChannel ch = channel.asTextChannel();
            Connection conn = Database.getConnect();

            String sql = "UPDATE guild_settings SET confession_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                conn.createStatement().execute(sql);
                conn.close();
            } catch (SQLException e) {
                Console.warn("Failed to configure guild confession channel");
            }

            event.reply("Confession channel set to: " + ch.getAsMention()).queue();
        }
    }
}
