package periodicallyprogramming.avalon.commands.mod;

import periodicallyprogramming.avalon.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Toggle extends ListenerAdapter {

    private static boolean insults = true;
    private static boolean gmgn = true;
    private static boolean nowPlaying = true;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("toggle")) {

            var sub_invoke = event.getSubcommandName();

            switch (sub_invoke) {
                case ("insults") -> {
                    String sql = "SELECT insults FROM main.guild_settings WHERE guild_id= " + event.getGuild().getId();
                    try {
                        ResultSet rs = Database.getConnect().createStatement().executeQuery(sql);
                        int sel = rs.getInt(1);

                        // enabled
                        if (sel == 1) {
                            String sql2 = "UPDATE guild_settings SET insults=0 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }
                        // disabled
                        else if (sel == 0) {
                            String sql2 = "UPDATE guild_settings SET insults=1 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    insults = !insults;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.green)
                            .setDescription("Insults are now toggled `ON`");

                    if (!insults) {
                        builder.setColor(Color.red);
                        builder.setDescription("Insults are now toggled `OFF`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
                case ("goodmorning_goodnight") -> {
                    String sql = "SELECT gm_gn FROM main.guild_settings WHERE guild_id= " + event.getGuild().getId();
                    try {
                        ResultSet rs = Database.getConnect().createStatement().executeQuery(sql);
                        int sel = rs.getInt(1);

                        // enabled
                        if (sel == 1) {
                            String sql2 = "UPDATE guild_settings SET gm_gn=0 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }
                        // disabled
                        else if (sel == 0) {
                            String sql2 = "UPDATE guild_settings SET gm_gn=1 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    gmgn = !gmgn;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.green)
                            .setDescription("Messages are now toggled `ON`");

                    if (!gmgn) {
                        builder.setColor(Color.red);
                        builder.setDescription("Messages are now toggled `OFF`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
                case ("now_playing") -> {
                    String sql = "SELECT now_playing FROM main.guild_settings WHERE guild_id= " + event.getGuild().getId();
                    try {
                        ResultSet rs = Database.getConnect().createStatement().executeQuery(sql);
                        int sel = rs.getInt(1);

                        // enabled
                        if (sel == 1) {
                            String sql2 = "UPDATE guild_settings SET now_playing=0 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }
                        // disabled
                        else if (sel == 0) {
                            String sql2 = "UPDATE guild_settings SET now_playing=1 WHERE guild_id=" + event.getGuild().getId();
                            Database.getConnect().createStatement().execute(sql2);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    nowPlaying = !nowPlaying;

                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.green)
                            .setDescription("`Now Playing` messages are now toggled `ON`");

                    if (!nowPlaying) {
                        builder.setColor(Color.red);
                        builder.setDescription("`Now Playing` messages are now toggled `OFF`");
                    }

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }
            }
        }
    }

    public static boolean insultsEnabled() {
        return insults;
    }

    public static boolean gmgnEnabled() {
        return gmgn;
    }

    public static boolean isNowPlaying() {
        return nowPlaying;
    }
}
