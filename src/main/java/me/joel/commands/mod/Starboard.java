package me.joel.commands.mod;

import me.joel.Console;
import me.joel.Database;
import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
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

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;

        Emoji star = Emoji.fromUnicode("U+2B50");
        User reactor = event.getUser();

        event.retrieveMessage().map(message -> {

            User user = message.getAuthor();

            if (message.getReaction(star) == null) return null;

            int count = message.getReaction(star).getCount();

            //  Checks against self_star
            if (user == reactor) {
                String sql = "SELECT star_self FROM main.starboard_settings WHERE guild_id=" + event.getGuild().getId();
                boolean self_star;

                try {
                    ResultSet set = Database.getConnect().createStatement().executeQuery(sql);

                    self_star = set.getBoolean(1);

                    if (!self_star) {
                        count++;
                        return null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            int limit = 3;
            String msg = message.getContentStripped();

            try {
                String sql1 = "SELECT star_limit FROM starboard_settings WHERE guild_id=" + event.getGuild().getId();
                ResultSet set = Database.getConnect().createStatement().executeQuery(sql1);

                limit = set.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            if (count >= limit) {

                // Get ID
                String sql = "SELECT starboard_ch FROM starboard_settings WHERE guild_id=" + event.getGuild().getId();
                TextChannel channel;
                try {
                    ResultSet set = Database.getConnect().createStatement().executeQuery(sql);

                    String channelID = set.getString(1);

                    if (channelID == null) return null;

                    channel = event.getGuild().getTextChannelById(channelID);

                    EmbedBuilder builder = new EmbedBuilder()
                            .setAuthor(user.getName(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                            .setDescription(msg)
                            .addField("Source", "[Click here](" + event.getJumpUrl() + ")", false)
                            .setColor(Util.randColor());

                    channel.sendMessageEmbeds(builder.build()).queue();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }).queue();

    }
}
