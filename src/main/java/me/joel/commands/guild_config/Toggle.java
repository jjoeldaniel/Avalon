package me.joel.commands.guild_config;

import me.joel.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Toggle extends ListenerAdapter {

    private static boolean nowPlaying = true;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("toggle")) {

            var sub_invoke = event.getSubcommandName();

            switch (sub_invoke) {
                case ("insults") -> {
                    String sql = "SELECT insults FROM \"public\".\"guild_settings\" WHERE guild_id= " + event.getGuild().getId();
                    try {
                        ResultSet rs = Database.getConnect().createStatement().executeQuery(sql);
                        rs.next();
                        int sel = rs.getInt(1);

                        // enabled
                        if (sel == 1) {
                            String sql2 = "UPDATE \"public\".\"guild_settings\" SET insults=0 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }
                        // disabled
                        else if (sel == 0) {
                            String sql2 = "UPDATE \"public\".\"guild_settings\" SET insults=1 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }

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
                    String sql = "SELECT gm_gn FROM \"public\".\"guild_settings\" WHERE guild_id= " + event.getGuild().getId();
                    try {
                        ResultSet rs = Database.getConnect().createStatement().executeQuery(sql);
                        rs.next();
                        int sel = rs.getInt(1);

                        // enabled
                        if (sel == 1) {
                            String sql2 = "UPDATE \"public\".\"guild_settings\" SET gm_gn=0 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }
                        // disabled
                        else if (sel == 0) {
                            String sql2 = "UPDATE \"public\".\"guild_settings\" SET gm_gn=1 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }

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
                    String sql = "SELECT now_playing FROM \"public\".\"guild_settings\" WHERE guild_id= " + event.getGuild().getId();
                    try {
                        ResultSet rs = Database.getConnect().createStatement().executeQuery(sql);
                        rs.next();
                        int sel = rs.getInt(1);

                        // enabled
                        if (sel == 1) {
                            String sql2 = "UPDATE \"public\".\"guild_settings\" SET now_playing=0 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }
                        // disabled
                        else if (sel == 0) {
                            String sql2 = "UPDATE \"public\".\"guild_settings\" SET now_playing=1 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }

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
