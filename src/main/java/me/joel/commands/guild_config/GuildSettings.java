package me.joel.commands.guild_config;

import me.joel.Console;
import me.joel.Database;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class GuildSettings extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("config")) {
            event.deferReply().queue();

            var sub_invoke = event.getSubcommandName();

            GuildChannelUnion channel = event.getOption("channel").getAsChannel();
            TextChannel ch = channel.asTextChannel();

            switch (sub_invoke) {
                case "join" -> {
                    String sql = "UPDATE \"public\".\"guild_settings\" SET join_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

                    try {
                        Database.getConnect().createStatement().execute(sql);
                        event.getHook().sendMessage("Join channel set to: " + ch.getAsMention()).queue();
                    } catch (SQLException e) {
                        Console.warn("Failed to configure guild join channel");
                        e.printStackTrace();
                    }
                }
                case "mod" -> {
                    String sql = "UPDATE \"public\".\"guild_settings\" SET mod_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

                    try {
                        Database.getConnect().createStatement().execute(sql);
                        event.getHook().sendMessage("Moderation channel set to: " + ch.getAsMention()).queue();
                    } catch (SQLException e) {
                        Console.warn("Failed to configure guild join channel");
                        e.printStackTrace();
                    }
                }
                case "leave" -> {
                    String sql = "UPDATE \"public\".\"guild_settings\" SET leave_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

                    try {
                        Database.getConnect().createStatement().execute(sql);
                        event.getHook().sendMessage("Leave channel set to: " + ch.getAsMention()).queue();
                    } catch (SQLException e) {
                        Console.warn("Failed to configure guild leave channel");
                        e.printStackTrace();
                    }
                }
                case "star" -> {
                    String sql = "UPDATE \"public\".\"starboard_settings\" SET starboard_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

                    try {
                        Database.getConnect().createStatement().execute(sql);
                        event.getHook().sendMessage("Starboard channel set to: " + ch.getAsMention()).queue();
                    } catch (SQLException e) {
                        Console.warn("Failed to configure guild starboard channel");
                        e.printStackTrace();
                    }
                }
                case "confess" -> {
                    String sql = "UPDATE \"public\".\"guild_settings\" SET confession_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

                    try {
                        Database.getConnect().createStatement().execute(sql);
                        event.getHook().sendMessage("Confession channel set to: " + ch.getAsMention()).queue();
                    } catch (SQLException e) {
                        Console.warn("Failed to configure guild confession channel");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
