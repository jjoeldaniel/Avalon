package me.joel.commands.guild_config;

import me.joel.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;

public class GuildSettings extends ListenerAdapter {

    public static HashMap<Guild, Long> starboard_channel = new HashMap<>();
    public static HashMap<Guild, Integer> starboard_limit = new HashMap<>();
    public static HashMap<Guild, Boolean> starboard_self = new HashMap<>();

    public static HashMap<Guild, Long> confession_channel = new HashMap<>();
    public static HashMap<Guild, Long> mod_channel = new HashMap<>();
    public static HashMap<Guild, Long> join_channel = new HashMap<>();
    public static HashMap<Guild, Long> leave_channel = new HashMap<>();

    public static HashMap<Guild, Boolean> insults = new HashMap<>();
    public static HashMap<Guild, Boolean> now_playing = new HashMap<>();
    public static HashMap<Guild, Boolean> gm_gn = new HashMap<>();

    // SL4FJ Logger
    final Logger log = LoggerFactory.getLogger( GuildSettings.class );

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        final String URL = System.getenv( "DATABASE_URL" );
        final String USER = System.getenv( "DATABASE_USER" );
        final String PASSWORD = System.getenv( "DATABASE_PASSWORD" );

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

            builder.addField("Star Limit", String.valueOf(starboard_limit.get(event.getGuild())), false);

            if (starboard_self.get(event.getGuild()))
            {
                builder.addField("Self Star", "Activated", false);
            }
            else {
                builder.addField("Self Star", "Deactivated", false);
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
                    updateChannel("join_ch", event.getGuild(), ch.getId());
                    join_channel.put(event.getGuild(), Long.valueOf( ch.getId() ) );
                    event.getHook().sendMessage("Join channel set to: " + ch.getAsMention()).queue();
                }
                case "mod" -> {
                    updateChannel("mod_ch", event.getGuild(), ch.getId());
                    mod_channel.put(event.getGuild(), Long.valueOf( ch.getId() ) );
                    event.getHook().sendMessage("Mod channel set to: " + ch.getAsMention()).queue();
                }
                case "leave" -> {
                    updateChannel("leave_ch", event.getGuild(), ch.getId());
                    leave_channel.put(event.getGuild(), Long.valueOf( ch.getId() ) );
                    event.getHook().sendMessage("Leave channel set to: " + ch.getAsMention()).queue();
                }
                case "star" -> {
                    String sql = "UPDATE \"public\".\"starboard_settings\" SET starboard_ch=" + ch.getId() + " WHERE guild_id=" + event.getGuild().getId();

                    try (Connection conn = DriverManager.getConnection( URL, USER, PASSWORD )) {
                        conn.createStatement().execute( sql );
                        event.getHook().sendMessage("Starboard channel set to: " + ch.getAsMention()).queue();
                        starboard_channel.put(event.getGuild(), Long.valueOf( ch.getId() ) );
                    } catch (SQLException e) {
                        log.error("Failed to configure guild starboard channel", e);
                    }
                }
                case "confess" -> {
                    updateChannel("confession_ch", event.getGuild(), ch.getId());
                    confession_channel.put(event.getGuild(), Long.valueOf( ch.getId() ) );
                    event.getHook().sendMessage("Confession channel set to: " + ch.getAsMention()).queue();
                }
            }
        }
    }

    void updateChannel(String type, Guild guild, String ch_id) {
        final String URL = System.getenv( "DATABASE_URL" );
        final String USER = System.getenv( "DATABASE_USER" );
        final String PASSWORD = System.getenv( "DATABASE_PASSWORD" );

        String sql = "UPDATE \"public\".\"guild_settings\" SET " + type + "=" + ch_id + " WHERE guild_id=" + guild.getId();

        try (Connection conn = DriverManager.getConnection( URL, USER, PASSWORD )) {
            conn.createStatement().execute( sql );
        } catch (SQLException e) {
            log.error("Failed to configure guild " + type + " channel", e);
        }
    }
}
