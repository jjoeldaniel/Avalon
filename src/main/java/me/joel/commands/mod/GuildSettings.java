package me.joel.commands.mod;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
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

        if (event.getName().equals("config_view")) {
            Guild guild = event.getGuild();

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Server Configuration");

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
    }
}
