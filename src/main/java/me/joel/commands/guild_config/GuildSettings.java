package me.joel.commands.guild_config;

import me.joel.Console;
import me.joel.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;

public class GuildSettings extends ListenerAdapter {

    public static HashMap<Guild, String> starboard_channel = new HashMap<>();
    public static HashMap<Guild, Integer> starboard_limit = new HashMap<>();
    public static HashMap<Guild, Boolean> starboard_self = new HashMap<>();

    public static HashMap<Guild, String> confession_channel = new HashMap<>();
    public static HashMap<Guild, String> mod_channel = new HashMap<>();
    public static HashMap<Guild, String> join_channel = new HashMap<>();
    public static HashMap<Guild, String> leave_channel = new HashMap<>();

    public static HashMap<Guild, Boolean> insults = new HashMap<>();
    public static HashMap<Guild, Boolean> now_playing = new HashMap<>();
    public static HashMap<Guild, Boolean> gm_gn = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("config_view")) {
            Guild guild = event.getGuild();

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Server Configuration");

            if (confession_channel.get(guild) != null) {
                builder.addField("Confession channel", guild.getTextChannelById(confession_channel.get(guild)).getAsMention(), false);
            }
            else {
                builder.addField("Confession channel", "Not set", false);
            }

            if (mod_channel.get(guild) != null) {
                builder.addField("Mod channel", guild.getTextChannelById(mod_channel.get(guild)).getAsMention(), false);
            }
            else {
                builder.addField("Mod channel", "Not set", false);
            }

            if (join_channel.get(guild) != null) {
                builder.addField("Join channel", guild.getTextChannelById(join_channel.get(guild)).getAsMention(), false);
            }
            else {
                builder.addField("Join channel", "Not set", false);
            }

            if (leave_channel.get(guild) != null) {
                builder.addField("Leave channel", guild.getTextChannelById(leave_channel.get(guild)).getAsMention(), false);
            }
            else {
                builder.addField("Leave channel", "Not set", false);
            }

            if (starboard_channel.get(guild) != null) {
                builder.addField("Starboard channel", guild.getTextChannelById(starboard_channel.get(guild)).getAsMention(), false);
            }
            else {
                builder.addField("Starboard channel", "Not set", false);
            }

            if (insults.get(event.getGuild()))
            {
                builder.addField("Insults", "Activated", false);
            }
            else {
                builder.addField("Insults", "Deactivated", false);
            }

            if (gm_gn.get(event.getGuild()))
            {
                builder.addField("Gm/Gn Messages", "Activated", false);
            }
            else {
                builder.addField("Gm/Gn Messages", "Deactivated", false);
            }

            if (now_playing.get(event.getGuild()))
            {
                builder.addField("Now Playing Messages", "Activated", false);
            }
            else {
                builder.addField("Now Playing", "Deactivated", false);
            }

            builder.setFooter(guild.getId());

            event.replyEmbeds(builder.build()).queue();
        }

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
                        join_channel.put(event.getGuild(), ch.getId());
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
                        mod_channel.put(event.getGuild(), ch.getId());
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
                        leave_channel.put(event.getGuild(), ch.getId());
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
                        starboard_channel.put(event.getGuild(), ch.getId());
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
                        starboard_channel.put(event.getGuild(), ch.getId());
                    } catch (SQLException e) {
                        Console.warn("Failed to configure guild confession channel");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
