package me.joel.commands.mod;

import me.joel.Console;
import me.joel.Database;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class ChannelSettings extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("set_join")) {
            event.deferReply().queue();

            GuildChannelUnion channel = event.getOption("channel").getAsChannel();

            TextChannel ch = channel.asTextChannel();

            String sql = "UPDATE guild_settings SET join_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                Database.getConnect().createStatement().execute(sql);
            } catch (SQLException e) {
                Console.warn("Failed to configure guild join channel");
                e.printStackTrace();
            }

            event.getHook().sendMessage("Join channel set to: " + ch.getAsMention()).queue();
        }

        if (event.getName().equals("set_mod")) {
            event.deferReply().queue();

            GuildChannelUnion channel = event.getOption("channel").getAsChannel();

            TextChannel ch = channel.asTextChannel();

            String sql = "UPDATE guild_settings SET mod_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                Database.getConnect().createStatement().execute(sql);
            } catch (SQLException e) {
                Console.warn("Failed to configure guild join channel");
                e.printStackTrace();
            }

            event.getHook().sendMessage("Moderation channel set to: " + ch.getAsMention()).queue();
        }

        if (event.getName().equals("set_leave")) {
            event.deferReply().queue();

            GuildChannelUnion channel = event.getOption("channel").getAsChannel();

            TextChannel ch = channel.asTextChannel();

            String sql = "UPDATE guild_settings SET leave_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                Database.getConnect().createStatement().execute(sql);
            } catch (SQLException e) {
                Console.warn("Failed to configure guild leave channel");
                e.printStackTrace();
            }

            event.getHook().sendMessage("Leave channel set to: " + ch.getAsMention()).queue();
        }

        if (event.getName().equals("set_star")) {
            event.deferReply().queue();

            GuildChannelUnion channel = (event.getOption("channel")).getAsChannel();

            TextChannel ch = channel.asTextChannel();;

            String sql = "UPDATE starboard_settings SET starboard_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                Database.getConnect().createStatement().execute(sql);
            } catch (SQLException e) {
                Console.warn("Failed to configure guild starboard channel");
                e.printStackTrace();
            }

            event.getHook().sendMessage("Starboard channel set to: " + ch.getAsMention()).queue();
        }

        if (event.getName().equals("set_confess")) {
            event.deferReply().queue();

            GuildChannelUnion channel = event.getOption("channel").getAsChannel();

            TextChannel ch = channel.asTextChannel();;

            String sql = "UPDATE guild_settings SET confession_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

            try {
                Database.getConnect().createStatement().execute(sql);
            } catch (SQLException e) {
                Console.warn("Failed to configure guild confession channel");
                e.printStackTrace();
            }

            event.getHook().sendMessage("Confession channel set to: " + ch.getAsMention()).queue();
        }
    }
}
