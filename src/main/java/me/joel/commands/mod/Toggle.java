package me.joel.commands.mod;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Toggle extends ListenerAdapter {

    private static boolean nowPlaying = true;

    final private static String URL = System.getenv( "DATABASE_URL" );
    final private static String USER = System.getenv( "DATABASE_USER" );
    final private static String PASSWORD = System.getenv( "DATABASE_PASSWORD" );

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("toggle")) {

            var sub_invoke = event.getSubcommandName();

            switch (sub_invoke) {
                case ("insults") -> {
                    String sql = "UPDATE \"public\".\"guild_settings\" SET insults = NOT insults WHERE guild_id=" + event.getGuild().getId();

                    // connect here
                    try ( Connection conn = DriverManager.getConnection( URL, USER, PASSWORD ) ) {
                        conn.createStatement().execute( sql );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    boolean new_value = !GuildSettings.insults.get(event.getGuild());
                    GuildSettings.insults.put(event.getGuild(), new_value);

                    EmbedBuilder builder = new EmbedBuilder();
                    if (!new_value) {
                        builder.setColor(Color.red);
                        builder.setDescription("Insults are now toggled `OFF`");
                    }
                    else {
                        builder.setColor(Color.green);
                        builder.setDescription("Insults are now toggled `ON`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
                case ("goodmorning_goodnight") -> {
                    String sql = "UPDATE \"public\".\"guild_settings\" SET gm_gn = NOT gm_gn WHERE guild_id=" + event.getGuild().getId();

                    // connect here
                    try ( Connection conn = DriverManager.getConnection( URL, USER, PASSWORD ) ) {
                        conn.createStatement().execute( sql );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    boolean new_value = !GuildSettings.gm_gn.get(event.getGuild());
                    GuildSettings.gm_gn.put(event.getGuild(), new_value);

                    EmbedBuilder builder = new EmbedBuilder();
                    if (!new_value) {
                        builder.setColor(Color.red);
                        builder.setDescription("Messages are now toggled `OFF`");
                    }
                    else {
                        builder.setColor(Color.green);
                        builder.setDescription("Messages are now toggled `ON`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
                case ("now_playing") -> {
                    
                    String sql = "UPDATE \"public\".\"guild_settings\" SET now_playing = NOT now_playing WHERE guild_id=" + event.getGuild().getId();

                    // connect here
                    try ( Connection conn = DriverManager.getConnection( URL, USER, PASSWORD ) ) {
                        conn.createStatement().execute( sql );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    boolean new_value = !GuildSettings.now_playing.get(event.getGuild());
                    GuildSettings.now_playing.put(event.getGuild(), new_value);

                    EmbedBuilder builder = new EmbedBuilder();
                    if (!new_value) {
                        builder.setColor(Color.red);
                        builder.setDescription("`Now Playing` are now toggled `OFF`");
                    }
                    else {
                        builder.setColor(Color.green);
                        builder.setDescription("`Now Playing` are now toggled `ON`");
                    }

                    nowPlaying = new_value;

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }
        }
    }

    public static boolean isNowPlaying() {
        return nowPlaying;
    }
}
